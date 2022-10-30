package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.repository.ParticipantRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final GroupService groupService;
    private final ActivityService activityService;
    private final AttendanceListService attendanceListService;


    public Participant findById(Long id) {
        return this.participantRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
    }

    public Participant save(Participant participant) {
        checkDuplicate(participant);
        return this.participantRepository.save(participant);
    }

    public List<Participant> findAll() {
        return this.participantRepository.findAll();
    }

    public void delete(Long id) {
        Participant participant = this.findById(id);
        this.groupService.removeReferencesFromParticipant(participant);
        this.activityService.removeReferencesFromParticipant(participant);
        this.attendanceListService.removeReferencesFromParticipant(participant);
        this.participantRepository.deleteById(id);
    }

    public Page<Participant> findByName(String name, Pageable pageable) {
        return this.participantRepository.findByNameContains(name, pageable);
    }

    private void checkDuplicate(Participant participant) {
        Optional<Participant> optional = this.participantRepository.findByLetters(participant.getLetters());
        if(participant.getId() == null && optional.isPresent()) {
            throw new RuntimeException("Participant is alread registered!");
        }
    }
}
