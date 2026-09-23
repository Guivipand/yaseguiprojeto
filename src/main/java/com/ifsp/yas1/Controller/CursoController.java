package com.ifsp.yas1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ifsp.yas1.Model.Curso;
import com.ifsp.yas1.Repository.CursoRepository;

@Controller
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping("/formularioCurso")
    public String formularioCurso(Model model) {
        model.addAttribute("curso", new Curso());
        return "formulario/formularioCurso";
    }

    @PostMapping("/cadastrarCurso")
    public String saveCursos(@ModelAttribute Curso curso) {
        if (!curso.validar()) {
            return "formulario/formularioCurso";
        }
        cursoRepository.save(curso);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaCursos")
    public String lCurso(Model model) {
        List<Curso> listaCursos = cursoRepository.findAll();
        model.addAttribute("cursos", listaCursos);
        return "lista/listaCursos";
    }

    @GetMapping("/editarCurso")
    public String editarCurso(@RequestParam int id, Model model) {
        Curso curso = cursoRepository.findById(id).orElseThrow();
        model.addAttribute("curso", curso);
        return "editar/editarCurso";
    }

    @PostMapping("/atualizarCurso")
    public String atualizarCurso(@ModelAttribute Curso curso) {
        if (!curso.validar()) {
            return "editar/editarCurso";
        }
        cursoRepository.save(curso);
        return "redirect:/listaCursos";
    }

    @GetMapping("/excluirCurso")
    public String excluirCurso(@RequestParam int id) {
        Curso curso = cursoRepository.findById(id).orElseThrow();
        cursoRepository.delete(curso);
        return "redirect:/listaCursos";
    }
}

