package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafiomvc.model.Activity;
import tech.antoniosgarbi.desafiomvc.repository.ActivityRepository;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final DeliveryService deliveryService;

    public ActivityService(ActivityRepository activityRepository, DeliveryService deliveryService) {
        this.activityRepository = activityRepository;
        this.deliveryService = deliveryService;
    }

    public Activity findById(Long id) {
        return this.activityRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
    }

    public Activity save(Activity activity) {
        if (activity.getDelivered() != null) {
            activity.getDelivered().forEach(d -> {
                if (d != null) {
                    this.deliveryService.save(d);
                }
            });
        }
        return this.activityRepository.save(activity);
    }

    public List<Activity> checkAtivityDeadlineAndPersistTransient(List<Activity> activities, Date eventEndDate) {
        List<Activity> verifiedActivities = new LinkedList<>();

        for (Activity activity : activities) {
            if (this.isValidActivity(activity, eventEndDate)) verifiedActivities.add(this.save(activity));
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
        if (activity != null && activity.getName() != null && activity.getStart() != null && activity.getEnd() != null) {
            validateActivityDeadLine(activity, eventDeadline);
            return true;
        }
        return false;
    }

    private void validateActivityDeadLine(Activity activity, Date eventDeadline) {
        if (activity.getEnd().compareTo(eventDeadline) > 0) {
            throw new RuntimeException("Não é possível cadastrar Atividade com prazo marcado para após o fim do evento");
        }
    }


}
