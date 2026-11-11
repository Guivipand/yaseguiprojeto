package com.ifsp.yas1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ifsp.yas1.Model.Aluno;
import com.ifsp.yas1.Model.Pessoa;
import com.ifsp.yas1.Model.Professor;
import com.ifsp.yas1.Repository.AlunoRepository;
import com.ifsp.yas1.Repository.PessoaRepository;
import com.ifsp.yas1.Repository.ProfessorRepository;
import com.ifsp.yas1.Service.PessoaRole;

@Controller
public class PessoaController {

    @Autowired
    private PessoaRepository repositorio;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "sessao/login.html";
    }

    @GetMapping("/register")
    public String formCadastrarUsuario(Authentication autenticacao, Model model) {
        adicionarOpcoes(model, autenticacao);
        return "sessao/formCadastrarUsuario.html";
    }

    @PostMapping("/register")
    public String cadastrar(@RequestParam String username, @RequestParam String password, @RequestParam PessoaRole role,
            @RequestParam(required = false) Integer aluno, @RequestParam(required = false) Integer professor,
            Authentication autenticacao, Model model) throws Exception{
        String erro = null;
        Pessoa pessoa = null;
        if (repositorio.findByUsername(username) != null) {
            erro = "Já existe um usuário com esse nome!";
        } else if (role == PessoaRole.ADMIN || role == PessoaRole.BIBLIOTECARIO) {
            if (ehAdmin(autenticacao)) {
                pessoa = new Pessoa();
                pessoa.setNome(username);
            } else {
                erro = "Somente um administrador logado pode cadastrar administradores e bibliotecários.";
            }
        } else if (role == PessoaRole.ALUNO) {
            Aluno vinculado = aluno == null ? null : alunoRepository.findById(aluno).orElse(null);
            if (vinculado == null) {
                erro = "Selecione o aluno ligado a este usuário.";
            } else if (vinculado.getUsername() != null) {
                erro = "Este aluno já tem um usuário cadastrado.";
            } else {
                pessoa = vinculado;
            }
        } else {
            Professor vinculado = professor == null ? null : professorRepository.findById(professor).orElse(null);
            if (vinculado == null) {
                erro = "Selecione o professor ligado a este usuário.";
            } else if (vinculado.getUsername() != null) {
                erro = "Este professor já tem um usuário cadastrado.";
            } else {
                pessoa = vinculado;
            }
        }

        if (erro == null) {
            pessoa.setUsername(username);
            pessoa.setPassword(passwordEncoder.encode(password));
            pessoa.setRole(role);
            repositorio.save(pessoa);
            System.out.println("Usuário " + username + " criado com sucesso!");
            model.addAttribute("mensagemSucesso", "Usuário criado com sucesso!");
        } else {
            model.addAttribute("erroUsuario", erro);
            model.addAttribute("roleSelecionado", role);
            model.addAttribute("alunoSelecionado", aluno);
            model.addAttribute("professorSelecionado", professor);
        }
        adicionarOpcoes(model, autenticacao);
        return "sessao/formCadastrarUsuario.html";
    }

    private void adicionarOpcoes(Model model, Authentication autenticacao) {
        model.addAttribute("alunosSemUsuario", alunoRepository.findByUsernameIsNullOrderByNomeAsc());
        model.addAttribute("professoresSemUsuario", professorRepository.findByUsernameIsNullOrderByNomeAsc());
        model.addAttribute("podeCadastrarAdmin", ehAdmin(autenticacao));
    }

    private boolean ehAdmin(Authentication autenticacao) {
        return autenticacao != null && autenticacao.getAuthorities().stream()
                .anyMatch(permissao -> "ROLE_ADMIN".equals(permissao.getAuthority()));
    }
}
