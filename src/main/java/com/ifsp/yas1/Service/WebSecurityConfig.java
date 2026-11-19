package com.ifsp.yas1.Service;

import java.util.Arrays;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http, PersistentTokenRepository tokenRepository) throws Exception {
        return http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/css/**").permitAll()
                .requestMatchers(crud("Usuario")).hasRole("ADMIN")
                .requestMatchers("/listaUsuarios").hasRole("ADMIN")
                .requestMatchers("/listaExemplares", "/detalhesExemplar").hasAnyRole("ADMIN", "BIBLIOTECARIO")
                .requestMatchers("/listaEmprestimos", "/detalhesEmprestimo", "/listaReservas", "/detalhesReserva")
                        .hasAnyRole("ADMIN", "BIBLIOTECARIO", "ALUNO")
                .requestMatchers("/listaProfessores", "/detalhesProfessor", "/listaCursos", "/listaDisciplinas",
                        "/listaOfertaDisciplinas", "/listaMatriculas", "/listaProvas", "/detalhesProva")
                        .hasAnyRole("ADMIN", "PROFESSOR", "ALUNO")
                .requestMatchers("/formularioMatricula", "/cadastrarMatricula").hasAnyRole("ADMIN", "ALUNO")
                .requestMatchers("/formularioEmprestimo", "/cadastrarEmprestimo", "/renovarEmprestimo",
                        "/formularioReserva", "/cadastrarReserva", "/cancelarReserva").hasAnyRole("ADMIN", "BIBLIOTECARIO", "ALUNO")
                .requestMatchers("/marcarProvaEstudada", "/desmarcarProvaEstudada").hasRole("ALUNO")
                .requestMatchers(crud("Prova", "OfertaDisciplina", "DisciplinaLivro")).hasAnyRole("ADMIN", "PROFESSOR")
                .requestMatchers(crud("Livro", "Exemplar", "Emprestimo", "Reserva")).hasAnyRole("ADMIN", "BIBLIOTECARIO")
                .requestMatchers("/devolverEmprestimo").hasAnyRole("ADMIN", "BIBLIOTECARIO")
                .requestMatchers(crud("Curso", "Disciplina", "Professor", "Aluno", "Matricula", "Bibliotecario")).hasRole("ADMIN")
                .requestMatchers("/listaBibliotecarios", "/detalhesBibliotecario").hasRole("ADMIN")
                .requestMatchers("/listaAlunos", "/detalhesAluno").hasAnyRole("ADMIN", "PROFESSOR", "BIBLIOTECARIO")
                .anyRequest().authenticated())
            .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/menu", true).permitAll())
            .rememberMe(lembrar -> lembrar
                .tokenRepository(tokenRepository)
                .rememberMeParameter("lembrar")
                .tokenValiditySeconds(7 * 24 * 60 * 60))
            .build();
    }

    private static String[] crud(String... entidades) {
        return Arrays.stream(entidades)
                .flatMap(e -> Stream.of("formulario", "cadastrar", "editar", "atualizar", "excluir").map(acao -> "/" + acao + e))
                .toArray(String[]::new);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        new JdbcTemplate(dataSource).execute("CREATE TABLE IF NOT EXISTS persistent_logins (username VARCHAR(64) NOT NULL, "
                + "series VARCHAR(64) PRIMARY KEY, token VARCHAR(64) NOT NULL, last_used TIMESTAMP NOT NULL)");
        JdbcTokenRepositoryImpl repositorio = new JdbcTokenRepositoryImpl();
        repositorio.setDataSource(dataSource);
        return repositorio;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
