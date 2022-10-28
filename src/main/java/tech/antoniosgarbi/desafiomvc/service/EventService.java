package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.stereotype.Service;

import lombok.Getter;
import tech.antoniosgarbi.desafiomvc.model.Activity;
import tech.antoniosgarbi.desafiomvc.model.AttendanceList;
import tech.antoniosgarbi.desafiomvc.model.Event;
import tech.antoniosgarbi.desafiomvc.model.Group;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.repository.EventRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Getter
public class EventService {
    private final EventRepository eventRepository;
    private final ActivityService activityService;
    private final GroupService groupService;
    private final AttendanceListService attendanceListService;

    public EventService(EventRepository eventRepository, ActivityService activityService, GroupService groupService,
            AttendanceListService attendanceListService) {
        this.eventRepository = eventRepository;
        this.activityService = activityService;
        this.groupService = groupService;
        this.attendanceListService = attendanceListService;
    }

    public Event save(Event event) {
        this.checkDuplicate(event);

        List<Activity> verifiedActivities = this.activityService
                .checkAtivityDeadlineAndPersistTransient(event.getActivities(), event.getEnd());
        event.setActivities(verifiedActivities);

        List<Group> verifiedGroups = this.groupService.checkGroups(event.getGroups());
        event.setGroups(verifiedGroups);

        if (event.getStart().compareTo(event.getEnd()) > 0) {
            throw new RuntimeException("A data do fim não pode ser anterior a data de início");
        }

        if (event.getPresences() == null) {
            event.setPresences(new ArrayList<>());
        }

        this.checkAttendanceList(event);

        return this.eventRepository.save(event);
    }

    private void checkAttendanceList(Event event) {

        long diff = event.getEnd().getTime() - event.getStart().getTime();

        int totalDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        List<Date> datesInRange = new LinkedList<>();
        // fill the list with all the dates that need to have an attendance list
        for (int i = 0; i <= totalDays; i++) {
            LocalDate startPlusIndex = event.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    .plusDays(i);
            Instant instant = startPlusIndex.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Date dayOnRange = Date.from(instant);

            if (event.isWeekendIncluded()) {
                datesInRange.add(dayOnRange);
            } else {
                if (!this.isWeekend(dayOnRange)) {
                    datesInRange.add(dayOnRange);
                }
            }
        }

        // just create an attendance list for each date
        if (event.getId() == null) {
            datesInRange.forEach(date -> event.getPresences().add(this.attendanceListService.createNewList(date)));
        }
        // remove attendance lists that have dates that were removed and create new ones
        // using the newly added dates, keeping the dates that have not changed
        else {
            Event atual = findById(event.getId());

            if (this.datesHasChange(atual, event)) {
                List<AttendanceList> atualIncluded = new LinkedList<>();
                List<Date> atualIncludedDate = new LinkedList<>();

                for (AttendanceList attendanceList : event.getPresences()) {
                    if (datesInRange.contains(attendanceList.getDate())) {
                        atualIncluded.add(attendanceList);
                        atualIncludedDate.add(attendanceList.getDate());
                    }
                }
                event.setPresences(atualIncluded);

                for (Date date : datesInRange) {
                    if (!atualIncludedDate.contains(date)) {
                        event.getPresences().add(this.attendanceListService.createNewList(date));
                    }
                }
            } else {
                event.setPresences(atual.getPresences());
            }
        }
    }

    private boolean datesHasChange(Event atual, Event update) {
        return atual.getStart().compareTo(update.getStart()) != 0 
                || atual.getEnd().compareTo(update.getEnd()) != 0
                || atual.isWeekendIncluded() != update.isWeekendIncluded();
    }

    private boolean isWeekend(final Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        int day = cal.get(Calendar.DAY_OF_WEEK);
        
        return day == Calendar.SATURDAY || day == Calendar.SUNDAY;
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

    public AttendanceList findAttendanceListById(Long id) {
        return this.attendanceListService.findById(id);
    }

    public List<Participant> getParticipantsFromEventId(Long eventId) {
        List<Participant> allFromEvent = new LinkedList<>();

        this.findById(eventId).getGroups().forEach(group -> allFromEvent.addAll(group.getMembers()));

        return allFromEvent;
    }
}
