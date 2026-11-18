package com.ifsp.yas1.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifsp.yas1.Model.Prova;

@Repository
public interface ProvaRepository extends JpaRepository<Prova, Integer>{
    @Query("select p from Prova p left join p.ofertaDisciplina o left join o.disciplina d where :q = '' or lower(p.conteudo) like lower(concat('%', :q, '%')) or lower(d.materia) like lower(concat('%', :q, '%'))")
    Page<Prova> buscar(@Param("q") String q, Pageable pageable);
}
