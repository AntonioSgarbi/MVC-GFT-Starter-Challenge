package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafiomvc.model.Activity;
import tech.antoniosgarbi.desafiomvc.model.Event;
import tech.antoniosgarbi.desafiomvc.model.Group;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.repository.EventRepository;

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
        checkDuplicate(event);
        checkActivities(event);
        checkGroups(event);
        return this.eventRepository.save(event);
    }

    public Event createEmptyEvent() {
        Event event = new Event();
        event.setActivities(List.of(new Activity()));
        Group group = new Group();
        event.setGroups(List.of(group));

        return event;
    }

    private void checkGroups(Event event) {
        Map<Long, Participant> participants = new HashMap<>();
        
        List<Group> verifiedGroups = new LinkedList<>();
        
        for(Group group : event.getGroups()) { 
            if(group != null && group.getMembers() != null) {
                for(Participant participant : group.getMembers()) {
                    if(participant != null) {
                        if(participants.containsKey(participant.getId()))
                            throw new RuntimeException("Uma pessoa não pode participar de dois grupos em um mesmo evento!");
                        else
                            participants.put(participant.getId(), participant);
                    }
                }
                verifiedGroups.add(groupService.save(group));
            }
        }
        event.setGroups(verifiedGroups);
    }

    private void checkActivities(Event event) {
        System.out.println(event);
        List<Activity> verifiedActivities = new LinkedList<>();
        event.getActivities().stream().forEach(activity -> {
            if(activity != null && activity.getName() != null && activity.getStart() != null && activity.getEnd() != null) {
                if(activity.getEnd().compareTo(event.getEnd()) > 0) {
                    throw new RuntimeException("Não é possível cadastrar Atividade com entrega marcada para após o fim do evento");
                }
                verifiedActivities.add(this.activityService.save(activity));
            }
        });
        event.setActivities(verifiedActivities);
    }

    public List<Event> findAll() {
        return this.eventRepository.findAll();
    }

    public List<Participant> findAllParticipants(Activity activity) {
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
        if(event.getId() == null && optional.isPresent()) {
            throw new RuntimeException("Event is alread registered!");
        }
    }
}
