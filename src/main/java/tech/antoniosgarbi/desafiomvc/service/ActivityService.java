package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafiomvc.model.Activity;
import tech.antoniosgarbi.desafiomvc.repository.ActivityRepository;

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
        if(activity.getDelivered() != null) {
            activity.getDelivered().forEach(d -> {
                if(d != null) this.deliveryService.save(d);
            });
        }
        return this.activityRepository.save(activity);
    }

    public List<Activity> findAll() {
        return this.activityRepository.findAll();
    }

    public void delete(Long id) {
        this.activityRepository.deleteById(id);
    }

  

}
