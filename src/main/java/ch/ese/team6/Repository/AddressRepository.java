package ch.ese.team6.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.Address;


public interface AddressRepository extends JpaRepository<Address, Long>{
	List<Address>findAll();


	
}
