package com.ifsp.yas1.Service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import com.ifsp.yas1.Model.Pessoa;
import com.ifsp.yas1.Model.Professor;
import com.ifsp.yas1.Repository.ProfessorRepository;

class UsuarioLogadoTest {

    private Professor professor(int id) {
        Professor professor = new Professor();
        professor.setId(id);
        professor.setRole(PessoaRole.PROFESSOR);
        return professor;
    }

    private UsuarioLogado logado(Pessoa pessoa) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(pessoa, null, pessoa.getAuthorities()));
        ProfessorRepository professores = mock(ProfessorRepository.class);
        when(professores.findById(pessoa.getId())).thenReturn(Optional.of(professor(pessoa.getId())));
        UsuarioLogado usuarioLogado = new UsuarioLogado();
        ReflectionTestUtils.setField(usuarioLogado, "professorRepository", professores);
        return usuarioLogado;
    }

    @AfterEach
    void limpar() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void professorSoMexeNaPropriaOferta() {
        UsuarioLogado usuarioLogado = logado(professor(1));
        assertDoesNotThrow(() -> usuarioLogado.exigirProfessorDono(professor(1)));
        assertThrows(AccessDeniedException.class, () -> usuarioLogado.exigirProfessorDono(professor(2)));
    }

    @Test
    void adminMexeEmQualquerOferta() {
        Professor admin = professor(9);
        admin.setRole(PessoaRole.ADMIN);
        assertDoesNotThrow(() -> logado(admin).exigirProfessorDono(professor(2)));
    }
}
