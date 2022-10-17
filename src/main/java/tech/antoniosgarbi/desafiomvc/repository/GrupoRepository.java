package tech.antoniosgarbi.desafiomvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.antoniosgarbi.desafiomvc.model.Grupo;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
}
