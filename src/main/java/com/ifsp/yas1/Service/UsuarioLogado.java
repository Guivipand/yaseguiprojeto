package com.ifsp.yas1.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ifsp.yas1.Model.Aluno;
import com.ifsp.yas1.Model.Pessoa;
import com.ifsp.yas1.Repository.AlunoRepository;

@Service
public class UsuarioLogado {

    public static final String SEM_ALUNO = "Seu usuário não está ligado a um aluno. Peça a um administrador para fazer o vínculo.";

    @Autowired
    private AlunoRepository alunoRepository;

    public Pessoa pessoa() {
        Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao != null && autenticacao.getPrincipal() instanceof Pessoa pessoa) {
            return pessoa;
        }
        return null;
    }

    public boolean ehAluno() {
        Pessoa pessoa = pessoa();
        return pessoa != null && pessoa.getRole() == PessoaRole.ALUNO;
    }

    public Aluno aluno() {
        return ehAluno() ? alunoRepository.findById(pessoa().getId()).orElse(null) : null;
    }

    public int idAluno() {
        Aluno aluno = aluno();
        return aluno != null ? aluno.getId() : -1;
    }

    public void exigirDono(Aluno dono) {
        if (ehAluno() && (dono == null || dono.getId() != idAluno())) {
            throw new AccessDeniedException("Registro de outro aluno");
        }
    }
}
