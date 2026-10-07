package com.ifsp.yas1.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import com.ifsp.yas1.Model.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer>{
    // O Spring Data JPA cria a consulta automaticamente a partir do nome do método
    UserDetails findByUsername(String username);
}
