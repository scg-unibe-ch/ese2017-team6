package ch.ese.team6.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.OrderItem;

public interface OrderItemRepository  extends JpaRepository<OrderItem, Long>{

	@Override
	List<OrderItem>findAll();
	
	
	List<OrderItem> findByRouteId(long routeid);
	

	boolean existsByorderItemStatus(String status);
}
