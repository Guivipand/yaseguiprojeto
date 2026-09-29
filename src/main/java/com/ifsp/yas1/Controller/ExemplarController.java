package com.ifsp.yas1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ifsp.yas1.Model.Exemplar;
import com.ifsp.yas1.Repository.ExemplarRepository;
import com.ifsp.yas1.Repository.LivroRepository;

@Controller
public class ExemplarController {

	@Autowired
	private ExemplarRepository exemplarRepository;

	@Autowired
	private LivroRepository livroRepository;

	@GetMapping("/formularioExemplar")
	public String formularioExemplar(Model model) {
		Exemplar exemplar = new Exemplar();
		exemplar.setStatus("Disponível");
		model.addAttribute("exemplar", exemplar);
		model.addAttribute("livros", livroRepository.findAll());
		return "formulario/formularioExemplar";
	}

	@PostMapping("/cadastrarExemplar")
	public String cadastrarExemplar(@ModelAttribute Exemplar exemplar, Model model) {
		if (!exemplar.validar()) {
			model.addAttribute("erroExemplar", true);
			model.addAttribute("livros", livroRepository.findAll());
			return "formulario/formularioExemplar";
		}
		exemplar.setStatus("Disponível");
		exemplarRepository.save(exemplar);
		return "redirect:/sucessCad";
	}

	@GetMapping("/listaExemplares")
	public String listaExemplares(Model model) {
		List<Exemplar> exemplares = exemplarRepository.findAll();
		model.addAttribute("exemplares", exemplares);
		return "lista/listaExemplares";
	}

	@GetMapping("/detalhesExemplar")
	public String detalhesExemplar(@RequestParam int id, Model model) {
		Exemplar exemplar = exemplarRepository.findById(id).orElseThrow();
		model.addAttribute("exemplar", exemplar);
		return "detalhes/detalhesExemplar";
	}

	@GetMapping("/editarExemplar")
	public String editarExemplar(@RequestParam int id, Model model) {
		Exemplar exemplar = exemplarRepository.findById(id).orElseThrow();
		model.addAttribute("exemplar", exemplar);
		model.addAttribute("livros", livroRepository.findAll());
		return "editar/editarExemplar";
	}

	@PostMapping("/atualizarExemplar")
	public String atualizarExemplar(@ModelAttribute Exemplar exemplar, Model model) {
		Exemplar exemplarExistente = exemplarRepository.findById(exemplar.getId()).orElseThrow();
		if (!exemplar.validar()) {
			model.addAttribute("erroExemplar", true);
			model.addAttribute("livros", livroRepository.findAll());
			return "editar/editarExemplar";
		}
		exemplar.setStatus(exemplarExistente.getStatus());
		exemplarRepository.save(exemplar);
		return "redirect:/listaExemplares";
	}

	@GetMapping("/excluirExemplar")
	public String excluirExemplar(@RequestParam int id) {
		Exemplar exemplar = exemplarRepository.findById(id).orElseThrow();
		exemplarRepository.delete(exemplar);
		return "redirect:/listaExemplares";
	}
}
