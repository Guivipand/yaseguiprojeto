package com.ifsp.yas1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ifsp.yas1.Model.Aluno;
import com.ifsp.yas1.Model.Prova;
import com.ifsp.yas1.Repository.OfertaDisciplinaRepository;
import com.ifsp.yas1.Repository.ProvaRepository;
import com.ifsp.yas1.Service.ProvaService;
import com.ifsp.yas1.Service.UsuarioLogado;

@Controller
public class ProvaController {

    @Autowired
    private ProvaRepository provaRepository;

    @Autowired
    private OfertaDisciplinaRepository ofertaDisciplinaRepository;

    @Autowired
    private ProvaService provaService;

    @Autowired
    private UsuarioLogado usuarioLogado;

    @GetMapping("/formularioProva")
    public String formularioProva(Model model) {
        model.addAttribute("prova", new Prova());
        model.addAttribute("ofertasDisciplina", ofertaDisciplinaRepository.findAll());
        return "formulario/formularioProva";
    }

    @PostMapping("/cadastrarProva")
    public String cadastrarProva(@ModelAttribute Prova prova, Model model) {
        if (!prova.validar()) {
            adicionarErrosValidacao(model, prova);
            return "formulario/formularioProva";
        }
        provaRepository.save(prova);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaProvas")
    public String listaProvas(@RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") int pagina, Model model) {
        Page<Prova> resultado = provaRepository.buscar(q.trim(), PageRequest.of(Math.max(pagina, 0), 10, Sort.by("data")));
        List<Prova> provas = resultado.getContent();
        model.addAttribute("pagina", resultado);
        model.addAttribute("q", q);
        model.addAttribute("provas", provas);
        return "lista/listaProvas";
    }

    @GetMapping("/detalhesProva")
    public String detalhesProva(@RequestParam int id, Model model) {
        Prova prova = provaRepository.findById(id).orElseThrow();
        model.addAttribute("prova", prova);
        model.addAttribute("usuarioAluno", usuarioLogado.ehAluno());
        model.addAttribute("estudou", prova.foiEstudadaPor(usuarioLogado.aluno()));
        return "detalhes/detalhesProva";
    }

    @GetMapping("/editarProva")
    public String editarProva(@RequestParam int id, Model model) {
        Prova prova = provaRepository.findById(id).orElseThrow();
        model.addAttribute("prova", prova);
        model.addAttribute("ofertasDisciplina", ofertaDisciplinaRepository.findAll());
        return "editar/editarProva";
    }

    @PostMapping("/atualizarProva")
    public String atualizarProva(@ModelAttribute Prova prova, Model model) {
        Prova existente = provaRepository.findById(prova.getId()).orElseThrow();
        prova.setAlunosQueEstudaram(existente.getAlunosQueEstudaram());
        if (!prova.validar()) {
            adicionarErrosValidacao(model, prova);
            return "editar/editarProva";
        }
        provaRepository.save(prova);
        return "redirect:/listaProvas";
    }

    @GetMapping("/excluirProva")
    public String excluirProva(@RequestParam int id, RedirectAttributes redirect) {
        Prova prova = provaRepository.findById(id).orElseThrow();
        try {
            provaRepository.delete(prova);
        } catch (DataIntegrityViolationException e) {
            redirect.addFlashAttribute("erroExclusao", true);
        }
        return "redirect:/listaProvas";
    }

    @PostMapping("/marcarProvaEstudada")
    public String marcarProvaEstudada(@RequestParam int id, RedirectAttributes redirect) {
        Prova prova = provaRepository.findById(id).orElseThrow();
        Aluno aluno = usuarioLogado.aluno();
        String impedimento = aluno == null ? UsuarioLogado.SEM_ALUNO : provaService.marcarEstudada(prova, aluno);
        if (impedimento != null) {
            redirect.addFlashAttribute("erroRegra", impedimento);
        } else {
            redirect.addFlashAttribute("estudoRegistrado", true);
        }
        return "redirect:/detalhesProva?id=" + id;
    }

    @PostMapping("/desmarcarProvaEstudada")
    public String desmarcarProvaEstudada(@RequestParam int id) {
        Prova prova = provaRepository.findById(id).orElseThrow();
        Aluno aluno = usuarioLogado.aluno();
        if (aluno != null) {
            provaService.desmarcarEstudada(prova, aluno);
        }
        return "redirect:/detalhesProva?id=" + id;
    }

    private void adicionarErrosValidacao(Model model, Prova prova) {
        model.addAttribute("prova", prova);
        model.addAttribute("ofertasDisciplina", ofertaDisciplinaRepository.findAll());
        model.addAttribute("erroOferta", !prova.validarOfertaDisciplina());
        model.addAttribute("erroData", !prova.validarData());
        model.addAttribute("erroConteudo", !prova.validarConteudo());
        model.addAttribute("erroPeso", !prova.validarPeso());
    }
}
