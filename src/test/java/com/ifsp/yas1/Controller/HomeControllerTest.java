package com.ifsp.yas1.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ifsp.yas1.Model.Pessoa;
import com.ifsp.yas1.Service.AutorizacaoService;
import com.ifsp.yas1.Service.PessoaRole;
import com.ifsp.yas1.Service.WebSecurityConfig;

@WebMvcTest(HomeController.class)
@Import({WebSecurityConfig.class, HomeControllerTest.Seguranca.class})
class HomeControllerTest {

    @TestConfiguration
    @EnableWebSecurity
    static class Seguranca {
    }

    private static final Map<PessoaRole, Set<String>> VISIVEIS = Map.of(
            PessoaRole.ADMIN, Set.of("/listaUsuarios", "/listaAlunos", "/listaProfessores", "/listaBibliotecarios",
                    "/listaCursos", "/listaDisciplinas", "/listaOfertaDisciplinas", "/listaMatriculas", "/listaProvas",
                    "/listaLivros", "/listaExemplares", "/listaDisciplinaLivros", "/listaEmprestimos", "/listaReservas"),
            PessoaRole.PROFESSOR, Set.of("/listaAlunos", "/listaProfessores", "/listaCursos", "/listaDisciplinas",
                    "/listaOfertaDisciplinas", "/listaMatriculas", "/listaProvas", "/listaLivros", "/listaDisciplinaLivros"),
            PessoaRole.ALUNO, Set.of("/listaProfessores", "/listaCursos", "/listaDisciplinas", "/listaOfertaDisciplinas",
                    "/listaMatriculas", "/listaProvas", "/listaLivros", "/listaDisciplinaLivros", "/listaEmprestimos",
                    "/listaReservas"),
            PessoaRole.BIBLIOTECARIO, Set.of("/listaAlunos", "/listaLivros", "/listaExemplares", "/listaDisciplinaLivros",
                    "/listaEmprestimos", "/listaReservas"));

    private static final Map<PessoaRole, String> PAPEIS = Map.of(
            PessoaRole.ADMIN, "Administrador",
            PessoaRole.PROFESSOR, "Professor",
            PessoaRole.ALUNO, "Aluno",
            PessoaRole.BIBLIOTECARIO, "Bibliotecário");

    private static final Pattern CARD = Pattern.compile("class=\"modulo\" href=\"([^\"]+)\"");

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AutorizacaoService autorizacaoService;

    @MockitoBean
    private PersistentTokenRepository tokenRepository;

    @Test
    void cadaPerfilVeApenasSeusModulos() throws Exception {
        for (PessoaRole role : PessoaRole.values()) {
            Pessoa pessoa = new Pessoa();
            pessoa.setUsername("usuario");
            pessoa.setNome("  Maria da Silva");
            pessoa.setRole(role);

            String html = mvc.perform(get("/").with(user(pessoa))).andReturn().getResponse().getContentAsString();

            Set<String> cards = new TreeSet<>();
            Matcher m = CARD.matcher(html);
            while (m.find()) {
                cards.add(m.group(1));
            }
            assertEquals(new TreeSet<>(VISIVEIS.get(role)), cards, role.name());
            assertTrue(html.contains("Olá, <span>Maria</span>"), role.name());
            assertTrue(html.contains("<span class=\"saudacao-papel\">" + PAPEIS.get(role) + "</span>"), role.name());
            assertTrue(html.matches("(?s).*class=\"saudacao-data\">\\p{L}+-?\\p{L}*, \\d{1,2} de \\p{L}+</span>.*"), role.name());
        }
    }
}
