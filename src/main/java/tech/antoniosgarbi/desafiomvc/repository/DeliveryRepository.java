package tech.antoniosgarbi.desafiomvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.antoniosgarbi.desafiomvc.model.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

}