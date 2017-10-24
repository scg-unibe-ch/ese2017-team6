package ch.ese.team6.models.Deliveries;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.models.Deliveries.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long>{

	List<Delivery>findAll();
	
}
