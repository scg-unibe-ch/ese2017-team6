package ch.ese.team6.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.ese.team6.Model.Route;

public interface RouteRepository extends JpaRepository<Route, Long>{
	List<Route> findByRouteDate(Date routeDate);
}
