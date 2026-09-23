package com.ifsp.yas1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ifsp.yas1.Model.Disciplina;
import com.ifsp.yas1.Repository.CursoRepository;
import com.ifsp.yas1.Repository.DisciplinaRepository;

@Controller
public class DisciplinaController {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping("/formularioDisciplina")
    public String formularioDisciplina(Model model) {
        model.addAttribute("disciplina", new Disciplina());
        model.addAttribute("cursos", cursoRepository.findAll());
        return "formulario/formularioDisciplina";
    }

    @PostMapping("/cadastrarDisciplina")
    public String saveDisciplinas(@ModelAttribute Disciplina disciplina) {
        if (!disciplina.validar()) {
            return "formulario/formularioDisciplina";
        }
        disciplinaRepository.save(disciplina);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaDisciplinas")
    public String lDisciplina(Model model) {
        List<Disciplina> listaDisciplinas = disciplinaRepository.findAll();
        model.addAttribute("disciplinas", listaDisciplinas);
        return "lista/listaDisciplina";
    }

    @GetMapping("/editarDisciplina")
    public String editarDisciplina(@RequestParam int id, Model model) {
        Disciplina disciplina = disciplinaRepository.findById(id).orElseThrow();
        model.addAttribute("disciplina", disciplina);
        model.addAttribute("cursos", cursoRepository.findAll());
        return "editar/editarDisciplina";
    }

    @PostMapping("/atualizarDisciplina")
    public String atualizarDisciplina(@ModelAttribute Disciplina disciplina) {
        if (!disciplina.validar()) {
            return "editar/editarDisciplina";
        }
        disciplinaRepository.save(disciplina);
        return "redirect:/listaDisciplinas";
    }

    @GetMapping("/excluirDisciplina")
    public String excluirDisciplina(@RequestParam int id) {
        Disciplina disciplina = disciplinaRepository.findById(id).orElseThrow();
        disciplinaRepository.delete(disciplina);
        return "redirect:/listaDisciplinas";
    }
}
