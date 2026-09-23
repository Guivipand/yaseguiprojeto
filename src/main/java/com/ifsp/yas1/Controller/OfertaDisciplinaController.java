package com.ifsp.yas1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ifsp.yas1.Model.OfertaDisciplina;
import com.ifsp.yas1.Repository.DisciplinaRepository;
import com.ifsp.yas1.Repository.OfertaDisciplinaRepository;
import com.ifsp.yas1.Repository.ProfessorRepository;

@Controller
public class OfertaDisciplinaController {

    @Autowired
    private OfertaDisciplinaRepository ofertaDisciplinaRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @GetMapping("/formularioOfertaDisciplina")
    public String formularioOfertaDisciplina(Model model) {
        model.addAttribute("ofertaDisciplina", new OfertaDisciplina());
        model.addAttribute("disciplinas", disciplinaRepository.findAll());
        model.addAttribute("professores", professorRepository.findAll());
        return "formulario/formularioOfertaDisciplina";
    }

    @PostMapping("/cadastrarOfertaDisciplina")
    public String saveOfertaDisciplinas(@ModelAttribute OfertaDisciplina ofertaDisciplina) {
        if (!ofertaDisciplina.validar()) {
            return "formulario/formularioOfertaDisciplina";
        }
        ofertaDisciplinaRepository.save(ofertaDisciplina);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaOfertaDisciplinas")
    public String lOfertaDisciplina(Model model) {
        List<OfertaDisciplina> listaOfertaDisciplinas = ofertaDisciplinaRepository.findAll();
        model.addAttribute("ofertasDisciplina", listaOfertaDisciplinas);
        return "lista/listaOfertaDisciplina";
    }

    @GetMapping("/editarOfertaDisciplina")
    public String editarOfertaDisciplina(@RequestParam int id, Model model) {
        OfertaDisciplina ofertaDisciplina = ofertaDisciplinaRepository.findById(id).orElseThrow();
        model.addAttribute("ofertaDisciplina", ofertaDisciplina);
        model.addAttribute("disciplinas", disciplinaRepository.findAll());
        model.addAttribute("professores", professorRepository.findAll());
        return "editar/editarOfertaDisciplina";
    }

    @PostMapping("/atualizarOfertaDisciplina")
    public String atualizarOfertaDisciplina(@ModelAttribute OfertaDisciplina ofertaDisciplina) {
        if (!ofertaDisciplina.validar()) {
            return "editar/editarOfertaDisciplina";
        }
        ofertaDisciplinaRepository.save(ofertaDisciplina);
        return "redirect:/listaOfertaDisciplinas";
    }

    @GetMapping("/excluirOfertaDisciplina")
    public String excluirOfertaDisciplina(@RequestParam int id) {
        OfertaDisciplina ofertaDisciplina = ofertaDisciplinaRepository.findById(id).orElseThrow();
        ofertaDisciplinaRepository.delete(ofertaDisciplina);
        return "redirect:/listaOfertaDisciplinas";
    }
}
