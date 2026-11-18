package com.ifsp.yas1.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifsp.yas1.Model.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Integer>{
    @Query("select p from Professor p where :q = '' or lower(p.nome) like lower(concat('%', :q, '%')) or lower(p.prontuario) like lower(concat('%', :q, '%'))")
    Page<Professor> buscar(@Param("q") String q, Pageable pageable);

    List<Professor> findByUsernameIsNullOrderByNomeAsc();
}
