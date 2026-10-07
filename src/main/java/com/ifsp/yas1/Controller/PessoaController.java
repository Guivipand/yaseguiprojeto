package com.ifsp.yas1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ifsp.yas1.Model.Pessoa;
import com.ifsp.yas1.Repository.PessoaRepository;

@Controller
public class PessoaController {

    @Autowired
    private PessoaRepository repositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Página usada pelo .loginPage("/login") do WebSecurityConfig
    @GetMapping("/login")
    public String login() {
        return "sessao/login.html";
    }

    @GetMapping("/register")
    public String formCadastrarUsuario() {
        return "sessao/formCadastrarUsuario.html";
    }

    @PostMapping("/register")
    public String cadastrar(@RequestParam String username, @RequestParam String password, Model model) throws Exception{
        if (repositorio.findByUsername(username)==null) {
            Pessoa pessoa = new Pessoa();
            pessoa.setUsername(username);
            pessoa.setPassword(passwordEncoder.encode(password));
            repositorio.save(pessoa);
            System.out.println("Usuário " + username + " criado com sucesso!");
            model.addAttribute("mensagemSucesso", "Usuário criado com sucesso!");
        } else {
            model.addAttribute("erroUsuario", "Já existe um usuário com esse nome!");
        }
        return "sessao/formCadastrarUsuario.html";
    }
}
