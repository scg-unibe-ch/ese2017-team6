package ch.ese.team6.models.routes;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.models.users.Users;


public interface RouteRepository  extends JpaRepository<Routes, Long>{

	List<Routes>findAll();
	
	List<Routes>findByDriver(Users users);
	 
	long findVehicleIdByRouteDate(java.util.Date date);
	
	long findDriverByRouteDate(java.util.Date date);
}
