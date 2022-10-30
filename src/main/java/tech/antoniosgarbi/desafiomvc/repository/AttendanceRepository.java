package tech.antoniosgarbi.desafiomvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.antoniosgarbi.desafiomvc.model.AttendanceList;
import tech.antoniosgarbi.desafiomvc.model.Participant;

public interface AttendanceRepository extends JpaRepository<AttendanceList, Long> {

    List<AttendanceList> findAllByParticipantsWerePresentContains(Participant participant);

}
