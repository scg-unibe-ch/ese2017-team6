package ch.ese.team6.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
private ArrayList<Truck> trucks;
private ArrayList<IDelivarable> delivarables;
private AddressDistanceManager addressDistances;
private Address deposit;
private RouteCollection routes;
private ArrayList<User> drivers;
private Date deliveryDate;
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
		
		// We do 10 random cluster initalizations 
		int bestTime = Integer.MAX_VALUE;
		RouteCollection bestSolution = null;
		for(int i = 0; i< 30;i++) {
			RouteCollection candidate= this.initializeRandom();
			if(candidate!= null && candidate.getEstimatedTime()<bestTime) {
				bestSolution = candidate;
				bestTime = candidate.getEstimatedTime();
			}
		}
		
		
		
		/*
		 * Since we move the Items from one order to another at the end we have to make
		 * sure each orderItem stores the right Route
		 */
		
		if(bestSolution==null) {
			for(IDelivarable o: this.delivarables) {
				o.setRoute(null);
			}
		}else {

			bestSolution.removeEmptyRoutes();
			
			for(Route r: bestSolution.getRoutes()) {
				for(OrderItem o: r.getOrderItems()) {
					o.setRoute(r);
				}
			}
		}
		
		this.routes = bestSolution;
		

	}
	
	
	private RouteCollection initializeRandom(){
		// First step: Each route will consist of a random address.
		// We create as many routes as we have trucks
		int maxRoutes = Math.min(trucks.size(), drivers.size());
		RouteCollection candidate = new RouteCollection(maxRoutes);
		ArrayList<Address> addresses = new ArrayList<Address>(getAllAddresses(delivarables));
		for(IDelivarable o: this.delivarables) {
			o.setRoute(null);
		}
		
		
		for(int i = 0; i< maxRoutes;i++) {
			Route route = new Route(deliveryDate,deposit);
			
			
			// Each route will start at a different address.
			if(!addresses.isEmpty()) {
				int randomNumber = (int) Math.floor(Math.random()*addresses.size());
				Address randomAddress = addresses.get(randomNumber%addresses.size());
				route.addDelivarables(getAllAtAddress(randomAddress,delivarables));
				addresses.remove(randomAddress);
			}
				
			candidate.add(route);		
		}
		
		
		// Our candidate now has several routes but it is not guaranteed that each delivarable forms part of a route
		// We now add the delivarables to the neighrest route
		while(!addresses.isEmpty()) {
			Address a = addresses.get(0);
			addresses.remove(0);
			Route route = getNeighrestRoute(a,candidate,false,null);
			route.addDelivarables(getAllAtAddress(a, delivarables));
		}
		
		// Now all delivarables form part of a route. And there are as many routes as trucks
		// But we did not yet assign trucks to the routes.
		this.assignTrucks(candidate);
		this.assignDivers(candidate);
		
		//Now all delivarables are assigned to a route and all routes have a truck.
		// We now want to make sure that the capacity constraints are satisfied.
		//For each route where the capacity is not satisfied, we try to remove Objects.
		for(Route route: candidate.getRoutes()) {
			if(!route.isCapacitySatified()) {
				boolean success = fix(route,candidate);
				if(!success) {
					return null;
				}
			}
		}
		
		return candidate;
	}

	private void assignDivers(RouteCollection candidate) {

		
		assert candidate.size() <= drivers .size();
		int i = 0;
		for(Route route:candidate.getRoutes()) {
			route.setDriver(drivers.get(i));
			i++;
		}
	}


	//Tries to fix a Route which does not satisfy capcacity by moving the Elements away to other routes
	private boolean fix(Route route, RouteCollection candidate) {
		int n = route.getOrderItems().size();
		for(int i = n-1; i >= 0; i--) {
			IDelivarable delivarableToMove = route.getOrderItems().get(i);
			Route routeWhereDeliveryCanBeAdded = getNeighrestRoute(delivarableToMove.getAddress(), candidate, true, delivarableToMove);
			if(routeWhereDeliveryCanBeAdded!=null) {
				route.remove(delivarableToMove);
				routeWhereDeliveryCanBeAdded.addDelivarable((delivarableToMove));
				
				if(route.isCapacitySatified()) {
					return true;
				}
			}
		}
		
		return route.isCapacitySatified();
	}


	/**
	 * We have a RouteCollection candidate which has the same number of Routes as trucks we have
	 * Now we need to assign Trucks to the routes
	 * We proceed as follows: We assign a truck to the route which has the most similar size and weight as the truck
	 * @param candidate
	 */
	private void assignTrucks(RouteCollection candidate) {
		ArrayList<Truck> trucks_copy = (ArrayList<Truck>) trucks.clone();
		
		assert candidate.size() <= trucks_copy.size();
		for(Route route:candidate.getRoutes()) {
			
			Truck truckCandidate = trucks_copy.get(0);
			int bestCandidateD = getDistance(truckCandidate.getMaxCargoSpace()-route.getSize(),truckCandidate.getMaxLoadCapacity()-route.getWeight());
			for(Truck t:trucks_copy) {
				int temp =getDistance(t.getMaxCargoSpace()-route.getSize(), t.getMaxLoadCapacity()-route.getWeight());
				if(temp<bestCandidateD) {
					bestCandidateD = temp;
					truckCandidate = t;
					
				}
			}
			
			route.setTruck(truckCandidate);
			trucks_copy.remove(truckCandidate);
			
		}
	}


	private int getDistance(int i, int j) {
		return Math.abs(i)+Math.abs(j);
	}


	/**
	 * Searches in routes for the route which is the nearest to the Address a
	 * if checkCapacity only routes are returned which have enough capacity to take the delivarableToPass
	 * @param d
	 * @param routes
	 * @return
	 */
	private Route getNeighrestRoute(Address a,RouteCollection routes, boolean checkCapacity,IDelivarable delivarableToPass) {
		
		Route nearestRoute = null;
		int distance = Integer.MAX_VALUE;
		for(Route route: routes.getRoutes()) {
			int distanceToNearestAddressInRoute = addressDistances.getDistanceToNeighrestAddress(a, route.getAllAddresses(true,false));
			if(distanceToNearestAddressInRoute<distance) {
				if(!checkCapacity || (checkCapacity && route.doesIDelivarableFit(delivarableToPass))) {
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
	
	/**
	 * Returns a set with all the addresses where something must be delivered
	 */
	public static Set<Address> getAllAddresses(List<IDelivarable> delivarables) {
		Set<Address> addresses = new HashSet<Address>();
		for (IDelivarable d : delivarables) {
			addresses.add(d.getAddress());
		}
		return addresses;
	}
	
	/**
	 * Why is it static?
	 * We want to use it also for Route Generation purposes. In the route generation problem we have
	 * a set of delivarables and we want to get all with the same address even if they do not yet belong to a route.
	 * Returns an ArrayList of all the Delivarables with the address a.
	 * 
	 */
	public static ArrayList<IDelivarable> getAllAtAddress(Address a, List<IDelivarable> delivarables) {
		ArrayList<IDelivarable> ret = new ArrayList<IDelivarable>();
		for (IDelivarable d : delivarables) {
			if (d.getAddress().equals(a)) {
				ret.add(d);
			}
		}
		return ret;
	}




}
