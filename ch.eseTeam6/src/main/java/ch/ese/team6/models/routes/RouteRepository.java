package ch.ese.team6.models.routes;



import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ch.ese.team6.models.users.Users;


public interface RouteRepository  extends JpaRepository<Routes, Long>{

	List<Routes>findAll();
	
	List<Routes>findByDriver(Users users);
	 
	long findVehicleIdByRouteDate(Date date);
	
	long findDriverByRouteDate(Date date);
}
