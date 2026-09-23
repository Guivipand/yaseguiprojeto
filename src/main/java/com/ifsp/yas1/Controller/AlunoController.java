package com.ifsp.yas1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ifsp.yas1.Model.Aluno;
import com.ifsp.yas1.Repository.AlunoRepository;
import com.ifsp.yas1.Repository.CursoRepository;

@Controller
public class AlunoController {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping("/formularioAluno")
    public String formularioAluno(Model model) {
        model.addAttribute("aluno", new Aluno());
        model.addAttribute("cursos", cursoRepository.findAll());
        return "formulario/formularioAluno";
    }

    @PostMapping("/cadastrarAluno")
    public String saveAlunos(@ModelAttribute Aluno aluno) {

        if (!aluno.validar()) {
        return "formulario/formularioAluno";
    }
        alunoRepository.save(aluno);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaAlunos")
    public String lAluno(Model model) {
        List<Aluno> listaAluno = alunoRepository.findAll();
        model.addAttribute("alunos", listaAluno);
        return "lista/listaAluno";
    }

    @GetMapping("/detalhesAluno")
    public String detalhesAluno(@RequestParam int id, Model model) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow();
        model.addAttribute("aluno", aluno);
        return "detalhes/detalhesAluno";
    }

    @GetMapping("/editarAluno")
    public String editarAluno(@RequestParam int id, Model model) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow();
        model.addAttribute("aluno", aluno);
        model.addAttribute("cursos", cursoRepository.findAll());
        return "editar/editarAluno";
    }

    @PostMapping("/atualizarAluno")
    public String atualizarAluno(@ModelAttribute Aluno aluno) {
        if (!aluno.validar()) {
            return "editar/editarAluno";
        }
        alunoRepository.save(aluno);
        return "redirect:/listaAlunos";
    }

    @GetMapping("/excluirAluno")
    public String excluirAluno(@RequestParam int id) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow();
        alunoRepository.delete(aluno);
        return "redirect:/listaAlunos";
    }
}
