package ch.ese.team6.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.Route;
import ch.ese.team6.Model.User;

public interface RouteRepository extends JpaRepository<Route, Long>{
	
	List<Route> findByRouteDate(String routeDate);
	
	User findByDriverId(long driver_id);

}
