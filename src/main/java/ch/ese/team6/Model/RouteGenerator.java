package ch.ese.team6.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RouteGenerator implements IAutomaticRouteGenerator {
private ArrayList<Truck> trucks;
private ArrayList<IDelivarable> delivarables;
private AddressDistanceManager addressDistances;
private Address deposit;
private RouteCollection routes;
	@Override
	public void initialize(ArrayList<Truck> trucks, ArrayList<IDelivarable> items,
			AddressDistanceManager addressDistances, Address depositAddress) {
		assert trucks!=null && trucks.size() != 0;
		assert depositAddress != null;
		assert items!=null && items.size() !=0;
		assert addressDistances != null;
		this.trucks = trucks;
		this.delivarables = items;
		this.addressDistances = addressDistances;
		this.deposit = depositAddress;
		
		
		// We do 10 random cluster initalizations 
		int bestDistance = Integer.MAX_VALUE;
		RouteCollection bestSolution = null;
		for(int i = 0; i< 30;i++) {
			RouteCollection candidate= this.initializeRandom();
			if(candidate!= null && candidate.getDrivenDistance(addressDistances)<bestDistance) {
				bestSolution = candidate;
				bestDistance = candidate.getDrivenDistance(addressDistances);
			}
		}
		
		this.routes = bestSolution;
		

	}
	
	
	private RouteCollection initializeRandom(){
		// First step: Each route will consist of a random address.
		// We create as many routes as we have trucks
		RouteCollection candidate = new RouteCollection(trucks.size());
		ArrayList<Address> addresses = new ArrayList<Address>(getAllAddresses(delivarables));
		
		
		for(int i = 0; i< trucks.size();i++) {
			RouteTruckIDelivarable route = new RouteTruckIDelivarable(deposit);
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
			RouteTruckIDelivarable route = getNeighrestRoute(a,candidate,false,null);
			route.addDelivarables(getAllAtAddress(a, delivarables));
		}
		
		// Now all delivarables form part of a route. And there are as many routes as trucks
		// But we did not yet assign trucks to the routes.
		this.assignTrucks(candidate);
		
		//Now all delivarables are assigned to a route and all routes have a truck.
		// We now want to make sure that the capacity constraints are satisfied.
		//For each route where the capacity is not satisfied, we try to remove Objects.
		for(RouteTruckIDelivarable route: candidate) {
			if(!route.isCapacitySatified()) {
				boolean success = fix(route,candidate);
				if(!success) {
					return null;
				}
			}
		}
		
		return candidate;
	}

	//Tries to fix a Route which does not satisfy capcacity by moving the Elements away to other routes
	private boolean fix(RouteTruckIDelivarable route, RouteCollection candidate) {
		int n = route.getDelivarables().size();
		for(int i = n-1; i >= 0; i--) {
			IDelivarable delivarableToMove = route.getDelivarables().get(i);
			RouteTruckIDelivarable routeWhereDeliveryCanBeAdded = getNeighrestRoute(delivarableToMove.getAddress(), candidate, true, delivarableToMove);
			if(routeWhereDeliveryCanBeAdded!=null) {
				routeWhereDeliveryCanBeAdded.addIDelivarable((delivarableToMove));
				route.remove(delivarableToMove);
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
		assert candidate.size() == trucks_copy .size();
		for(RouteTruckIDelivarable route:candidate) {
			
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
	private RouteTruckIDelivarable getNeighrestRoute(Address a,RouteCollection routes, boolean checkCapacity,IDelivarable delivarableToPass) {
		
		RouteTruckIDelivarable nearestRoute = null;
		int distance = Integer.MAX_VALUE;
		for(RouteTruckIDelivarable route: routes) {
			int distanceToNearestAddressInRoute = addressDistances.getDistanceToNeighrestAddress(a, route.getAllAddress(true));
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
		routes.removeEmptyRoutes();
		return routes;
	}
	
	/**
	 * Returns a set with all the addresses where something must be delivered
	 */
	public static Set<Address> getAllAddresses(Iterable<IDelivarable> delivarables) {
		Set<Address> addresses = new HashSet<Address>();
		for (IDelivarable d : delivarables) {
			addresses.add(d.getAddress());
		}
		return addresses;
	}
	
	/**
	 * Returns an ArrayList of all the Delivarables with the address a.
	 * 
	 */
	public static ArrayList<IDelivarable> getAllAtAddress(Address a, ArrayList<IDelivarable> delivarables) {
		ArrayList<IDelivarable> ret = new ArrayList<IDelivarable>();
		for (IDelivarable d : delivarables) {
			if (d.getAddress().equals(a)) {
				ret.add(d);
			}
		}
		return ret;
	}




}
