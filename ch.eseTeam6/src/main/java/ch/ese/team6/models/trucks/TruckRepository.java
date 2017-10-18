package ch.ese.team6.models.trucks;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TruckRepository  extends JpaRepository<Trucks, Long>{

	List<Trucks>findAll();	
	
	Trucks findById(long id);
	
}
