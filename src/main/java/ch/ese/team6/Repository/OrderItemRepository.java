package ch.ese.team6.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.OrderItem;

public interface OrderItemRepository  extends JpaRepository<OrderItem, Long>{

	List<OrderItem>findAll();
	
	OrderItem findByOrderItemStatus(String status);

	boolean existsByorderItemStatus(String status);
}