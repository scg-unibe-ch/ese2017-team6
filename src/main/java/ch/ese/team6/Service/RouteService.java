package ch.ese.team6.Service;

import java.util.List;

import ch.ese.team6.Model.IDelivarable;
import ch.ese.team6.Model.Route;

public interface RouteService {

	int calculateLeftCapacity(Route route);
	List<Route> selectWithLeftCapacity();
	List<Route> selectWithLeftCapacity(IDelivarable o);
}
