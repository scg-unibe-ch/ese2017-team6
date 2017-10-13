package ch.ese.team6.models.clients;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Client, Long>{
	List<Client>findAll();
	
	Client findByname(String name);

	boolean existsByname(String name);
}
