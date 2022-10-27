package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafiomvc.model.Activity;
import tech.antoniosgarbi.desafiomvc.model.Event;
import tech.antoniosgarbi.desafiomvc.model.Group;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.repository.EventRepository;

import javax.persistence.PreRemove;
import java.util.*;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final ActivityService activityService;
    private final GroupService groupService;

    public EventService(EventRepository eventRepository, ActivityService activityService, GroupService groupService) {
        this.eventRepository = eventRepository;
        this.activityService = activityService;
        this.groupService = groupService;
    }

    public Event save(Event event) {
        this.checkDuplicate(event);

        List<Activity> verifiedActivities =
                this.activityService.checkAtivityDeadlineAndPersistTransient(event.getActivities(), event.getEnd());
        event.setActivities(verifiedActivities);

        List<Group> verifiedGroups = this.groupService.checkGroups(event.getGroups());
        event.setGroups(verifiedGroups);

        return this.eventRepository.save(event);
    }

    public Event createEmptyEvent() {
        Event event = new Event();
        event.setActivities(List.of(new Activity()));
        Group group = new Group();
        event.setGroups(List.of(group));

        return event;
    }

    public List<Event> findAll() {
        return this.eventRepository.findAll();
    }

    public List<Participant> findAllParticipantsFromEvent(Activity activity) {
        List<Participant> participants = new LinkedList<>();
        Event event = this.eventRepository.findByActivitiesContains(activity)
                .orElseThrow(() -> new RuntimeException("Atividade já foi excluída deste evento"));

        event.getGroups().forEach(group -> participants.addAll(group.getMembers()));

        return participants;
    }

    public Event findById(Long id) {
        return this.eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    public void delete(Long id) {
        this.eventRepository.deleteById(id);
    }

    private void checkDuplicate(Event event) {
        Optional<Event> optional = this.eventRepository.findByName(event.getName());
        if (event.getId() == null && optional.isPresent()) {
            throw new RuntimeException("Já existe um evento com esse nome registrado!");
        }
    }
}
