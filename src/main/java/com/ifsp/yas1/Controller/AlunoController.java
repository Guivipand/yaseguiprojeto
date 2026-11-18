package com.ifsp.yas1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String saveAlunos(@ModelAttribute Aluno aluno, Model model) {
        if (!aluno.validar()) {
            model.addAttribute("erroValidacao", !aluno.validarDados());
            model.addAttribute("erroIdade", !aluno.validarIdade());
            model.addAttribute("cursos", cursoRepository.findAll());
            return "formulario/formularioAluno";
        }
        alunoRepository.save(aluno);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaAlunos")
    public String lAluno(@RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") int pagina, Model model) {
        Page<Aluno> resultado = alunoRepository.buscar(q.trim(), PageRequest.of(Math.max(pagina, 0), 10, Sort.by("id")));
        List<Aluno> listaAluno = resultado.getContent();
        model.addAttribute("pagina", resultado);
        model.addAttribute("q", q);
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
    public String atualizarAluno(@ModelAttribute Aluno aluno, Model model) {
        Aluno existente = alunoRepository.findById(aluno.getId()).orElseThrow();
        aluno.setUsername(existente.getUsername());
        aluno.setPassword(existente.getPassword());
        aluno.setRole(existente.getRole());
        if (!aluno.validar()) {
            model.addAttribute("erroValidacao", !aluno.validarDados());
            model.addAttribute("erroIdade", !aluno.validarIdade());
            model.addAttribute("cursos", cursoRepository.findAll());
            return "editar/editarAluno";
        }
        alunoRepository.save(aluno);
        return "redirect:/listaAlunos";
    }

    @GetMapping("/excluirAluno")
    public String excluirAluno(@RequestParam int id, RedirectAttributes redirect) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow();
        try {
            alunoRepository.delete(aluno);
        } catch (DataIntegrityViolationException e) {
            redirect.addFlashAttribute("erroExclusao", true);
        }
        return "redirect:/listaAlunos";
    }
}
