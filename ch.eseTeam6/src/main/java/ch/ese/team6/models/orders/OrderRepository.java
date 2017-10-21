package ch.ese.team6.models.orders;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.models.customers.Customer;

public interface OrderRepository extends JpaRepository<Orders, Long> {
	List<Orders>findAll();
	
	Orders findByCustomer(Customer customer);
	
}
