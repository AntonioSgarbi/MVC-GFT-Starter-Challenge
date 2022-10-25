package tech.antoniosgarbi.desafiomvc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.antoniosgarbi.desafiomvc.model.Participant;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Page<Participant> findByNameContains(String name, Pageable pageable);

    Optional<Participant> findByLetters(String letters);

}
