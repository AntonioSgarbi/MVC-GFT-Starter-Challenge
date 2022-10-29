package tech.antoniosgarbi.desafiomvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.antoniosgarbi.desafiomvc.model.Activity;
import tech.antoniosgarbi.desafiomvc.model.Event;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);

    Optional<Event> findByActivitiesContains(Activity activity);

    List<Event> findAllByStartGreaterThanEqualOrEndLessThanEqual(Date start, Date end);
}
