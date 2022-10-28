package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.repository.ParticipantRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

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
