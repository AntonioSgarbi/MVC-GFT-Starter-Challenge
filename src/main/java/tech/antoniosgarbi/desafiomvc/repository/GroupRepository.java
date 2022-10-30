package tech.antoniosgarbi.desafiomvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.antoniosgarbi.desafiomvc.model.Group;
import tech.antoniosgarbi.desafiomvc.model.Participant;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findAllByMembersContains(Participant participant);
}
