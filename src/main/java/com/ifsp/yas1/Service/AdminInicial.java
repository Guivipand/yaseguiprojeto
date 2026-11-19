package com.ifsp.yas1.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ifsp.yas1.Model.Pessoa;
import com.ifsp.yas1.Repository.PessoaRepository;

@Component
public class AdminInicial implements ApplicationRunner {

    public static final String LOGIN_ADMIN = "admin";
    public static final String SENHA_ADMIN = "123";

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (pessoaRepository.findByUsername(LOGIN_ADMIN) != null) {
            return;
        }
        Pessoa admin = new Pessoa();
        admin.setNome("Administrador");
        admin.setUsername(LOGIN_ADMIN);
        admin.setPassword(passwordEncoder.encode(SENHA_ADMIN));
        admin.setRole(PessoaRole.ADMIN);
        pessoaRepository.save(admin);
    }
}
