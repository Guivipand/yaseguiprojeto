package com.ifsp.yas1.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ifsp.yas1.Model.Pessoa;
import com.ifsp.yas1.Repository.AlunoRepository;
import com.ifsp.yas1.Repository.BibliotecarioRepository;
import com.ifsp.yas1.Repository.PessoaRepository;
import com.ifsp.yas1.Repository.ProfessorRepository;
import com.ifsp.yas1.Service.PessoaRole;
import com.ifsp.yas1.Service.UploadService;

@Controller
public class PessoaController {

    @Autowired
    private PessoaRepository repositorio;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private BibliotecarioRepository bibliotecarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PersistentTokenRepository tokenRepository;

    @Autowired
    private UploadService uploadService;

    @GetMapping("/login")
    public String login() {
        return "sessao/login.html";
    }

    @GetMapping("/listaUsuarios")
    public String listaUsuarios(@RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") int pagina, Model model) {
        PageRequest paginacao = PageRequest.of(Math.max(pagina, 0), 10, Sort.by("username"));
        Page<Pessoa> resultado = repositorio.buscarUsuarios(PessoaRole.ADMIN, q.trim(), paginacao);
        model.addAttribute("usuarios", resultado.getContent());
        model.addAttribute("pagina", resultado);
        model.addAttribute("q", q);
        return "lista/listaUsuarios";
    }

    @GetMapping("/formularioUsuario")
    public String formularioUsuario(Model model) {
        adicionarOpcoes(model);
        return "formulario/formularioUsuario";
    }

    @PostMapping("/cadastrarUsuario")
    public String cadastrarUsuario(@RequestParam String username, @RequestParam String password, @RequestParam PessoaRole role,
            @RequestParam(required = false) Integer aluno, @RequestParam(required = false) Integer professor,
            @RequestParam(required = false) Integer bibliotecario,
            @RequestParam(required = false) MultipartFile foto, Model model) throws IOException {
        String erro = null;
        username = username.trim();
        Pessoa pessoa = switch (role) {
            case ALUNO -> aluno == null ? null : alunoRepository.findById(aluno).orElse(null);
            case PROFESSOR -> professor == null ? null : professorRepository.findById(professor).orElse(null);
            case BIBLIOTECARIO -> bibliotecario == null ? null : bibliotecarioRepository.findById(bibliotecario).orElse(null);
            case ADMIN -> null;
        };
        if (username.isEmpty() || password.isBlank()) {
            erro = "Informe o login e a senha.";
        } else if (repositorio.findByUsername(username) != null) {
            erro = "Já existe um usuário com esse nome!";
        } else if (pessoa == null) {
            erro = "Selecione a pessoa ligada a este usuário.";
        } else if (pessoa.getUsername() != null) {
            erro = "Essa pessoa já tem um usuário cadastrado.";
        }

        if (erro == null && temArquivo(foto)) {
            try {
                pessoa.setFoto(uploadService.salvar(foto, "fotos"));
            } catch (IllegalArgumentException e) {
                erro = e.getMessage();
            }
        }

        if (erro != null) {
            model.addAttribute("erroUsuario", erro);
            model.addAttribute("username", username);
            model.addAttribute("roleSelecionado", role);
            model.addAttribute("alunoSelecionado", aluno);
            model.addAttribute("professorSelecionado", professor);
            model.addAttribute("bibliotecarioSelecionado", bibliotecario);
            adicionarOpcoes(model);
            return "formulario/formularioUsuario";
        }
        pessoa.setUsername(username);
        pessoa.setPassword(passwordEncoder.encode(password));
        pessoa.setRole(role);
        repositorio.save(pessoa);
        return "redirect:/listaUsuarios";
    }

    @GetMapping("/editarUsuario")
    public String editarUsuario(@RequestParam int id, Model model) {
        model.addAttribute("usuario", usuarioEditavel(id));
        return "editar/editarUsuario";
    }

    @PostMapping("/atualizarUsuario")
    public String atualizarUsuario(@RequestParam int id, @RequestParam String username,
            @RequestParam(defaultValue = "") String password,
            @RequestParam(required = false) MultipartFile foto, Model model) throws IOException {
        Pessoa usuario = usuarioEditavel(id);
        String anterior = usuario.getUsername();
        username = username.trim();
        Pessoa existente = repositorio.findByUsername(username);
        String erro = username.isEmpty() ? "Informe o login."
                : existente != null && existente.getId() != id ? "Já existe um usuário com esse nome!" : null;
        if (erro == null && temArquivo(foto)) {
            try {
                usuario.setFoto(uploadService.salvar(foto, "fotos"));
            } catch (IllegalArgumentException e) {
                erro = e.getMessage();
            }
        }
        if (erro != null) {
            model.addAttribute("erroUsuario", erro);
            model.addAttribute("usuario", usuario);
            return "editar/editarUsuario";
        }
        usuario.setUsername(username);
        if (!password.isBlank()) {
            usuario.setPassword(passwordEncoder.encode(password));
        }
        repositorio.save(usuario);
        if (!username.equals(anterior) || !password.isBlank()) {
            tokenRepository.removeUserTokens(anterior);
        }
        return "redirect:/listaUsuarios";
    }

    @PostMapping("/excluirUsuario")
    public String excluirUsuario(@RequestParam int id) {
        Pessoa usuario = usuarioEditavel(id);
        tokenRepository.removeUserTokens(usuario.getUsername());
        usuario.setUsername(null);
        usuario.setPassword(null);
        usuario.setRole(null);
        usuario.setFoto(null);
        repositorio.save(usuario);
        return "redirect:/listaUsuarios";
    }

    private static boolean temArquivo(MultipartFile arquivo) {
        return arquivo != null && !arquivo.isEmpty();
    }

    private Pessoa usuarioEditavel(int id) {
        Pessoa usuario = repositorio.findById(id).orElseThrow();
        if (usuario.getUsername() == null || usuario.getRole() == PessoaRole.ADMIN) {
            throw new AccessDeniedException("Usuário não editável");
        }
        return usuario;
    }

    private void adicionarOpcoes(Model model) {
        model.addAttribute("alunosSemUsuario", alunoRepository.findByUsernameIsNullOrderByNomeAsc());
        model.addAttribute("professoresSemUsuario", professorRepository.findByUsernameIsNullOrderByNomeAsc());
        model.addAttribute("bibliotecariosSemUsuario", bibliotecarioRepository.findByUsernameIsNullOrderByNomeAsc());
    }
}
