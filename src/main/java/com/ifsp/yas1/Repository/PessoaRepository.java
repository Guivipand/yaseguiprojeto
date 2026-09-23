package com.ifsp.yas1.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ifsp.yas1.Model.Pessoa;


@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer>{
}

