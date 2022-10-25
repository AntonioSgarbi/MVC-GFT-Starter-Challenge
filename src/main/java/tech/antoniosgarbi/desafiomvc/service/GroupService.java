package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafiomvc.model.Group;
import tech.antoniosgarbi.desafiomvc.repository.GroupRepository;

import java.util.List;

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

    public List<Group> findAll() {
        return this.groupRepository.findAll();
    }

    public void delete(Long id) {
        this.groupRepository.deleteById(id);
    }

}
