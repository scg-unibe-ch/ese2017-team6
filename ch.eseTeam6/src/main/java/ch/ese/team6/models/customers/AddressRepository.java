package ch.ese.team6.models.customers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long>{
	List<Address>findAll();


	
}
