package ch.ese.team6.Service;

import java.util.ArrayList;

import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.AddressDistanceManager;
import ch.ese.team6.Model.IDelivarable;
import ch.ese.team6.Model.RouteCollection;
import ch.ese.team6.Model.Truck;

/**
 * 
 * Automatic Route generator is initialized with a List of available trucks and
 * a List of the orderItems that have to be delivered. getRoutes will return an
 * ArrayList of Routes if a solution was found. If no solution is found,
 * getRoutes will return null
 *
 */
public interface IAutomaticRouteGenerator {
	public void initialize(RouteGenerationProblem routeProblem);

	/*
	 * getRoutes will return an ArrayList of Routes if a solution was found. If no
	 * solution is found, getRoutes will return null
	 * 
	 */
	public RouteCollection getRoutes();
}
