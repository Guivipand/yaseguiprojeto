package com.ifsp.yas1.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifsp.yas1.Model.Bibliotecario;

@Repository
public interface BibliotecarioRepository extends JpaRepository<Bibliotecario, Integer>{
    @Query("select b from Bibliotecario b where :q = '' or lower(b.nome) like lower(concat('%', :q, '%')) or lower(b.prontuario) like lower(concat('%', :q, '%')) or lower(b.crb) like lower(concat('%', :q, '%'))")
    Page<Bibliotecario> buscar(@Param("q") String q, Pageable pageable);

    List<Bibliotecario> findByUsernameIsNullOrderByNomeAsc();
}
