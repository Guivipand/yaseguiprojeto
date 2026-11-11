package com.ifsp.yas1.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifsp.yas1.Model.Matricula;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Integer>{
    @Query("select m from Matricula m left join m.aluno a left join m.ofertaDisciplina o left join o.disciplina d where :q = '' or lower(a.nome) like lower(concat('%', :q, '%')) or lower(d.materia) like lower(concat('%', :q, '%')) or lower(m.status) like lower(concat('%', :q, '%'))")
    Page<Matricula> buscar(@Param("q") String q, Pageable pageable);

    @Query("select m from Matricula m left join m.aluno a left join m.ofertaDisciplina o left join o.disciplina d where m.aluno.id = :aluno and (:q = '' or lower(a.nome) like lower(concat('%', :q, '%')) or lower(d.materia) like lower(concat('%', :q, '%')) or lower(m.status) like lower(concat('%', :q, '%')))")
    Page<Matricula> buscarDoAluno(@Param("aluno") int aluno, @Param("q") String q, Pageable pageable);

    @Query("select count(m) from Matricula m where m.aluno.id = :aluno and m.ofertaDisciplina.id = :oferta and m.id <> :id")
    long contarDuplicadas(@Param("aluno") int aluno, @Param("oferta") int oferta, @Param("id") int id);

    @Query("select count(m) from Matricula m where m.aluno.id = :aluno and m.ofertaDisciplina.id = :oferta and m.status = 'Ativa'")
    long contarAtivasNaOferta(@Param("aluno") int aluno, @Param("oferta") int oferta);

    @Query("select count(m) from Matricula m, DisciplinaLivro dl where m.aluno.id = :aluno and m.status = 'Ativa' and dl.disciplina = m.ofertaDisciplina.disciplina and dl.livro.id = :livro")
    long contarBibliografiaDoAluno(@Param("aluno") int aluno, @Param("livro") int livro);
}
