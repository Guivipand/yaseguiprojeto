package com.ifsp.yas1.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        liberarPapelAdminNoBanco();
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

    private void liberarPapelAdminNoBanco() {
        List<String> restricoes = jdbcTemplate.queryForList(
                "select cc.constraint_name from information_schema.check_constraints cc "
                + "join information_schema.table_constraints tc "
                + "on tc.constraint_schema = cc.constraint_schema and tc.constraint_name = cc.constraint_name "
                + "where tc.table_schema = database() and tc.table_name = 'pessoa' "
                + "and cc.check_clause like '%role%' and cc.check_clause not like ?",
                String.class, "%" + (PessoaRole.values().length - 1) + "%");
        for (String restricao : restricoes) {
            jdbcTemplate.execute("alter table pessoa drop constraint `" + restricao.replace("`", "") + "`");
        }
    }
}
