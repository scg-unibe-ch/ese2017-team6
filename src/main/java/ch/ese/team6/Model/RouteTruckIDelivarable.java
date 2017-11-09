package ch.ese.team6.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A route consists of a truck, a date and several OrderItems stored in a
 * ArrayList
 *
 */
public class RouteTruckIDelivarable {
	private Address deposit;//The starting address of the Route
	private ArrayList<IDelivarable> delivarables;
	private Truck truck;
	private boolean isSorted;
	private int drivenDistance;

	public RouteTruckIDelivarable(Address deposit) {
		this.deposit = deposit;
		this.delivarables = new ArrayList<IDelivarable>();
	}

	public void addIDelivarable(IDelivarable d) {
		assert d != null;
		this.delivarables.add(d);
		isSorted = false;
	}
	
	public void addDelivarables(Collection<IDelivarable> delivarables) {
		assert delivarables!=null;
		this.delivarables.addAll(delivarables);
		isSorted = false;
	}

	/**
	 * Return true if the Delivarable d may be added to the route without violating
	 * size or weight constraints.
	 * 
	 * @param d
	 * @return
	 */
	public boolean doesIDelivarableFit(IDelivarable d) {
		if (truck == null) {
			return false;
		}
		if (d.getSize() <= (truck.getMaxCargoSpace() - this.getSize())) {
			if (d.getWeight() <= (truck.getMaxLoadCapacity() - this.getWeight())) {
				return true;
			}
		}

		return false;
	}
	
	public boolean isCapacitySatified() {
		if(truck == null) {
			return false;
		}
		return (getSize()<=truck.getMaxCargoSpace()) &&(getWeight()<= truck.getMaxLoadCapacity());
	}
	

	public int getSize() {
		int s = 0;
		for (IDelivarable d : delivarables) {
			s += d.getSize();
		}
		return s;
	}

	public int getWeight() {
		int w = 0;
		for (IDelivarable d : delivarables) {
			w += d.getWeight();
		}
		return w;
	}
	
	public int getDrivenDistance(AddressDistanceManager distanceManager) {
		this.sortRoute(distanceManager);
		return this.drivenDistance;
	}
	
	public Set<Address> getAllAddress(boolean includeDeposit){
		Set<Address> addresses =  RouteGenerator.getAllAddresses(delivarables);
		if(includeDeposit) {
			addresses.add(this.deposit);
		}
		return addresses;
	}

	/**
	 * We can add the delivarables to the Route in the order we want sortRoute will
	 * then give the (nearly) shortest path. It will sort the delivarables such that
	 * the trucker should start by the first delivarable and then go to the second
	 * etc. Note that finding a shortes path going through all delivarables is a
	 * typical NP hard problem. Here we use the following heuristics: Start at the
	 * deposit: go to the nearest delivery, From the nearest delivery go to the next
	 * one etc.
	 * does also update the drivenDistance
	 */
	public void sortRoute(AddressDistanceManager distanceManager) {
		if(isSorted) {
			return;
		}
		
		this.drivenDistance = 0;
		ArrayList<IDelivarable> sorted = new ArrayList<IDelivarable>(delivarables.size());
		Address currentAddress = deposit;
		Address nextAddress;
		Set<Address> addresses = RouteGenerator.getAllAddresses(this.delivarables);
		while (!addresses.isEmpty()) {
			nextAddress = distanceManager.getNeigherstAddress(currentAddress, addresses, false);
			addresses.remove(nextAddress);
			sorted.addAll(RouteGenerator.getAllAtAddress(nextAddress,delivarables));
			drivenDistance+= distanceManager.getDistance(currentAddress, nextAddress);
			
			currentAddress = nextAddress;
		}
		
		drivenDistance+= distanceManager.getDistance(currentAddress, deposit);

		assert sorted.size() == delivarables.size();
		delivarables = sorted;
		this.isSorted = true;

	}

	public void setTruck(Truck truck) {
		this.truck = truck;
	}

	public ArrayList<IDelivarable> getDelivarables() {
		return this.delivarables;
	}

	public void remove(IDelivarable delivarableToMove) {
		assert this.delivarables.contains(delivarableToMove);
		this.delivarables.remove(delivarableToMove);
		isSorted = false;
	}
	
	public String toString() {
		String s = "Route using truck: "+this.truck+"\n";
		for(IDelivarable i: delivarables) {
			s = s+"  "+i.getAddress()+"\n";
		}
		s = s+"Size: "+getSize()+"/"+truck.getMaxCargoSpace()+", Weight: "+getWeight()+"/"+truck.getMaxLoadCapacity();
		return s;
	}
	
	

	}
