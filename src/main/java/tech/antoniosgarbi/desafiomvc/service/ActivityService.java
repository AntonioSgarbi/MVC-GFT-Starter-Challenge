package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafiomvc.model.Activity;
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
        //participantes do evento x participantes com entrega cadastrada
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

    public List<Activity> checkAtivityDeadlineAndPersistTransient(List<Activity> activities, Date eventEndDate) {
        if(activities == null) return new ArrayList<>();
        
        List<Activity> verifiedActivities = new LinkedList<>();

        for (Activity activity : activities) {
            if (this.isValidActivity(activity, eventEndDate))
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

    private boolean isValidActivity(Activity activity, Date eventDeadline) {
        if (activity != null && activity.getName() != null && activity.getStart() != null
                && activity.getEnd() != null) {
            validateActivityDeadLine(activity, eventDeadline);
            return true;
        }
        return false;
    }

    private void validateActivityDeadLine(Activity activity, Date eventDeadline) {
        if (activity.getEnd().compareTo(eventDeadline) > 0) {
            throw new RuntimeException(
                    "Não é possível cadastrar Atividade com prazo marcado para após o fim do evento");
        }
    }

}
