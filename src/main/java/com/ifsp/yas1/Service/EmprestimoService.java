package com.ifsp.yas1.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ifsp.yas1.Model.Aluno;
import com.ifsp.yas1.Model.Emprestimo;
import com.ifsp.yas1.Repository.EmprestimoRepository;
import com.ifsp.yas1.Repository.ReservaRepository;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    public boolean livroReservado(Emprestimo emprestimo) {
        return emprestimo.getExemplar() != null
                && !reservaRepository.filaDoLivro(emprestimo.getExemplar().getLivro().getId()).isEmpty();
    }

    public boolean podeRenovar(Emprestimo emprestimo) {
        return emprestimo.podeRenovar() && !livroReservado(emprestimo);
    }

    public String verificarRegras(Aluno aluno) {
        int idAluno = aluno.getId();
        if (emprestimoRepository.contarAtrasadosDoAluno(idAluno, LocalDate.now()) > 0) {
            return "O aluno tem empréstimo em atraso. Regularize a devolução para liberar novas retiradas.";
        }
        LocalDate suspensoAte = suspensoAte(idAluno);
        if (suspensoAte != null) {
            return "O aluno está suspenso até " + suspensoAte.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + " por ter devolvido um livro com atraso.";
        }
        if (emprestimoRepository.contarAtivosDoAluno(idAluno) >= 3) {
            return "Limite de 3 empréstimos ativos atingido. Devolva um livro e tente novamente.";
        }
        return null;
    }

    public LocalDate suspensoAte(int aluno) {
        LocalDate ultimaAtrasada = emprestimoRepository.ultimaDevolucaoAtrasada(aluno);
        if (ultimaAtrasada == null) {
            return null;
        }
        LocalDate fim = ultimaAtrasada.plusDays(7);
        return LocalDate.now().isAfter(fim) ? null : fim;
    }

    @Transactional
    public void atualizarAtrasos() {
        LocalDate hoje = LocalDate.now();
        for (Emprestimo emprestimo : emprestimoRepository.listarAtivos()) {
            boolean atrasado = emprestimo.getData_prevista() != null && emprestimo.getData_prevista().isBefore(hoje);
            if (atrasado && "Emprestado".equals(emprestimo.getStatus())) {
                emprestimo.setStatus("Atrasado");
                emprestimoRepository.save(emprestimo);
            } else if (!atrasado && "Atrasado".equals(emprestimo.getStatus())) {
                emprestimo.setStatus("Emprestado");
                emprestimoRepository.save(emprestimo);
            }
        }
    }
}
