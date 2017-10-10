package ch.ese.team6.models.orders;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
	List<Orders>findAll();
	
	//Users findByUsername(String username);

	//boolean existsByUsername(String username);
	
}
