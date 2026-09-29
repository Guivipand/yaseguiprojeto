package com.ifsp.yas1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ifsp.yas1.Model.DisciplinaLivro;
import com.ifsp.yas1.Repository.DisciplinaLivroRepository;
import com.ifsp.yas1.Repository.DisciplinaRepository;
import com.ifsp.yas1.Repository.LivroRepository;

@Controller
public class DisciplinaLivroController {

    @Autowired
    private DisciplinaLivroRepository disciplinaLivroRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private LivroRepository livroRepository;

    @GetMapping("/formularioDisciplinaLivro")
    public String formularioDisciplinaLivro(Model model) {
        model.addAttribute("disciplinaLivro", new DisciplinaLivro());
        adicionarOpcoes(model);
        return "formulario/formularioDisciplinaLivro";
    }

    @PostMapping("/cadastrarDisciplinaLivro")
    public String cadastrarDisciplinaLivro(@ModelAttribute DisciplinaLivro disciplinaLivro, Model model) {
        if (!disciplinaLivro.validar()) {
            model.addAttribute("erroDisciplinaLivro", true);
            adicionarOpcoes(model);
            return "formulario/formularioDisciplinaLivro";
        }
        disciplinaLivroRepository.save(disciplinaLivro);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaDisciplinaLivros")
    public String listaDisciplinaLivros(Model model) {
        List<DisciplinaLivro> disciplinaLivros = disciplinaLivroRepository.findAll();
        model.addAttribute("disciplinaLivros", disciplinaLivros);
        return "lista/listaDisciplinaLivros";
    }

    @GetMapping("/detalhesDisciplinaLivro")
    public String detalhesDisciplinaLivro(@RequestParam int id, Model model) {
        DisciplinaLivro disciplinaLivro = disciplinaLivroRepository.findById(id).orElseThrow();
        model.addAttribute("disciplinaLivro", disciplinaLivro);
        return "detalhes/detalhesDisciplinaLivro";
    }

    @GetMapping("/editarDisciplinaLivro")
    public String editarDisciplinaLivro(@RequestParam int id, Model model) {
        DisciplinaLivro disciplinaLivro = disciplinaLivroRepository.findById(id).orElseThrow();
        model.addAttribute("disciplinaLivro", disciplinaLivro);
        adicionarOpcoes(model);
        return "editar/editarDisciplinaLivro";
    }

    @PostMapping("/atualizarDisciplinaLivro")
    public String atualizarDisciplinaLivro(@ModelAttribute DisciplinaLivro disciplinaLivro, Model model) {
        if (!disciplinaLivro.validar()) {
            model.addAttribute("erroDisciplinaLivro", true);
            adicionarOpcoes(model);
            return "editar/editarDisciplinaLivro";
        }
        disciplinaLivroRepository.save(disciplinaLivro);
        return "redirect:/listaDisciplinaLivros";
    }

    @GetMapping("/excluirDisciplinaLivro")
    public String excluirDisciplinaLivro(@RequestParam int id) {
        DisciplinaLivro disciplinaLivro = disciplinaLivroRepository.findById(id).orElseThrow();
        disciplinaLivroRepository.delete(disciplinaLivro);
        return "redirect:/listaDisciplinaLivros";
    }

    private void adicionarOpcoes(Model model) {
        model.addAttribute("disciplinas", disciplinaRepository.findAll());
        model.addAttribute("livros", livroRepository.findAll());
    }
}