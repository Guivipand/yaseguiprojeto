package com.ifsp.yas1.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.ifsp.yas1.Service.EmprestimoService;
import com.ifsp.yas1.Service.ReservaService;
import com.ifsp.yas1.Service.UsuarioLogado;

@Controller
public class EmprestimoController {

    @Autowired
    private EmprestimoRepository repository;

    @Autowired
    private ExemplarRepository exemplarRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuarioLogado usuarioLogado;

    @GetMapping("/formularioEmprestimo")
    public String formularioEmprestimo(Model model) {
        model.addAttribute("emprestimo", new Emprestimo());
        adicionarOpcoesEmprestimo(model);
        return "formulario/formularioEmprestimo";
    }

    @PostMapping("/cadastrarEmprestimo")
    @Transactional
    public String cadastrarEmprestimo(@ModelAttribute Emprestimo registro, Model model) {
        emprestimoService.atualizarAtrasos();
        reservaService.processarExpiradas();
        registro.setRenovacoes(0);
        if (usuarioLogado.ehAluno()) {
            registro.setAluno(usuarioLogado.aluno());
            if (registro.getAluno() == null) {
                model.addAttribute("erroRegra", UsuarioLogado.SEM_ALUNO);
                adicionarOpcoesEmprestimo(model);
                return "formulario/formularioEmprestimo";
            }
        }
        if (registro.getExemplar() != null) {
            registro.setExemplar(exemplarRepository.findById(registro.getExemplar().getId()).orElse(null));
        }
        boolean exemplarLiberado = registro.validarDisponibilidadeExemplar()
                || (registro.getExemplar() != null && registro.getAluno() != null
                        && reservaService.reservadoPara(registro.getExemplar(), registro.getAluno()));
        if (!registro.validar() || !registro.validarDataRetirada() || !exemplarLiberado) {
            model.addAttribute("erroDataRetirada", !registro.validarDataRetirada());
            model.addAttribute("erroDataPrevista", !registro.validarDataPrevista());
            model.addAttribute("erroCamposEmprestimo", !registro.validarCampos());
            model.addAttribute("erroExemplarIndisponivel", !exemplarLiberado);
            adicionarOpcoesEmprestimo(model);
            return "formulario/formularioEmprestimo";
        }
        String impedimento = emprestimoService.verificarRegras(registro.getAluno(), registro.getExemplar().getLivro());
        if (impedimento != null) {
            model.addAttribute("erroRegra", impedimento);
            adicionarOpcoesEmprestimo(model);
            return "formulario/formularioEmprestimo";
        }
        registro.setStatus("Emprestado");
        registro.getExemplar().setStatus("Indisponível");
        exemplarRepository.save(registro.getExemplar());
        reservaService.marcarAtendida(registro.getExemplar(), registro.getAluno());
        repository.save(registro);
        return "redirect:/sucessCad";
    }

    @GetMapping("/listaEmprestimos")
    public String listaEmprestimos(@RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") int pagina, Model model) {
        emprestimoService.atualizarAtrasos();
        PageRequest paginacao = PageRequest.of(Math.max(pagina, 0), 10, Sort.by("id"));
        Page<Emprestimo> resultado = usuarioLogado.ehAluno()
                ? repository.buscarDoAluno(usuarioLogado.idAluno(), q.trim(), paginacao)
                : repository.buscar(q.trim(), paginacao);
        List<Emprestimo> lista = resultado.getContent();
        model.addAttribute("emprestimos", lista);
        model.addAttribute("pagina", resultado);
        model.addAttribute("q", q);
        return "lista/listaEmprestimos";
    }

    @GetMapping("/detalhesEmprestimo")
    public String detalhesEmprestimo(@RequestParam int id, Model model) {
        emprestimoService.atualizarAtrasos();
        Emprestimo registro = repository.findById(id).orElseThrow();
        usuarioLogado.exigirDono(registro.getAluno());
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
        usuarioLogado.exigirDono(registro.getAluno());
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
            repository.save(registro);
            reservaService.liberarExemplar(registro.getExemplar());
        }
        return "redirect:/detalhesEmprestimo?id=" + id;
    }

    @GetMapping("/excluirEmprestimo")
    @Transactional
    public String excluirEmprestimo(@RequestParam int id) {
        Emprestimo registro = repository.findById(id).orElseThrow();
        if (registro.getData_devolucao() == null && registro.getExemplar() != null) {
            reservaService.liberarExemplar(registro.getExemplar());
        }
        repository.delete(registro);
        return "redirect:/listaEmprestimos";
    }

    private void adicionarOpcoesEmprestimo(Model model) {
        model.addAttribute("exemplares", exemplarRepository.findByStatusIn(List.of("Disponível", "Reservado")));
        model.addAttribute("alunos", alunoRepository.findAll());
        model.addAttribute("dataMinima", LocalDate.now());
        model.addAttribute("usuarioAluno", usuarioLogado.ehAluno());
        model.addAttribute("alunoLogado", usuarioLogado.aluno());
    }

}
