package ch.ese.team6.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.AddressDistanceManager;
import ch.ese.team6.Model.IDelivarable;
import ch.ese.team6.Model.OrderItem;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Model.RouteCollection;
import ch.ese.team6.Model.Truck;
import ch.ese.team6.Model.User;

public class RouteGenerator implements IAutomaticRouteGenerator {
	private List<Truck> trucks;
	private ArrayList<IDelivarable> delivarables;
	private AddressDistanceManager addressDistances;
	private Address deposit;
	private RouteCollection routes;
	private List<User> drivers;
	private Date deliveryDate;
	private Random random;

	@Override
	public void initialize(RouteGenerationProblem routeProblem) {
		assert routeProblem.isOK();
		assert routeProblem.theoreticallySolvable();

		this.trucks = routeProblem.getTrucks();
		this.delivarables = routeProblem.getOrders();
		this.addressDistances = routeProblem.getAddressManager();
		this.deposit = routeProblem.getDepositAddress();
		this.drivers = routeProblem.getDrivers();
		this.deliveryDate = routeProblem.getDeliveryDate();
		random = new Random(2017);
		// We do 30 random cluster initalizations
		int bestTime = Integer.MAX_VALUE;
		RouteCollection bestSolution = null;
		for (int i = 0; i < 30; i++) {
			RouteCollection candidate = this.initializeRandom();
			if (candidate != null && candidate.getEstimatedTime() < bestTime) {
				bestSolution = candidate;
				bestTime = candidate.getEstimatedTime();
			}
		}

		/*
		 * Since we move the Items from one order to another at the end we have to make
		 * sure each orderItem stores the right Route
		 */

		if (bestSolution == null) {
			for (IDelivarable o : this.delivarables) {
				o.setRoute(null);
			}
		} else {

			bestSolution.removeEmptyRoutes();

			for (Route r : bestSolution.getRoutes()) {
				for (OrderItem o : r.getOrderItems()) {
					o.setRoute(r);
				}
			}
		}

		this.routes = bestSolution;

	}

	/**
	 * 
	 * @return
	 */
	private RouteCollection initializeRandom() {
		// First step: Each route will consist of a random address.
		// We create as many routes as we have trucks
		int maxRoutes = Math.min(trucks.size(), drivers.size());
		RouteCollection candidate = new RouteCollection(maxRoutes);

		for (int i = 0; i < maxRoutes; i++) {
			Route route = new Route(deliveryDate, deposit);

			candidate.add(route);
		}
		this.assignTrucksAndDrivers(candidate);

		Collections.shuffle(delivarables, this.random);
		for (IDelivarable order : delivarables) {
			Route route = this.getNeighrestRoute(order.getAddress(), candidate, true, order);
			if (route == null) {
				// the order does not fit any route
				return null;
			}
			route.addDelivarable(order);
		}

		return candidate;
	}

	/**
	 * Randomly assigns trucks and drivers to the routes
	 */
	private void assignTrucksAndDrivers(RouteCollection candidate) {

		Collections.shuffle(trucks, random);
		Collections.shuffle(drivers, random);
		int i = 0;
		for (Route route : candidate.getRoutes()) {
			route.setDriver(drivers.get(i));
			route.setTruck(trucks.get(i));
			i++;
		}

	}

	/**
	 * Searches in routes for the route which is the nearest to the Address a if
	 * checkCapacity only routes are returned which have enough capacity to take the
	 * delivarableToPass if no route is found null is returned.
	 * 
	 * @param d
	 * @param routes
	 * @return
	 */
	private Route getNeighrestRoute(Address a, RouteCollection routes, boolean checkCapacity,
			IDelivarable delivarableToPass) {

		Route nearestRoute = null;
		int distance = Integer.MAX_VALUE;
		for (Route route : routes.getRoutes()) {
			int distanceToNearestAddressInRoute = addressDistances.getDistanceToNeighrestAddress(a,
					route.getAllAddresses(true, false));
			if (distanceToNearestAddressInRoute < distance) {
				if (!checkCapacity || (checkCapacity && route.doesIDelivarableFit(delivarableToPass))) {
					nearestRoute = route;
				}

			}
		}
		return nearestRoute;

	}

	@Override
	/**
	 * Will be null if no legal solution was found
	 */
	public RouteCollection getRoutes() {
		return routes;
	}

}
