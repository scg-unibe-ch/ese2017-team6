package ch.ese.team6.models.orderitems;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.models.orderitems.OrderItems;


public interface OrderItemRepository  extends JpaRepository<OrderItems, Long>{

	List<OrderItems>findAll();
	
	OrderItems findByorderItemStatus(String status);

	boolean existsByorderItemStatus(String status);
}
