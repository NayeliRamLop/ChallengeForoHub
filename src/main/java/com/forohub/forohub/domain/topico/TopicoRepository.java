package com.forohub.forohub.domain.topico;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    boolean existsByTituloAndMensaje(String titulo, String mensaje);
    Optional<Topico> findByTituloAndMensaje(String titulo, String mensaje);
}
