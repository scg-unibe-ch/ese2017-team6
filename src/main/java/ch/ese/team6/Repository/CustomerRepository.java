package ch.ese.team6.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Long>{
	List<Customer>findAll();
	
	Customer findByName(String name);

	boolean existsByName(String name);
}
