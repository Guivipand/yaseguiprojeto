package com.ifsp.yas1.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifsp.yas1.Model.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Integer>{
    @Query("select l from Livro l where :q = '' or lower(l.titulo) like lower(concat('%', :q, '%')) or lower(l.autor) like lower(concat('%', :q, '%')) or lower(l.editora) like lower(concat('%', :q, '%')) or l.isbn like lower(concat('%', :q, '%'))")
    Page<Livro> buscar(@Param("q") String q, Pageable pageable);
}
