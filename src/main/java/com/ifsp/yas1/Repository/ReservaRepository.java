package com.ifsp.yas1.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifsp.yas1.Model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer>{
    @Query("select r from Reserva r left join r.aluno a left join r.livro l where :q = '' or lower(a.nome) like lower(concat('%', :q, '%')) or lower(l.titulo) like lower(concat('%', :q, '%')) or lower(r.status) like lower(concat('%', :q, '%'))")
    Page<Reserva> buscar(@Param("q") String q, Pageable pageable);

    @Query("select r from Reserva r left join r.aluno a left join r.livro l where r.aluno.id = :aluno and (:q = '' or lower(a.nome) like lower(concat('%', :q, '%')) or lower(l.titulo) like lower(concat('%', :q, '%')) or lower(r.status) like lower(concat('%', :q, '%')))")
    Page<Reserva> buscarDoAluno(@Param("aluno") int aluno, @Param("q") String q, Pageable pageable);

    @Query("select r from Reserva r where r.livro.id = :livro and r.status = 'Aguardando' order by r.data_reserva, r.id")
    List<Reserva> filaDoLivro(@Param("livro") int livro);

    @Query("select r from Reserva r where r.status = 'Notificada' and r.prazo_retirada < :agora")
    List<Reserva> listarExpiradas(@Param("agora") LocalDateTime agora);

    @Query("select r from Reserva r where r.exemplar.id = :exemplar and r.status = 'Notificada'")
    Optional<Reserva> buscarNotificadaDoExemplar(@Param("exemplar") int exemplar);

    @Query("select r from Reserva r where r.aluno.id = :aluno and r.livro.id = :livro and r.status in ('Aguardando', 'Notificada')")
    List<Reserva> listarPendentesDoAluno(@Param("aluno") int aluno, @Param("livro") int livro);

    @Query("select r from Reserva r where r.livro.id = :livro and r.status in ('Aguardando', 'Notificada') order by r.status desc, r.data_reserva, r.id")
    List<Reserva> listarPendentesDoLivro(@Param("livro") int livro);
}
