package ch.ese.team6.models.routes;



import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RouteRepository  extends JpaRepository<Routes, Long>{

	List<Routes>findAll();
	
	List<Routes>findByDriverId(long driverId);
	
	long findVehicleIdByRouteDate(Date date);
	
	long findDriverByRouteDate(Date date);
}
