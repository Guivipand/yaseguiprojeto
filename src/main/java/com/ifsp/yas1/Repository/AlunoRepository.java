package com.ifsp.yas1.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifsp.yas1.Model.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Integer>{
    @Query("select a from Aluno a where :q = '' or lower(a.nome) like lower(concat('%', :q, '%')) or lower(a.idMatricula) like lower(concat('%', :q, '%')) or a.cpf like lower(concat('%', :q, '%'))")
    Page<Aluno> buscar(@Param("q") String q, Pageable pageable);

    List<Aluno> findByUsernameIsNullOrderByNomeAsc();
}
