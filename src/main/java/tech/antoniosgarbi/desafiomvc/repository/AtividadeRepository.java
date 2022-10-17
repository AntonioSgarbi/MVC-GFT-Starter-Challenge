package tech.antoniosgarbi.desafiomvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.antoniosgarbi.desafiomvc.model.Atividade;

public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
}
