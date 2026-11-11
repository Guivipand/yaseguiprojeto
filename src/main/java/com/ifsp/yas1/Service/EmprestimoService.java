package com.ifsp.yas1.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ifsp.yas1.Model.Aluno;
import com.ifsp.yas1.Model.Emprestimo;
import com.ifsp.yas1.Model.Livro;
import com.ifsp.yas1.Repository.EmprestimoRepository;
import com.ifsp.yas1.Repository.MatriculaRepository;

@Service
public class EmprestimoService {

    public static final int LIMITE_EMPRESTIMOS = 3;

    @Value("${biblioteca.suspensao-dias:7}")
    private int diasSuspensao;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    public String verificarRegras(Aluno aluno, Livro livro) {
        int idAluno = aluno.getId();
        if (emprestimoRepository.contarAtrasadosDoAluno(idAluno, LocalDate.now()) > 0) {
            return "O aluno tem empréstimo em atraso. Regularize a devolução para liberar novas retiradas (RN04).";
        }
        LocalDate suspensoAte = suspensoAte(idAluno);
        if (suspensoAte != null) {
            return "O aluno está suspenso até " + suspensoAte.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + " por ter devolvido um livro com atraso (RN09).";
        }
        if (emprestimoRepository.contarAtivosDoAluno(idAluno) >= LIMITE_EMPRESTIMOS) {
            return "Limite de " + LIMITE_EMPRESTIMOS + " empréstimos ativos atingido. Devolva um livro e tente novamente (RN01).";
        }
        if (matriculaRepository.contarBibliografiaDoAluno(idAluno, livro.getId()) == 0) {
            return "Este livro não está na bibliografia de nenhuma disciplina em que o aluno tem matrícula ativa (RN05).";
        }
        return null;
    }

    public LocalDate suspensoAte(int aluno) {
        LocalDate ultimaAtrasada = emprestimoRepository.ultimaDevolucaoAtrasada(aluno);
        if (ultimaAtrasada == null || diasSuspensao <= 0) {
            return null;
        }
        LocalDate fim = ultimaAtrasada.plusDays(diasSuspensao);
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

    @Scheduled(cron = "0 5 0 * * *")
    public void rotinaDiaria() {
        atualizarAtrasos();
    }
}
