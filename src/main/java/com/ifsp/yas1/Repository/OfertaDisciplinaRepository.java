package com.ifsp.yas1.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifsp.yas1.Model.OfertaDisciplina;

@Repository
public interface OfertaDisciplinaRepository extends JpaRepository<OfertaDisciplina, Integer>{
    @Query("select o from OfertaDisciplina o left join o.disciplina d left join o.professor p where :q = '' or lower(d.materia) like lower(concat('%', :q, '%')) or lower(p.nome) like lower(concat('%', :q, '%'))")
    Page<OfertaDisciplina> buscar(@Param("q") String q, Pageable pageable);
}
