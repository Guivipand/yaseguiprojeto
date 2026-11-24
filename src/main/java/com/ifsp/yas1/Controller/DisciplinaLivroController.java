package com.ifsp.yas1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ifsp.yas1.Model.Disciplina;
import com.ifsp.yas1.Model.DisciplinaLivro;
import com.ifsp.yas1.Repository.DisciplinaLivroRepository;
import com.ifsp.yas1.Repository.DisciplinaRepository;
import com.ifsp.yas1.Repository.LivroRepository;
import com.ifsp.yas1.Repository.OfertaDisciplinaRepository;
import com.ifsp.yas1.Service.UsuarioLogado;

@Controller
public class DisciplinaLivroController {

    @Autowired
    private DisciplinaLivroRepository disciplinaLivroRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private OfertaDisciplinaRepository ofertaDisciplinaRepository;

    @Autowired
    private UsuarioLogado usuarioLogado;

    @GetMapping("/formularioDisciplinaLivro")
    public String formularioDisciplinaLivro(Model model) {
        model.addAttribute("disciplinaLivro", new DisciplinaLivro());
        adicionarOpcoes(model);
        return "formulario/formularioDisciplinaLivro";
    }

    @PostMapping("/cadastrarDisciplinaLivro")
    public String cadastrarDisciplinaLivro(@ModelAttribute DisciplinaLivro disciplinaLivro, Model model) {
        exigirDisciplinaPropria(disciplinaLivro);
        if (!disciplinaLivro.validar()) {
            model.addAttribute("erroDisciplinaLivro", true);
            adicionarOpcoes(model);
            return "formulario/formularioDisciplinaLivro";
        }
        disciplinaLivroRepository.save(disciplinaLivro);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaDisciplinaLivros")
    public String listaDisciplinaLivros(@RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") int pagina, Model model) {
        Page<DisciplinaLivro> resultado = disciplinaLivroRepository.buscar(q.trim(), PageRequest.of(Math.max(pagina, 0), 10, Sort.by("id")));
        List<DisciplinaLivro> disciplinaLivros = resultado.getContent();
        model.addAttribute("pagina", resultado);
        model.addAttribute("q", q);
        model.addAttribute("disciplinaLivros", disciplinaLivros);
        model.addAttribute("idsDisciplinasProfessor", idsDisciplinasProfessor());
        return "lista/listaDisciplinaLivros";
    }

    @GetMapping("/detalhesDisciplinaLivro")
    public String detalhesDisciplinaLivro(@RequestParam int id, Model model) {
        DisciplinaLivro disciplinaLivro = disciplinaLivroRepository.findById(id).orElseThrow();
        model.addAttribute("disciplinaLivro", disciplinaLivro);
        model.addAttribute("idsDisciplinasProfessor", idsDisciplinasProfessor());
        return "detalhes/detalhesDisciplinaLivro";
    }

    @GetMapping("/editarDisciplinaLivro")
    public String editarDisciplinaLivro(@RequestParam int id, Model model) {
        DisciplinaLivro disciplinaLivro = disciplinaLivroRepository.findById(id).orElseThrow();
        exigirDisciplinaPropria(disciplinaLivro);
        model.addAttribute("disciplinaLivro", disciplinaLivro);
        adicionarOpcoes(model);
        return "editar/editarDisciplinaLivro";
    }

    @PostMapping("/atualizarDisciplinaLivro")
    public String atualizarDisciplinaLivro(@ModelAttribute DisciplinaLivro disciplinaLivro, Model model) {
        exigirDisciplinaPropria(disciplinaLivroRepository.findById(disciplinaLivro.getId()).orElseThrow());
        exigirDisciplinaPropria(disciplinaLivro);
        if (!disciplinaLivro.validar()) {
            model.addAttribute("erroDisciplinaLivro", true);
            adicionarOpcoes(model);
            return "editar/editarDisciplinaLivro";
        }
        disciplinaLivroRepository.save(disciplinaLivro);
        return "redirect:/listaDisciplinaLivros";
    }

    @PostMapping("/excluirDisciplinaLivro")
    public String excluirDisciplinaLivro(@RequestParam int id) {
        DisciplinaLivro disciplinaLivro = disciplinaLivroRepository.findById(id).orElseThrow();
        exigirDisciplinaPropria(disciplinaLivro);
        disciplinaLivroRepository.delete(disciplinaLivro);
        return "redirect:/listaDisciplinaLivros";
    }

    private List<Integer> idsDisciplinasProfessor() {
        return usuarioLogado.ehProfessor()
                ? ofertaDisciplinaRepository.disciplinasDoProfessor(usuarioLogado.idProfessor()).stream().map(Disciplina::getId).toList()
                : null;
    }

    private void exigirDisciplinaPropria(DisciplinaLivro disciplinaLivro) {
        List<Integer> permitidas = idsDisciplinasProfessor();
        Disciplina disciplina = disciplinaLivro.getDisciplina();
        if (permitidas != null && disciplina != null && !permitidas.contains(disciplina.getId())) {
            throw new AccessDeniedException("Disciplina sem oferta do professor");
        }
    }

    private void adicionarOpcoes(Model model) {
        model.addAttribute("disciplinas", usuarioLogado.ehProfessor()
                ? ofertaDisciplinaRepository.disciplinasDoProfessor(usuarioLogado.idProfessor())
                : disciplinaRepository.findAll());
        model.addAttribute("livros", livroRepository.findAll());
    }
}