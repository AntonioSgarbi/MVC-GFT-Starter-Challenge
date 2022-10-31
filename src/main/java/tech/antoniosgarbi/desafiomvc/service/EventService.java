package tech.antoniosgarbi.desafiomvc.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.antoniosgarbi.desafiomvc.model.*;
import tech.antoniosgarbi.desafiomvc.repository.EventRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Getter
@Transactional
@EnableScheduling
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ActivityService activityService;
    private final GroupService groupService;
    private final AttendanceListService attendanceListService;

    public Event save(Event event) {
        this.checkDuplicate(event);
        
        List<Activity> verifiedActivities = this.activityService
                .checkAtivityDeadlineAndPersistTransient(event);
                
        event.setActivities(verifiedActivities);

        List<Group> verifiedGroups = this.groupService.checkGroups(event.getGroups());
        
        event.setGroups(verifiedGroups);

        if (event.getStart().compareTo(event.getEnd()) > 0) {
            throw new RuntimeException("A data do fim do evento não pode ser anterior a data de início do evento");
        }

        if (event.getPresences() == null) {
            event.setPresences(new ArrayList<>());
        }

        if(event.getId() != null) {
            this.attendanceListService.checkAttendanceList(event, this.eventRepository.findById(event.getId()));

        } else {
            this.attendanceListService.checkAttendanceList(event, null);

        }

        return this.eventRepository.save(event);
    }

    public Event createEmptyEvent() {
        Event event = new Event();
        event.setActivities(List.of(new Activity()));
        event.setPresences(new ArrayList<>());
        
        Group group = new Group();
        group.setScore(0);
        event.setGroups(List.of(group));

        return event;
    }

    public Page<Event> findAll(Pageable pageable) {
        return this.eventRepository.findAll(pageable);
    }

    public List<Participant> findAllParticipantsFromEvent(Long eventId) {
        Event event = this.findById(eventId);

        List<Participant> participants = new LinkedList<>();

        event.getGroups().forEach(group -> participants.addAll(group.getMembers()));

        return participants;
    }

    public Event findById(Long id) {
        return this.eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("O evento selecionado não está mais disponível"));
    }

    public void delete(Long id) {
        Event event = this.findById(id);
        List<Activity> activities = event.getActivities();
        List<AttendanceList> attendanceLists = event.getPresences();
        List<Group> groups = event.getGroups();

        event.setActivities(null);
        event.setPresences(null);
        event.setGroups(null);
        
        this.eventRepository.save(event);
        this.eventRepository.delete(event);

        this.activityService.deleteAll(activities);
        this.attendanceListService.deleteAll(attendanceLists);
        this.groupService.deleteAll(groups);
    }

    public List<Participant> getAllParticipantsFromEvent(Event event) {
        List<Participant> allFromEvent = new LinkedList<>();

        event.getGroups().forEach(group -> allFromEvent.addAll(group.getMembers()));

        return allFromEvent;
    }

    public List<Participant> getAllParticipantsFromEventId(Long eventId) {
        List<Participant> allFromEvent = new LinkedList<>();

        this.findById(eventId).getGroups().forEach(group -> allFromEvent.addAll(group.getMembers()));

        return allFromEvent;
    }

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void updateRankings() {
        LocalDate today = LocalDate.now().atStartOfDay().toLocalDate();
        Instant todayInstant = today.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Date todayDate = Date.from(todayInstant);

        LocalDate tomorrow = today.plusDays(1);
        Instant tomorrowInstant = tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Date tomorrowDate = Date.from(tomorrowInstant);

        List<Event> allEventsRunning = this.eventRepository
                .findAllByStartGreaterThanEqualOrEndLessThanEqual(todayDate, tomorrowDate);

        for (Event event : allEventsRunning) {
            List<Group> allGroups = event.getGroups();

            for (Group group : allGroups) {
                int score = 0;
                boolean fullPresence = true;
                boolean fullActivity = true;

                for (Participant participant : group.getMembers()) {

                    for (AttendanceList attendanceList : event.getPresences()) {

                        if (attendanceList.getParticipantsArrivedLate().contains(participant)) {
                            score += 8;
                        } else if (attendanceList.getParticipantsWerePresent().contains(participant)) {
                            score += 10;
                        } else {
                            fullPresence = false;
                        }
                    }

                    for (Activity activity : event.getActivities()) {

                        if (activity.getDelayed().contains(participant)) {
                            score += 8;
                        } else if (activity.getDelivered().contains(participant)) {
                            score += 10;
                        } else {
                            fullActivity = false;
                        }
                    }
                }

                if (fullPresence)
                    score += 5;
                if (fullActivity)
                    score += 3;

                group.setScore(score);
                this.groupService.save(group);
            }
        }
    }

    private void checkDuplicate(Event event) {
        Optional<Event> optional = this.eventRepository.findByName(event.getName());
        if (event.getId() == null && optional.isPresent()) {
            throw new RuntimeException("Já existe um evento com esse nome registrado!");
        }
    }

}
