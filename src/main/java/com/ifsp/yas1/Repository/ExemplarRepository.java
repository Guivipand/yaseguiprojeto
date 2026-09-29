package com.ifsp.yas1.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifsp.yas1.Model.Exemplar;

@Repository
public interface ExemplarRepository extends JpaRepository<Exemplar, Integer> {
	List<Exemplar> findByStatus(String status);
}
