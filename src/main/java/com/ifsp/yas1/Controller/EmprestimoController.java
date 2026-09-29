package com.ifsp.yas1.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ifsp.yas1.Model.Emprestimo;
import com.ifsp.yas1.Repository.AlunoRepository;
import com.ifsp.yas1.Repository.EmprestimoRepository;
import com.ifsp.yas1.Repository.ExemplarRepository;

@Controller
public class EmprestimoController {

    @Autowired
    private EmprestimoRepository repository;

    @Autowired
    private ExemplarRepository exemplarRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @GetMapping("/formularioEmprestimo")
    public String formularioEmprestimo(Model model) {
        model.addAttribute("emprestimo", new Emprestimo());
        adicionarOpcoesEmprestimo(model);
        return "formulario/formularioEmprestimo";
    }

    @PostMapping("/cadastrarEmprestimo")
    @Transactional
    public String cadastrarEmprestimo(@ModelAttribute Emprestimo registro, Model model) {
        registro.setRenovacoes(0);
        if (registro.getExemplar() != null) {
            registro.setExemplar(exemplarRepository.findById(registro.getExemplar().getId()).orElse(null));
        }
        if (!registro.validar() || !registro.validarDataRetirada() || !registro.validarDisponibilidadeExemplar()) {
            model.addAttribute("erroDataRetirada", !registro.validarDataRetirada());
            model.addAttribute("erroDataPrevista", !registro.validarDataPrevista());
            model.addAttribute("erroCamposEmprestimo", !registro.validarCampos());
            model.addAttribute("erroExemplarIndisponivel", !registro.validarDisponibilidadeExemplar());
            adicionarOpcoesEmprestimo(model);
            return "formulario/formularioEmprestimo";
        }
        registro.setStatus("Emprestado");
        registro.getExemplar().setStatus("Indisponível");
        exemplarRepository.save(registro.getExemplar());
        repository.save(registro);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaEmprestimos")
    public String listaEmprestimos(Model model) {
        List<Emprestimo> lista = repository.findAll();
        model.addAttribute("emprestimos", lista);
        return "lista/listaEmprestimos";
    }

    @GetMapping("/detalhesEmprestimo")
    public String detalhesEmprestimo(@RequestParam int id, Model model) {
        Emprestimo registro = repository.findById(id).orElseThrow();
        model.addAttribute("emprestimo", registro);
        return "detalhes/detalhesEmprestimo";
    }

    @GetMapping("/editarEmprestimo")
    public String editarEmprestimo(@RequestParam int id, Model model) {
        Emprestimo registro = repository.findById(id).orElseThrow();
        model.addAttribute("emprestimo", registro);
        adicionarOpcoesEmprestimo(model);
        return "editar/editarEmprestimo";
    }

    @PostMapping("/atualizarEmprestimo")
    public String atualizarEmprestimo(@ModelAttribute Emprestimo registro, Model model) {
        Emprestimo existente = repository.findById(registro.getId()).orElseThrow();
        registro.setExemplar(existente.getExemplar());
        registro.setData_devolucao(existente.getData_devolucao());
        registro.setStatus(existente.getStatus());
        registro.setRenovacoes(existente.getRenovacoes());
        if (!registro.validar()) {
            model.addAttribute("erroDataPrevista", !registro.validarDataPrevista());
            model.addAttribute("erroCamposEmprestimo", !registro.validarCampos());
            adicionarOpcoesEmprestimo(model);
            return "editar/editarEmprestimo";
        }
        repository.save(registro);
        return "redirect:/listaEmprestimos";
    }

    @PostMapping("/renovarEmprestimo")
    @Transactional
    public String renovarEmprestimo(@RequestParam int id) {
        Emprestimo registro = repository.findById(id).orElseThrow();
        if (registro.renovar()) {
            repository.save(registro);
        }
        return "redirect:/detalhesEmprestimo?id=" + id;
    }

    @PostMapping("/devolverEmprestimo")
    @Transactional
    public String devolverEmprestimo(@RequestParam int id) {
        Emprestimo registro = repository.findById(id).orElseThrow();
        if (registro.getData_devolucao() == null) {
            registro.setData_devolucao(java.time.LocalDate.now());
            registro.setStatus("Devolvido");
            registro.getExemplar().setStatus("Disponível");
            exemplarRepository.save(registro.getExemplar());
            repository.save(registro);
        }
        return "redirect:/detalhesEmprestimo?id=" + id;
    }

    @GetMapping("/excluirEmprestimo")
    @Transactional
    public String excluirEmprestimo(@RequestParam int id) {
        Emprestimo registro = repository.findById(id).orElseThrow();
        if (registro.getData_devolucao() == null && registro.getExemplar() != null) {
            registro.getExemplar().setStatus("Disponível");
            exemplarRepository.save(registro.getExemplar());
        }
        repository.delete(registro);
        return "redirect:/listaEmprestimos";
    }

    private void adicionarOpcoesEmprestimo(Model model) {
        model.addAttribute("exemplares", exemplarRepository.findByStatus("Disponível"));
        model.addAttribute("alunos", alunoRepository.findAll());
        model.addAttribute("dataMinima", LocalDate.now());
    }

}
