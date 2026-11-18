package com.ifsp.yas1.Controller;

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

import com.ifsp.yas1.Model.Reserva;
import com.ifsp.yas1.Repository.AlunoRepository;
import com.ifsp.yas1.Repository.LivroRepository;
import com.ifsp.yas1.Repository.ReservaRepository;
import com.ifsp.yas1.Service.ReservaService;
import com.ifsp.yas1.Service.UsuarioLogado;

@Controller
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuarioLogado usuarioLogado;

    @GetMapping("/formularioReserva")
    public String formularioReserva(Model model) {
        model.addAttribute("reserva", new Reserva());
        adicionarOpcoes(model);
        return "formulario/formularioReserva";
    }

    @PostMapping("/cadastrarReserva")
    public String cadastrarReserva(@ModelAttribute Reserva reserva, Model model) {
        if (usuarioLogado.ehAluno()) {
            reserva.setAluno(usuarioLogado.aluno());
            if (reserva.getAluno() == null) {
                model.addAttribute("erroRegra", UsuarioLogado.SEM_ALUNO);
                adicionarOpcoes(model);
                return "formulario/formularioReserva";
            }
        }
        if (!reserva.validar()) {
            model.addAttribute("erroCampos", true);
            adicionarOpcoes(model);
            return "formulario/formularioReserva";
        }
        String impedimento = reservaService.reservar(reserva);
        if (impedimento != null) {
            model.addAttribute("erroRegra", impedimento);
            adicionarOpcoes(model);
            return "formulario/formularioReserva";
        }
        return "redirect:/detalhesReserva?id=" + reserva.getId();
    }

    @GetMapping("/listaReservas")
    public String listaReservas(@RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") int pagina, Model model) {
        reservaService.processarExpiradas();
        PageRequest paginacao = PageRequest.of(Math.max(pagina, 0), 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Reserva> resultado = usuarioLogado.ehAluno()
                ? reservaRepository.buscarDoAluno(usuarioLogado.idAluno(), q.trim(), paginacao)
                : reservaRepository.buscar(q.trim(), paginacao);
        List<Reserva> reservas = resultado.getContent();
        model.addAttribute("pagina", resultado);
        model.addAttribute("q", q);
        model.addAttribute("reservas", reservas);
        return "lista/listaReservas";
    }

    @GetMapping("/detalhesReserva")
    public String detalhesReserva(@RequestParam int id, Model model) {
        reservaService.processarExpiradas();
        Reserva reserva = reservaRepository.findById(id).orElseThrow();
        usuarioLogado.exigirDono(reserva.getAluno());
        model.addAttribute("reserva", reserva);
        model.addAttribute("fila", reservaRepository.listarPendentesDoLivro(reserva.getLivro().getId()));
        return "detalhes/detalhesReserva";
    }

    @PostMapping("/cancelarReserva")
    public String cancelarReserva(@RequestParam int id) {
        Reserva reserva = reservaRepository.findById(id).orElseThrow();
        usuarioLogado.exigirDono(reserva.getAluno());
        reservaService.cancelar(reserva);
        return "redirect:/detalhesReserva?id=" + id;
    }

    @GetMapping("/excluirReserva")
    public String excluirReserva(@RequestParam int id) {
        Reserva reserva = reservaRepository.findById(id).orElseThrow();
        reservaService.excluir(reserva);
        return "redirect:/listaReservas";
    }

    private void adicionarOpcoes(Model model) {
        model.addAttribute("livros", livroRepository.findAll(Sort.by("titulo")));
        model.addAttribute("alunos", alunoRepository.findAll(Sort.by("nome")));
        model.addAttribute("usuarioAluno", usuarioLogado.ehAluno());
        model.addAttribute("alunoLogado", usuarioLogado.aluno());
    }
}
