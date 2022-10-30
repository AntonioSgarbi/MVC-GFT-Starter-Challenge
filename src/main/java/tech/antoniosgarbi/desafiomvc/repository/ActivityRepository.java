package tech.antoniosgarbi.desafiomvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.antoniosgarbi.desafiomvc.model.Activity;
import tech.antoniosgarbi.desafiomvc.model.Participant;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findAllByDeliveredContains(Participant participant);
}
