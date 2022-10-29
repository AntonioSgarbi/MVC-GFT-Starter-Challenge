package tech.antoniosgarbi.desafiomvc.service;

import lombok.Getter;

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
import java.util.concurrent.TimeUnit;

@Service
@Getter
@Transactional
@EnableScheduling
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

        this.checkAttendanceList(event);

        return this.eventRepository.save(event);
    }

    private void checkAttendanceList(Event event) {
        List<Date> datesInRange = this.getDatesOnRange(event);

        // just create an attendance list for each date
        if (event.getId() == null) {
            datesInRange.forEach(date -> event.getPresences().add(this.attendanceListService.createNewList(date)));
        }
        // remove attendance lists that have dates that were removed and create new ones
        // using the newly added dates, keeping the dates that have not changed
        else {
            Event atual = findById(event.getId());

            if (!this.datesHasChanged(atual, event)) {
                event.setPresences(atual.getPresences());
            } else {
                List<AttendanceList> atualIncluded = new LinkedList<>();
                List<Date> atualIncludedDate = new LinkedList<>();

                for (AttendanceList attendanceList : atual.getPresences()) {
                    for (Date date : datesInRange) {
                        if (date.compareTo(attendanceList.getDate()) == 0) {
                            System.out.println(date);
                            atualIncluded.add(attendanceList);
                            atualIncludedDate.add(date);
                        }
                    }
                }
                event.setPresences(atualIncluded);

                for (Date date : datesInRange) {
                    if (!atualIncludedDate.contains(date)) {
                        System.out.println(date);
                        event.getPresences().add(this.attendanceListService.createNewList(date));
                    }
                }
            }
        }
    }

    private List<Date> getDatesOnRange(Event event) {
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
        return datesInRange;
    }

    private boolean datesHasChanged(Event atual, Event update) {
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
        System.out.println("lalalal");
        LocalDate today = LocalDate.now().atStartOfDay().toLocalDate();
        Instant todayInstant = today.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Date todayDate = Date.from(todayInstant);

        LocalDate tomorrow = today.plusDays(1);
        Instant tomorrowInstant = tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Date tomorrowDate = Date.from(tomorrowInstant);

        System.out.println(todayDate);
        System.out.println(tomorrowDate);

        List<Event> allEventsRunning = this.eventRepository
                .findAllByStartGreaterThanEqualOrEndLessThanEqual(todayDate, tomorrowDate);

        System.out.println(allEventsRunning);

        for (Event event : allEventsRunning) {
            System.out.println(event);

            List<Group> allGroups = event.getGroups();

            for (Group group : allGroups) {
                int score = 0;
                boolean fullPresence = true;
                boolean fullActivity = true;

                for (Participant participant : group.getMembers()) {

                    for (AttendanceList attendanceList : event.getPresences()) {

                        if (attendanceList.getParticipantsArrivedLate().contains(allGroups)) {
                            score += 8;
                        } else if (attendanceList.getParticipantsWerePresent().contains(participant)) {
                            score += 10;
                        } else {
                            fullPresence = false;
                        }
                    }

                    for (Activity activity : event.getActivities()) {

                        if (activity.getDelayed().contains(allGroups)) {
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

                System.out.println(score);

                group.setScore(score);
                this.groupService.save(group);
            }
        }
    }

}
