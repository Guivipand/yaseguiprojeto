package com.ifsp.yas1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ifsp.yas1.Model.Matricula;
import com.ifsp.yas1.Repository.AlunoRepository;
import com.ifsp.yas1.Repository.CursoRepository;
import com.ifsp.yas1.Repository.MatriculaRepository;
import com.ifsp.yas1.Repository.LivroRepository;

@Controller
public class MatriculaController {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private LivroRepository LivroRepository;

    @GetMapping("/formularioMatricula")
    public String formularioMatricula(Model model) {
        model.addAttribute("matricula", new Matricula());
        model.addAttribute("alunos", alunoRepository.findAll());
        model.addAttribute("cursos", cursoRepository.findAll());
        model.addAttribute("ofertasDisciplina", LivroRepository.findAll());
        return "formulario/formularioMatricula";
    }

    @PostMapping("/cadastrarMatricula")
    public String saveMatriculas(@ModelAttribute Matricula matricula) {
        if (!matricula.validar()) {
            return "formulario/formularioMatricula";
        }
        matriculaRepository.save(matricula);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaMatriculas")
    public String lMatricula(Model model) {
        List<Matricula> listaMatriculas = matriculaRepository.findAll();
        model.addAttribute("matriculas", listaMatriculas);
        return "lista/listaMatricula";
    }

    @GetMapping("/editarMatricula")
    public String editarMatricula(@RequestParam int id, Model model) {
        Matricula matricula = matriculaRepository.findById(id).orElseThrow();
        model.addAttribute("matricula", matricula);
        model.addAttribute("alunos", alunoRepository.findAll());
        model.addAttribute("cursos", cursoRepository.findAll());
        model.addAttribute("ofertasDisciplina", LivroRepository.findAll());
        return "editar/editarMatricula";
    }

    @PostMapping("/atualizarMatricula")
    public String atualizarMatricula(@ModelAttribute Matricula matricula) {
        if (!matricula.validar()) {
            return "editar/editarMatricula";
        }
        matriculaRepository.save(matricula);
        return "redirect:/listaMatriculas";
    }

    @GetMapping("/excluirMatricula")
    public String excluirMatricula(@RequestParam int id) {
        Matricula matricula = matriculaRepository.findById(id).orElseThrow();
        matriculaRepository.delete(matricula);
        return "redirect:/listaMatriculas";
    }
}
