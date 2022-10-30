package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import tech.antoniosgarbi.desafiomvc.model.Activity;
import tech.antoniosgarbi.desafiomvc.model.Event;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.repository.ActivityRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    public Activity findById(Long id) {
        return this.activityRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
    }

    public Activity save(Activity activity) {
        if (activity.getDelivered() == null && activity.getId() != null) {
            this.activityRepository
                    .findById(activity.getId())
                    .ifPresent(value -> activity.setDelivered(value.getDelivered()));
        }

        this.removeFromDelayedWhoDidNotDelivered(activity);

        return this.activityRepository.save(activity);
    }

    private void removeFromDelayedWhoDidNotDelivered(Activity activity) {
        if (activity.getDelivered() == null || activity.getDelayed() == null) {
            activity.setDelayed(null);
        } else {
            List<Participant> deliveredAndDelayed = new LinkedList<>();
            activity.getDelayed().forEach(participant -> {
                if (activity.getDelivered().contains(participant)) {
                    deliveredAndDelayed.add(participant);
                }
            });
            activity.setDelayed(deliveredAndDelayed);
        }
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
        if (activity.getStart().compareTo(activity.getEnd()) > 0) {
            throw new RuntimeException("O inicio da atividade não pode ser cadastrado para depois do fim! Atividade: "
                    + activity.getName());
        }

        if (activity.getStart().compareTo(eventInit) < 0) {
            throw new RuntimeException(
                    "Não é possível cadastrar Atividade com prazo marcado para antes do início do evento");
        }

        if (activity.getEnd().compareTo(eventDeadline) > 0) {
            throw new RuntimeException(
                    "Não é possível cadastrar Atividade com prazo marcado para após o fim do evento");
        }
    }

    public void deleteAll(List<Activity> activities) {
        activities.forEach(a -> {
            a.setDelivered(null);
            a.setDelayed(null);
        });
        this.activityRepository.saveAll(activities);
        this.activityRepository.deleteAll(activities);
    }

    public void removeReferencesFromParticipant(Participant participant) {
        List<Activity> activities = this.activityRepository.findAllByDeliveredContains(participant);
        activities.forEach(a -> {
            a.getDelivered().remove(participant);
            a.getDelayed().remove(participant);
        });
        this.activityRepository.saveAll(activities);
    }

}
