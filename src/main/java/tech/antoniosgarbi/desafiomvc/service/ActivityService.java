package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafiomvc.model.Activity;
import tech.antoniosgarbi.desafiomvc.model.Event;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.repository.ActivityRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public Activity findById(Long id) {
        return this.activityRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
    }

    public Object findForScreen(List<Participant> participants, Activity activity) {
        // participantes do evento x participantes com entrega cadastrada
        // to-do

        return null;
    }

    public Activity save(Activity activity) {
        if (activity.getDelivered() == null && activity.getId() != null) {
            this.activityRepository
                    .findById(activity.getId())
                    .ifPresent(value -> activity.setDelivered(value.getDelivered()));
        }
        return this.activityRepository.save(activity);
    }

    public List<Activity> checkAtivityDeadlineAndPersistTransient(Event event) {
        if (event.getActivities() == null)
            return new ArrayList<>();

        List<Activity> verifiedActivities = new LinkedList<>();

        for (Activity activity : event.getActivities()) {
            this.validateActivityDates(activity, event.getStart(), event.getEnd());
            verifiedActivities.add(this.save(activity));
        }
        return verifiedActivities;
    }

    public List<Activity> findAll() {
        return this.activityRepository.findAll();
    }

    public void delete(Long id) {
        this.activityRepository.deleteById(id);
    }

    private void validateActivityDates(Activity activity, Date eventInit, Date eventDeadline) {
        if (activity.getStart().compareTo(eventInit) < 0) {
            throw new RuntimeException(
                    "Não é possível cadastrar Atividade com prazo marcado para antes do início do evento");
        } else if (activity.getEnd().compareTo(eventDeadline) > 0) {
            throw new RuntimeException(
                    "Não é possível cadastrar Atividade com prazo marcado para após o fim do evento");
        }
    }

}
