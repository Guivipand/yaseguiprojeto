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

import com.ifsp.yas1.Model.OfertaDisciplina;
import com.ifsp.yas1.Model.Professor;
import com.ifsp.yas1.Repository.DisciplinaRepository;
import com.ifsp.yas1.Repository.OfertaDisciplinaRepository;
import com.ifsp.yas1.Repository.ProfessorRepository;
import com.ifsp.yas1.Service.UsuarioLogado;

@Controller
public class OfertaDisciplinaController {

    @Autowired
    private OfertaDisciplinaRepository ofertaDisciplinaRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private UsuarioLogado usuarioLogado;

    @GetMapping("/formularioOfertaDisciplina")
    public String formularioOfertaDisciplina(Model model) {
        model.addAttribute("ofertaDisciplina", new OfertaDisciplina());
        adicionarOpcoes(model);
        return "formulario/formularioOfertaDisciplina";
    }

    @PostMapping("/cadastrarOfertaDisciplina")
    public String saveOfertaDisciplinas(@ModelAttribute OfertaDisciplina ofertaDisciplina, Model model) {
        if (!fixarProfessor(ofertaDisciplina, model) || !ofertaDisciplina.validar()) {
            model.addAttribute("erroValidacao", model.getAttribute("erroProfessor") == null);
            adicionarOpcoes(model);
            return "formulario/formularioOfertaDisciplina";
        }
        ofertaDisciplinaRepository.save(ofertaDisciplina);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaOfertaDisciplinas")
    public String lOfertaDisciplina(@RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") int pagina, Model model) {
        Page<OfertaDisciplina> resultado = ofertaDisciplinaRepository.buscar(q.trim(), PageRequest.of(Math.max(pagina, 0), 10, Sort.by("id")));
        List<OfertaDisciplina> listaOfertaDisciplinas = resultado.getContent();
        model.addAttribute("pagina", resultado);
        model.addAttribute("q", q);
        model.addAttribute("ofertasDisciplina", listaOfertaDisciplinas);
        model.addAttribute("idProfessorLogado", usuarioLogado.ehProfessor() ? usuarioLogado.idProfessor() : null);
        return "lista/listaOfertaDisciplina";
    }

    @GetMapping("/editarOfertaDisciplina")
    public String editarOfertaDisciplina(@RequestParam int id, Model model) {
        OfertaDisciplina ofertaDisciplina = ofertaDisciplinaRepository.findById(id).orElseThrow();
        usuarioLogado.exigirProfessorDono(ofertaDisciplina.getProfessor());
        model.addAttribute("ofertaDisciplina", ofertaDisciplina);
        adicionarOpcoes(model);
        return "editar/editarOfertaDisciplina";
    }

    @PostMapping("/atualizarOfertaDisciplina")
    public String atualizarOfertaDisciplina(@ModelAttribute OfertaDisciplina ofertaDisciplina, Model model) {
        usuarioLogado.exigirProfessorDono(ofertaDisciplinaRepository.findById(ofertaDisciplina.getId()).orElseThrow().getProfessor());
        if (!fixarProfessor(ofertaDisciplina, model) || !ofertaDisciplina.validar()) {
            model.addAttribute("erroValidacao", model.getAttribute("erroProfessor") == null);
            adicionarOpcoes(model);
            return "editar/editarOfertaDisciplina";
        }
        ofertaDisciplinaRepository.save(ofertaDisciplina);
        return "redirect:/listaOfertaDisciplinas";
    }

    @PostMapping("/excluirOfertaDisciplina")
    public String excluirOfertaDisciplina(@RequestParam int id, RedirectAttributes redirect) {
        OfertaDisciplina ofertaDisciplina = ofertaDisciplinaRepository.findById(id).orElseThrow();
        usuarioLogado.exigirProfessorDono(ofertaDisciplina.getProfessor());
        try {
            ofertaDisciplinaRepository.delete(ofertaDisciplina);
        } catch (DataIntegrityViolationException e) {
            redirect.addFlashAttribute("erroExclusao", true);
        }
        return "redirect:/listaOfertaDisciplinas";
    }

    private boolean fixarProfessor(OfertaDisciplina ofertaDisciplina, Model model) {
        if (!usuarioLogado.ehProfessor()) {
            return true;
        }
        Professor professor = usuarioLogado.professor();
        if (professor == null) {
            model.addAttribute("erroProfessor", UsuarioLogado.SEM_PROFESSOR);
            return false;
        }
        ofertaDisciplina.setProfessor(professor);
        return true;
    }

    private void adicionarOpcoes(Model model) {
        model.addAttribute("disciplinas", disciplinaRepository.findAll());
        model.addAttribute("professores", professorRepository.findAll());
        model.addAttribute("usuarioProfessor", usuarioLogado.ehProfessor());
        model.addAttribute("professorLogado", usuarioLogado.professor());
    }
}
