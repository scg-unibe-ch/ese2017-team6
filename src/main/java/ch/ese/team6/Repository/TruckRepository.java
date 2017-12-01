package ch.ese.team6.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.Truck;


public interface TruckRepository  extends JpaRepository<Truck, Long>{
	@Override
	List<Truck>findAll();	
	Truck findById(long id);
	boolean existsByTruckname(String truckname);
	
}