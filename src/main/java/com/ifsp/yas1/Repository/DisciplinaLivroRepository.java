package com.ifsp.yas1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifsp.yas1.Model.DisciplinaLivro;

@Repository
public interface DisciplinaLivroRepository extends JpaRepository<DisciplinaLivro, Integer> {
}