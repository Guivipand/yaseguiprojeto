package com.ifsp.yas1.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ifsp.yas1.Model.Livro;
import com.ifsp.yas1.Repository.LivroRepository;
import com.ifsp.yas1.Service.LivroService;

@Controller
public class LivroController {

    @Autowired 
    private LivroService livroService;

    @Autowired
    private LivroRepository livroRepository;

    @GetMapping("/formularioLivro")
    public String formularioLivro(Model model) {
        model.addAttribute("livro", new Livro());
        return "formulario/formularioLivro";
    }

    @PostMapping("/cadastrarLivro")
    public String saveLivros(@ModelAttribute Livro livro, @RequestParam("imagem") MultipartFile imagem) throws IOException {
        String capa_imagem = livroService.salvarImagem(imagem);
        livro.setCapa_imagem(capa_imagem);
        livroRepository.save(livro);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaLivros")
    public String livro(Model model) {
        List<Livro> listaLivros = livroRepository.findAll();
        model.addAttribute("livros", listaLivros);
        return "lista/listaLivros";
    }

    @GetMapping("/detalhesLivro")
    public String detalhesLivro(@RequestParam int id, Model model) {
        Livro livro = livroRepository.findById(id).orElseThrow();
        model.addAttribute("livro", livro);
        return "detalhes/detalhesLivro";
    }

    @GetMapping("/editarLivro")
    public String editarLivro(@RequestParam int id, Model model) {
        Livro livro = livroRepository.findById(id).orElseThrow();
        model.addAttribute("Livro", livro);
        return "editar/editarLivro";
    }

    @PostMapping("/atualizarLivro")
    public String atualizarLivro(@ModelAttribute Livro livro, @RequestParam("imagem") MultipartFile imagem) throws IOException{
        String capa_imagem = livroService.salvarImagem(imagem);
        livro.setCapa_imagem(capa_imagem);
        livroRepository.save(livro);
        return "redirect:/listaLivros";
    }

    @GetMapping("/excluirLivro")
    public String excluirLivro(@RequestParam int id) {
        Livro livro = livroRepository.findById(id).orElseThrow();
        livroRepository.delete(livro);
        return "redirect:/listaLivros";
    }
}
