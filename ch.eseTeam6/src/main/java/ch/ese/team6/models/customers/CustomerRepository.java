package ch.ese.team6.models.customers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long>{
	List<Customer>findAll();
	
	Customer findByname(String name);

	boolean existsByname(String name);
}
