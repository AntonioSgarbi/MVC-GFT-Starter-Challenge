package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafiomvc.model.Group;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.repository.GroupRepository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group findById(Long id) {
        return this.groupRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
    }

    public Group save(Group group) {
        return this.groupRepository.save(group);
    }

    public List<Group> checkGroups(List<Group> groups) {
        Map<Long, Participant> participants = new HashMap<>();

        List<Group> verifiedGroups = new LinkedList<>();

        for (Group group : groups) {
            if (group != null && group.getMembers() != null) {
                for (Participant participant : group.getMembers()) {
                    if (participant != null) {
                        if (participants.containsKey(participant.getId()))
                            throw new RuntimeException("Uma pessoa não pode participar de dois grupos em um mesmo evento!");
                        else
                            participants.put(participant.getId(), participant);
                    }
                }
                verifiedGroups.add(this.save(group));
            }
        }
        return verifiedGroups;
    }

    public List<Group> findAll() {
        return this.groupRepository.findAll();
    }

    public void delete(Long id) {
        this.groupRepository.deleteById(id);
    }

}
