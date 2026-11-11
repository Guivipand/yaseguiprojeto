package com.ifsp.yas1.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ifsp.yas1.Model.Aluno;
import com.ifsp.yas1.Model.Exemplar;
import com.ifsp.yas1.Model.Reserva;
import com.ifsp.yas1.Repository.EmprestimoRepository;
import com.ifsp.yas1.Repository.ExemplarRepository;
import com.ifsp.yas1.Repository.MatriculaRepository;
import com.ifsp.yas1.Repository.ReservaRepository;

@Service
public class ReservaService {

    public static final long HORAS_PARA_RETIRADA = 48;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ExemplarRepository exemplarRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Transactional
    public String reservar(Reserva reserva) {
        processarExpiradas();
        int livro = reserva.getLivro().getId();
        int aluno = reserva.getAluno().getId();

        if (exemplarRepository.contarPorLivro(livro) == 0) {
            return "Este livro ainda não tem exemplares no acervo.";
        }
        if (exemplarRepository.contarPorLivroEStatus(livro, "Disponível") > 0) {
            return "Há exemplar disponível deste livro: registre o empréstimo em vez de reservar.";
        }
        if (matriculaRepository.contarBibliografiaDoAluno(aluno, livro) == 0) {
            return "O livro não está na bibliografia de nenhuma disciplina em que o aluno tem matrícula ativa (RN05).";
        }
        if (!reservaRepository.listarPendentesDoAluno(aluno, livro).isEmpty()) {
            return "O aluno já está na fila de reserva deste livro.";
        }
        if (emprestimoRepository.contarAtivosDoAlunoPorLivro(aluno, livro) > 0) {
            return "O aluno já está com um exemplar deste livro emprestado.";
        }

        reserva.setData_reserva(LocalDateTime.now());
        reserva.setStatus(Reserva.AGUARDANDO);
        reserva.setExemplar(null);
        reserva.setPrazo_retirada(null);
        reservaRepository.save(reserva);
        recalcularFila(livro);
        return null;
    }

    @Transactional
    public void liberarExemplar(Exemplar exemplar) {
        List<Reserva> fila = reservaRepository.filaDoLivro(exemplar.getLivro().getId());
        if (fila.isEmpty()) {
            exemplar.setStatus("Disponível");
        } else {
            Reserva primeira = fila.get(0);
            primeira.setStatus(Reserva.NOTIFICADA);
            primeira.setExemplar(exemplar);
            primeira.setPrazo_retirada(LocalDateTime.now().plusHours(HORAS_PARA_RETIRADA));
            primeira.setPosicao_fila(null);
            reservaRepository.save(primeira);
            exemplar.setStatus("Reservado");
        }
        exemplarRepository.save(exemplar);
        recalcularFila(exemplar.getLivro().getId());
    }

    @Transactional
    public void processarExpiradas() {
        for (Reserva reserva : reservaRepository.listarExpiradas(LocalDateTime.now())) {
            Exemplar exemplar = reserva.getExemplar();
            reserva.setStatus(Reserva.EXPIRADA);
            reservaRepository.save(reserva);
            if (exemplar != null) {
                liberarExemplar(exemplar);
            }
        }
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void rotinaExpiracao() {
        processarExpiradas();
    }

    public boolean reservadoPara(Exemplar exemplar, Aluno aluno) {
        return reservaRepository.buscarNotificadaDoExemplar(exemplar.getId())
                .map(reserva -> reserva.getAluno().getId() == aluno.getId())
                .orElse(false);
    }

    @Transactional
    public void marcarAtendida(Exemplar exemplarEmprestado, Aluno aluno) {
        int livro = exemplarEmprestado.getLivro().getId();
        for (Reserva reserva : reservaRepository.listarPendentesDoAluno(aluno.getId(), livro)) {
            Exemplar separado = reserva.getExemplar();
            reserva.setStatus(Reserva.ATENDIDA);
            reserva.setPosicao_fila(null);
            reservaRepository.save(reserva);
            if (separado != null && separado.getId() != exemplarEmprestado.getId()) {
                liberarExemplar(separado);
            }
        }
        recalcularFila(livro);
    }

    @Transactional
    public void cancelar(Reserva reserva) {
        if (!reserva.isPendente()) {
            return;
        }
        Exemplar separado = Reserva.NOTIFICADA.equals(reserva.getStatus()) ? reserva.getExemplar() : null;
        reserva.setStatus(Reserva.CANCELADA);
        reserva.setPosicao_fila(null);
        reservaRepository.save(reserva);
        if (separado != null) {
            liberarExemplar(separado);
        } else {
            recalcularFila(reserva.getLivro().getId());
        }
    }

    @Transactional
    public void excluir(Reserva reserva) {
        cancelar(reserva);
        reservaRepository.delete(reserva);
    }

    private void recalcularFila(int livro) {
        List<Reserva> fila = reservaRepository.filaDoLivro(livro);
        for (int i = 0; i < fila.size(); i++) {
            fila.get(i).setPosicao_fila(i + 1);
        }
        reservaRepository.saveAll(fila);
    }
}
