package com.ifsp.yas1.Service;

import java.util.ArrayList;
import java.util.List;

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

    private static final int VALIDADE_LEMBRAR_SEGUNDOS = 7 * 24 * 60 * 60;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http, PersistentTokenRepository tokenRepository,
            AutorizacaoService autorizacaoService) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                // Permite acesso público a produtos
                .requestMatchers(HttpMethod.GET, "/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                // Libera o CSS para as telas de login e cadastro aparecerem com o estilo do projeto
                .requestMatchers(HttpMethod.GET, "/css/**").permitAll()
                .requestMatchers("/formularioMatricula", "/cadastrarMatricula").hasAnyRole("ADMIN", "ALUNO")
                .requestMatchers("/formularioEmprestimo", "/cadastrarEmprestimo", "/renovarEmprestimo",
                        "/formularioReserva", "/cadastrarReserva", "/cancelarReserva").hasAnyRole("ADMIN", "BIBLIOTECARIO", "ALUNO")
                .requestMatchers("/marcarProvaEstudada", "/desmarcarProvaEstudada").hasRole("ALUNO")
                .requestMatchers(crud("Prova", "OfertaDisciplina")).hasAnyRole("ADMIN", "PROFESSOR")
                .requestMatchers(crud("DisciplinaLivro")).hasAnyRole("ADMIN", "PROFESSOR", "BIBLIOTECARIO")
                .requestMatchers(crud("Livro", "Exemplar", "Emprestimo", "Reserva")).hasAnyRole("ADMIN", "BIBLIOTECARIO")
                .requestMatchers("/devolverEmprestimo").hasAnyRole("ADMIN", "BIBLIOTECARIO")
                .requestMatchers(crud("Curso", "Disciplina", "Professor", "Aluno", "Matricula")).hasRole("ADMIN")
                .requestMatchers("/listaAlunos", "/detalhesAluno").hasAnyRole("ADMIN", "PROFESSOR", "BIBLIOTECARIO")
                .anyRequest().authenticated()// Exige autenticação para qualquer outra requisição
            )

            .formLogin(form -> form.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/menu", true)
                        .permitAll()) // Habilita o formulário de login padrão
            .rememberMe(lembrar -> lembrar
                        .tokenRepository(tokenRepository)
                        .userDetailsService(autorizacaoService)
                        .rememberMeParameter("lembrar")
                        .rememberMeCookieName("lembrar-me")
                        .tokenValiditySeconds(VALIDADE_LEMBRAR_SEGUNDOS))
            .logout(logout -> logout
                        .deleteCookies("JSESSIONID", "lembrar-me")
                        .invalidateHttpSession(true))
            .build();
    }

    private static String[] crud(String... entidades) {
        List<String> rotas = new ArrayList<>();
        for (String entidade : entidades) {
            for (String acao : List.of("formulario", "cadastrar", "editar", "atualizar", "excluir")) {
                rotas.add("/" + acao + entidade);
            }
        }
        return rotas.toArray(String[]::new);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        new JdbcTemplate(dataSource).execute("CREATE TABLE IF NOT EXISTS persistent_logins ("
                + "username VARCHAR(64) NOT NULL, "
                + "series VARCHAR(64) PRIMARY KEY, "
                + "token VARCHAR(64) NOT NULL, "
                + "last_used TIMESTAMP NOT NULL)");
        JdbcTokenRepositoryImpl repositorio = new JdbcTokenRepositoryImpl();
        repositorio.setDataSource(dataSource);
        return repositorio;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Define o codificador de senhas que será usado na aplicação
        return new BCryptPasswordEncoder();
    }
}
