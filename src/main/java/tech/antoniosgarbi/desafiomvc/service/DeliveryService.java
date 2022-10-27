package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafiomvc.model.Delivery;
import tech.antoniosgarbi.desafiomvc.repository.DeliveryRepository;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public Delivery save(Delivery delivery) {
        if(!delivery.getWasDelivered()) {
            delivery.setLate(false);
        }
        return this.deliveryRepository.save(delivery);
    }

}
