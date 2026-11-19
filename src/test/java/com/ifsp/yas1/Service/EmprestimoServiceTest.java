package com.ifsp.yas1.Service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.ifsp.yas1.Model.Emprestimo;
import com.ifsp.yas1.Model.Exemplar;
import com.ifsp.yas1.Model.Livro;
import com.ifsp.yas1.Model.Reserva;
import com.ifsp.yas1.Repository.ReservaRepository;

class EmprestimoServiceTest {

    private Emprestimo emprestimoAtivo() {
        Livro livro = new Livro();
        livro.setId(7);
        Exemplar exemplar = new Exemplar();
        exemplar.setLivro(livro);
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setExemplar(exemplar);
        emprestimo.setStatus("Emprestado");
        emprestimo.setData_prevista(LocalDate.now().plusDays(3));
        return emprestimo;
    }

    private EmprestimoService servico(List<Reserva> fila) {
        ReservaRepository reservas = mock(ReservaRepository.class);
        when(reservas.filaDoLivro(7)).thenReturn(fila);
        EmprestimoService servico = new EmprestimoService();
        ReflectionTestUtils.setField(servico, "reservaRepository", reservas);
        return servico;
    }

    @Test
    void renovaQuandoNinguemReservou() {
        assertTrue(servico(List.of()).podeRenovar(emprestimoAtivo()));
    }

    @Test
    void naoRenovaQuandoHaReservaNaFila() {
        EmprestimoService servico = servico(List.of(new Reserva()));
        Emprestimo emprestimo = emprestimoAtivo();
        assertTrue(servico.livroReservado(emprestimo));
        assertFalse(servico.podeRenovar(emprestimo));
    }
}
