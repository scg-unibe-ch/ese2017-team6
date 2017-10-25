package ch.ese.team6.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.Customer;
import ch.ese.team6.Model.Order;



public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order>findAll();
	
	Order findByCustomer(Customer customer);
	
}
