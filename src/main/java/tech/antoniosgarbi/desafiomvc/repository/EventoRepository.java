package tech.antoniosgarbi.desafiomvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.antoniosgarbi.desafiomvc.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}
