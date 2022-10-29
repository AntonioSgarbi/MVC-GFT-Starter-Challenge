package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafiomvc.model.AttendanceList;
import tech.antoniosgarbi.desafiomvc.model.Event;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.repository.AttendanceRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class AttendanceListService {
    private final AttendanceRepository AttendanceRepository;

    public AttendanceListService(AttendanceRepository attendanceRepository) {
        AttendanceRepository = attendanceRepository;
    }

    public AttendanceList save(AttendanceList attendanceList) {
        return this.AttendanceRepository.save(attendanceList);
    }

    public AttendanceList createNewList(Date date) {
        return this.AttendanceRepository.save(new AttendanceList(null, date, new ArrayList<>(), new ArrayList<>()));
    }

    public AttendanceList findById(Long id) {
        return this.AttendanceRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
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

    public void checkAttendanceList(Event newEvent, Optional<Event> atualRegister) {
        List<Date> datesInRange = this.getDatesOnRange(newEvent);
                
        // just create an attendance list for each date
        if (newEvent.getId() == null) {
            datesInRange.forEach(date -> newEvent.getPresences().add(this.createNewList(date)));
        }
        // remove attendance lists that have dates that were removed and create new ones
        // using the newly added dates, keeping the dates that have not changed
        else {
            Event atual = atualRegister.get();

            if (!this.datesHasChanged(atual, newEvent)) {
                newEvent.setPresences(atual.getPresences());
            } else {
                newEvent.getPresences().forEach(list -> {
                    this.removeFromArrivedWhoDidNotWerePresent(list);
                });

                List<AttendanceList> atualIncluded = new LinkedList<>();
                List<Date> atualIncludedDate = new LinkedList<>();

                for (AttendanceList attendanceList : atual.getPresences()) {
                    for (Date date : datesInRange) {
                        if (date.compareTo(attendanceList.getDate()) == 0) {
                            atualIncluded.add(attendanceList);
                            atualIncludedDate.add(date);
                        }
                    }
                }
                newEvent.setPresences(atualIncluded);

                for (Date date : datesInRange) {
                    if (!atualIncludedDate.contains(date)) {
                        newEvent.getPresences().add(this.createNewList(date));
                    }
                }
            }
        }
    }

    private boolean datesHasChanged(Event atual, Event update) {
        return atual.getStart().compareTo(update.getStart()) != 0
                || atual.getEnd().compareTo(update.getEnd()) != 0
                || atual.isWeekendIncluded() != update.isWeekendIncluded();
    }

    private void removeFromArrivedWhoDidNotWerePresent(AttendanceList attendanceList) {
        if(attendanceList.getParticipantsWerePresent() == null || attendanceList.getParticipantsArrivedLate() != null) {
            attendanceList.setParticipantsArrivedLate(null);
        } else {
            List<Participant> wasPresentAndLate = new LinkedList<>();
            attendanceList.getParticipantsArrivedLate().forEach(participant -> {
                if (attendanceList.getParticipantsWerePresent().contains(participant)) {
                    wasPresentAndLate.add(participant);
                }
            });
            attendanceList.setParticipantsWerePresent(wasPresentAndLate);
        }
    }

    private boolean isWeekend(final Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        int day = cal.get(Calendar.DAY_OF_WEEK);

        return day == Calendar.SATURDAY || day == Calendar.SUNDAY;
    }

    
}
