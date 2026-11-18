package com.ifsp.yas1.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ifsp.yas1.Model.Aluno;
import com.ifsp.yas1.Model.Matricula;
import com.ifsp.yas1.Repository.AlunoRepository;
import com.ifsp.yas1.Repository.CursoRepository;
import com.ifsp.yas1.Repository.MatriculaRepository;
import com.ifsp.yas1.Repository.OfertaDisciplinaRepository;
import com.ifsp.yas1.Service.UsuarioLogado;

@Controller
public class MatriculaController {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private OfertaDisciplinaRepository ofertaDisciplinaRepository;

    @Autowired
    private UsuarioLogado usuarioLogado;

    @GetMapping("/formularioMatricula")
    public String formularioMatricula(Model model) {
        model.addAttribute("matricula", new Matricula());
        adicionarOpcoes(model);
        return "formulario/formularioMatricula";
    }

    @PostMapping("/cadastrarMatricula")
    public String saveMatriculas(@ModelAttribute Matricula matricula, Model model) {
        if (usuarioLogado.ehAluno()) {
            Aluno aluno = usuarioLogado.aluno();
            if (aluno == null) {
                model.addAttribute("erroAluno", UsuarioLogado.SEM_ALUNO);
                adicionarOpcoes(model);
                return "formulario/formularioMatricula";
            }
            matricula.setAluno(aluno);
            matricula.setCurso(aluno.getCurso());
        }
        matricula.setStatus("Ativa");
        matricula.setData_matricula(LocalDate.now());
        if (!matricula.validar() || matriculaDuplicada(matricula)) {
            model.addAttribute("erroValidacao", !matricula.validar());
            model.addAttribute("erroDuplicada", matricula.validar());
            adicionarOpcoes(model);
            return "formulario/formularioMatricula";
        }
        matriculaRepository.save(matricula);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaMatriculas")
    public String lMatricula(@RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") int pagina, Model model) {
        PageRequest paginacao = PageRequest.of(Math.max(pagina, 0), 10, Sort.by("id"));
        Page<Matricula> resultado = usuarioLogado.ehAluno()
                ? matriculaRepository.buscarDoAluno(usuarioLogado.idAluno(), q.trim(), paginacao)
                : matriculaRepository.buscar(q.trim(), paginacao);
        List<Matricula> listaMatriculas = resultado.getContent();
        model.addAttribute("pagina", resultado);
        model.addAttribute("q", q);
        model.addAttribute("matriculas", listaMatriculas);
        return "lista/listaMatricula";
    }

    @GetMapping("/editarMatricula")
    public String editarMatricula(@RequestParam int id, Model model) {
        Matricula matricula = matriculaRepository.findById(id).orElseThrow();
        model.addAttribute("matricula", matricula);
        adicionarOpcoes(model);
        return "editar/editarMatricula";
    }

    @PostMapping("/atualizarMatricula")
    public String atualizarMatricula(@ModelAttribute Matricula matricula, Model model) {
        Matricula existente = matriculaRepository.findById(matricula.getId()).orElseThrow();
        matricula.setData_matricula(existente.getData_matricula());
        if (!matricula.validar() || matriculaDuplicada(matricula)) {
            model.addAttribute("erroValidacao", !matricula.validar());
            model.addAttribute("erroDuplicada", matricula.validar());
            adicionarOpcoes(model);
            return "editar/editarMatricula";
        }
        matriculaRepository.save(matricula);
        return "redirect:/listaMatriculas";
    }

    @GetMapping("/excluirMatricula")
    public String excluirMatricula(@RequestParam int id) {
        Matricula matricula = matriculaRepository.findById(id).orElseThrow();
        matriculaRepository.delete(matricula);
        return "redirect:/listaMatriculas";
    }

    private boolean matriculaDuplicada(Matricula matricula) {
        return matricula.validar()
            && matriculaRepository.contarDuplicadas(matricula.getAluno().getId(),
                    matricula.getOfertaDisciplina().getId(), matricula.getId()) > 0;
    }

    private void adicionarOpcoes(Model model) {
        model.addAttribute("alunos", alunoRepository.findAll());
        model.addAttribute("cursos", cursoRepository.findAll());
        model.addAttribute("ofertasDisciplina", ofertaDisciplinaRepository.findAll());
        model.addAttribute("statusMatricula", Matricula.STATUS);
        model.addAttribute("usuarioAluno", usuarioLogado.ehAluno());
        model.addAttribute("alunoLogado", usuarioLogado.aluno());
    }
}
