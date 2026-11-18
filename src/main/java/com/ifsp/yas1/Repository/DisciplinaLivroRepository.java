package com.ifsp.yas1.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifsp.yas1.Model.DisciplinaLivro;

@Repository
public interface DisciplinaLivroRepository extends JpaRepository<DisciplinaLivro, Integer>{
    @Query("select dl from DisciplinaLivro dl left join dl.disciplina d left join dl.livro l where :q = '' or lower(d.materia) like lower(concat('%', :q, '%')) or lower(l.titulo) like lower(concat('%', :q, '%'))")
    Page<DisciplinaLivro> buscar(@Param("q") String q, Pageable pageable);

    @Query("select count(dl) from DisciplinaLivro dl where dl.disciplina.id = :disciplina and dl.livro.id = :livro")
    long contarLivroNaBibliografia(@Param("disciplina") int disciplina, @Param("livro") int livro);
}
