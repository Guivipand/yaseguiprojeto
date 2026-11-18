package com.ifsp.yas1.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifsp.yas1.Model.Exemplar;

@Repository
public interface ExemplarRepository extends JpaRepository<Exemplar, Integer>{
    @Query("select e from Exemplar e left join e.livro l where :q = '' or lower(e.tombo) like lower(concat('%', :q, '%')) or lower(l.titulo) like lower(concat('%', :q, '%')) or lower(e.status) like lower(concat('%', :q, '%'))")
    Page<Exemplar> buscar(@Param("q") String q, Pageable pageable);

    List<Exemplar> findByStatus(String status);

    List<Exemplar> findByStatusIn(List<String> status);

    @Query("select count(e) from Exemplar e where e.livro.id = :livro and e.status = :status")
    long contarPorLivroEStatus(@Param("livro") int livro, @Param("status") String status);

    @Query("select count(e) from Exemplar e where e.livro.id = :livro")
    long contarPorLivro(@Param("livro") int livro);
}
