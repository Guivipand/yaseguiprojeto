package com.ifsp.yas1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ifsp.yas1.Model.Professor;
import com.ifsp.yas1.Repository.ProfessorRepository;

@Controller
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;

    @GetMapping("/formularioProfessor")
    public String formularioProfessor(Model model) {
        model.addAttribute("professor", new Professor());
        return "formulario/formularioProfessor";
    }

    @PostMapping("/cadastrarProfessor")
    public String saveProfessor(@ModelAttribute Professor professor, Model model) {
        if (!professor.validar()) {
            model.addAttribute("erroValidacao", true);
            return "formulario/formularioProfessor";
        }
        professorRepository.save(professor);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaProfessores")
    public String lProfessor(Model model) {
        List<Professor> listaProfessores = professorRepository.findAll();
        model.addAttribute("professores", listaProfessores);
        return "lista/listaProfessores";
    }

    @GetMapping("/detalhesProfessor")
    public String detalhesProfessor(@RequestParam int id, Model model) {
        Professor professor = professorRepository.findById(id).orElseThrow();
        model.addAttribute("professor", professor);
        return "detalhes/detalhesProfessor";
    }

    @GetMapping("/editarProfessor")
    public String editarProfessor(@RequestParam int id, Model model) {
        Professor professor = professorRepository.findById(id).orElseThrow();
        model.addAttribute("professor", professor);
        return "editar/editarProfessor";
    }

    @PostMapping("/atualizarProfessor")
    public String atualizarProfessor(@ModelAttribute Professor professor, Model model) {
        if (!professor.validar()) {
            model.addAttribute("erroValidacao", true);
            return "editar/editarProfessor";
        }
        professorRepository.save(professor);
        return "redirect:/listaProfessores";
    }

    @GetMapping("/excluirProfessor")
    public String excluirProfessor(@RequestParam int id, RedirectAttributes redirect) {
        Professor professor = professorRepository.findById(id).orElseThrow();
        try {
            professorRepository.delete(professor);
        } catch (DataIntegrityViolationException e) {
            redirect.addFlashAttribute("erroExclusao", true);
        }
        return "redirect:/listaProfessores";
    }

}
