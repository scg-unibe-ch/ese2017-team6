package ch.ese.team6.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.Route;
import ch.ese.team6.Model.User;

public interface RouteRepository extends JpaRepository<Route, Long>{
	
	User findByDriverId(long driver_id);

}
