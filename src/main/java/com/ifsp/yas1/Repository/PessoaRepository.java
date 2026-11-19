package com.ifsp.yas1.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ifsp.yas1.Model.Pessoa;
import com.ifsp.yas1.Service.PessoaRole;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer>{
    Pessoa findByUsername(String username);

    @Query("select p from Pessoa p where p.username is not null and p.role <> :admin and (:q = '' or lower(p.username) like lower(concat('%', :q, '%')) or lower(p.nome) like lower(concat('%', :q, '%')))")
    Page<Pessoa> buscarUsuarios(@Param("admin") PessoaRole admin, @Param("q") String q, Pageable pageable);
}
