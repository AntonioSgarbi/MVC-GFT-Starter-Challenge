package tech.antoniosgarbi.desafiomvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.antoniosgarbi.desafiomvc.model.Participante;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
}
