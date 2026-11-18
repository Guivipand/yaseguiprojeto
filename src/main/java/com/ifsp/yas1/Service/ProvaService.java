package com.ifsp.yas1.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ifsp.yas1.Model.Aluno;
import com.ifsp.yas1.Model.Prova;
import com.ifsp.yas1.Repository.EmprestimoRepository;
import com.ifsp.yas1.Repository.MatriculaRepository;
import com.ifsp.yas1.Repository.ProvaRepository;

@Service
public class ProvaService {

    @Autowired
    private ProvaRepository provaRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Transactional
    public String marcarEstudada(Prova prova, Aluno aluno) {
        if (matriculaRepository.contarAtivasNaOferta(aluno.getId(), prova.getOfertaDisciplina().getId()) == 0) {
            return "Você não tem matrícula ativa na oferta desta prova.";
        }
        if (prova.foiEstudadaPor(aluno)) {
            return "Você já marcou esta prova como estudada.";
        }
        int disciplina = prova.getOfertaDisciplina().getDisciplina().getId();
        if (emprestimoRepository.contarDaBibliografia(aluno.getId(), disciplina) == 0) {
            return "Você ainda não pegou emprestado nenhum livro da bibliografia desta disciplina.";
        }
        prova.getAlunosQueEstudaram().add(aluno);
        provaRepository.save(prova);
        return null;
    }

    @Transactional
    public void desmarcarEstudada(Prova prova, Aluno aluno) {
        prova.getAlunosQueEstudaram().removeIf(item -> item.getId() == aluno.getId());
        provaRepository.save(prova);
    }
}
