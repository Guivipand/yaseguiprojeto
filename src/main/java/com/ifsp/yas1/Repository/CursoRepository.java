package com.ifsp.yas1.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifsp.yas1.Model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer>{
    @Query("select c from Curso c where :q = '' or lower(c.titulo) like lower(concat('%', :q, '%'))")
    Page<Curso> buscar(@Param("q") String q, Pageable pageable);
}
