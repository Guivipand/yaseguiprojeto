package com.ifsp.yas1.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                // Permite acesso público a produtos
                .requestMatchers(HttpMethod.GET, "/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                // Libera o CSS para as telas de login e cadastro aparecerem com o estilo do projeto
                .requestMatchers(HttpMethod.GET, "/css/**").permitAll()
                .anyRequest().authenticated()// Exige autenticação para qualquer outra requisição
            )

            .formLogin(form -> form.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/menu", true)
                        .permitAll()) // Habilita o formulário de login padrão
            .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Define o codificador de senhas que será usado na aplicação
        return new BCryptPasswordEncoder();
    }
}
