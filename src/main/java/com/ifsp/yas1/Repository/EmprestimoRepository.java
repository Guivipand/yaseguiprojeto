package com.ifsp.yas1.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifsp.yas1.Model.Emprestimo;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Integer>{
    @Query("select e from Emprestimo e left join e.aluno a left join e.exemplar ex left join ex.livro l where :q = '' or lower(a.nome) like lower(concat('%', :q, '%')) or lower(ex.tombo) like lower(concat('%', :q, '%')) or lower(l.titulo) like lower(concat('%', :q, '%')) or lower(e.status) like lower(concat('%', :q, '%'))")
    Page<Emprestimo> buscar(@Param("q") String q, Pageable pageable);

    @Query("select e from Emprestimo e left join e.aluno a left join e.exemplar ex left join ex.livro l where e.aluno.id = :aluno and (:q = '' or lower(a.nome) like lower(concat('%', :q, '%')) or lower(ex.tombo) like lower(concat('%', :q, '%')) or lower(l.titulo) like lower(concat('%', :q, '%')) or lower(e.status) like lower(concat('%', :q, '%')))")
    Page<Emprestimo> buscarDoAluno(@Param("aluno") int aluno, @Param("q") String q, Pageable pageable);

    @Query("select e from Emprestimo e where e.data_devolucao is null")
    List<Emprestimo> listarAtivos();

    @Query("select count(e) from Emprestimo e where e.aluno.id = :aluno and e.data_devolucao is null")
    long contarAtivosDoAluno(@Param("aluno") int aluno);

    @Query("select count(e) from Emprestimo e where e.aluno.id = :aluno and e.data_devolucao is null and e.data_prevista < :hoje")
    long contarAtrasadosDoAluno(@Param("aluno") int aluno, @Param("hoje") LocalDate hoje);

    @Query("select max(e.data_devolucao) from Emprestimo e where e.aluno.id = :aluno and e.data_devolucao > e.data_prevista")
    LocalDate ultimaDevolucaoAtrasada(@Param("aluno") int aluno);

    @Query("select count(e) from Emprestimo e where e.aluno.id = :aluno and e.exemplar.livro.id = :livro and e.data_devolucao is null")
    long contarAtivosDoAlunoPorLivro(@Param("aluno") int aluno, @Param("livro") int livro);

    @Query("select count(e) from Emprestimo e, DisciplinaLivro dl where e.aluno.id = :aluno and dl.livro = e.exemplar.livro and dl.disciplina.id = :disciplina")
    long contarDaBibliografia(@Param("aluno") int aluno, @Param("disciplina") int disciplina);
}
