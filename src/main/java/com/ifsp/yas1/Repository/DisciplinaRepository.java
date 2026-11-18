package com.ifsp.yas1.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifsp.yas1.Model.Disciplina;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Integer>{
    @Query("select d from Disciplina d left join d.curso c where :q = '' or lower(d.materia) like lower(concat('%', :q, '%')) or lower(c.titulo) like lower(concat('%', :q, '%'))")
    Page<Disciplina> buscar(@Param("q") String q, Pageable pageable);
}
