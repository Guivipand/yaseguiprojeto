package com.ifsp.yas1.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ifsp.yas1.Model.Curso;


@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer>{
}
