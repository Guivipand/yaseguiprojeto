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

import com.ifsp.yas1.Model.Bibliotecario;
import com.ifsp.yas1.Repository.BibliotecarioRepository;

@Controller
public class BibliotecarioController {

    @Autowired
    private BibliotecarioRepository bibliotecarioRepository;

    @GetMapping("/formularioBibliotecario")
    public String formularioBibliotecario(Model model) {
        model.addAttribute("bibliotecario", new Bibliotecario());
        return "formulario/formularioBibliotecario";
    }

    @PostMapping("/cadastrarBibliotecario")
    public String saveBibliotecario(@ModelAttribute Bibliotecario bibliotecario, Model model) {
        if (!bibliotecario.validar()) {
            model.addAttribute("erroValidacao", !bibliotecario.validarDados());
            model.addAttribute("erroIdade", !bibliotecario.validarIdade());
            return "formulario/formularioBibliotecario";
        }
        bibliotecarioRepository.save(bibliotecario);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaBibliotecarios")
    public String lBibliotecario(@RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") int pagina, Model model) {
        Page<Bibliotecario> resultado = bibliotecarioRepository.buscar(q.trim(), PageRequest.of(Math.max(pagina, 0), 10, Sort.by("id")));
        List<Bibliotecario> listaBibliotecarios = resultado.getContent();
        model.addAttribute("pagina", resultado);
        model.addAttribute("q", q);
        model.addAttribute("bibliotecarios", listaBibliotecarios);
        return "lista/listaBibliotecarios";
    }

    @GetMapping("/detalhesBibliotecario")
    public String detalhesBibliotecario(@RequestParam int id, Model model) {
        Bibliotecario bibliotecario = bibliotecarioRepository.findById(id).orElseThrow();
        model.addAttribute("bibliotecario", bibliotecario);
        return "detalhes/detalhesBibliotecario";
    }

    @GetMapping("/editarBibliotecario")
    public String editarBibliotecario(@RequestParam int id, Model model) {
        Bibliotecario bibliotecario = bibliotecarioRepository.findById(id).orElseThrow();
        model.addAttribute("bibliotecario", bibliotecario);
        return "editar/editarBibliotecario";
    }

    @PostMapping("/atualizarBibliotecario")
    public String atualizarBibliotecario(@ModelAttribute Bibliotecario bibliotecario, Model model) {
        Bibliotecario existente = bibliotecarioRepository.findById(bibliotecario.getId()).orElseThrow();
        bibliotecario.setUsername(existente.getUsername());
        bibliotecario.setPassword(existente.getPassword());
        bibliotecario.setRole(existente.getRole());
        if (!bibliotecario.validar()) {
            model.addAttribute("erroValidacao", !bibliotecario.validarDados());
            model.addAttribute("erroIdade", !bibliotecario.validarIdade());
            return "editar/editarBibliotecario";
        }
        bibliotecarioRepository.save(bibliotecario);
        return "redirect:/listaBibliotecarios";
    }

    @GetMapping("/excluirBibliotecario")
    public String excluirBibliotecario(@RequestParam int id, RedirectAttributes redirect) {
        Bibliotecario bibliotecario = bibliotecarioRepository.findById(id).orElseThrow();
        try {
            bibliotecarioRepository.delete(bibliotecario);
        } catch (DataIntegrityViolationException e) {
            redirect.addFlashAttribute("erroExclusao", true);
        }
        return "redirect:/listaBibliotecarios";
    }

}
