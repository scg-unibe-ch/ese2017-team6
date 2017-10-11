package ch.ese.team6.models.customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
	List<Customer> findAll();

	boolean existsByUsername(String username);

}
