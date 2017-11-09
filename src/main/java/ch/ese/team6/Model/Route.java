package ch.ese.team6.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "route")
public class Route {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	private String routeDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="truck")
	private Truck truck;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="driver")
	private User driver;
	
	public long estimatedTime;
	public long measuredTime;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ADDRESS_ID")
	private Address deposit;//The starting address of the Route
	
	
	@OneToMany(fetch=FetchType.EAGER,cascade = {CascadeType.ALL})
	@JoinColumn(name="ROUTE_ID")
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
 	
	
	private boolean isSorted;
	private int drivenDistance;

	public Route() {
		
	}
	public Route(Address deposit) {
		this.deposit = deposit;
	}
		
	public Route(String date) {
		this.routeDate = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRouteDate() {
		return routeDate;
	}

	public void setRouteDate(String routeDate) {
		this.routeDate = routeDate;
	}

	public String getDate() {
		/*SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		return date.format(routeDate.getTime());*/
		return this.routeDate;
	}
		
	public Truck getTruck() {
		return truck;
	}

	public void setTruck(Truck truck) {
		this.truck = truck;
	}

	public User getDriver() {
		return driver;
	}

	public void setDriver(User driver) {
		this.driver = driver;
	}

	
	public void addDelivarables(ArrayList<IDelivarable> delivarables) {
			for(IDelivarable d: delivarables) {
				this.addDelivarable(d);
			}
	}

	/**
	 * Removes a Delivarable from the route
	 * @param delivarableToMove
	 */
	public void remove(IDelivarable delivarableToMove) {
		if(delivarableToMove.getClass().equals((Order.class))) {
			Order o = (Order)delivarableToMove;
			for(OrderItem oi: o.getOrderItems()) {
				this.remove(oi);
			}
		}else if(delivarableToMove.getClass().equals(OrderItem.class)){
			OrderItem oi = (OrderItem)delivarableToMove;
			this.remove(oi);
		}
	}
	
	private void remove(OrderItem orderItem) {
		assert this.orderItems.contains(orderItem);
		this.orderItems.remove(orderItem);
		orderItem.setRoute(null);
	}
	
	
	/**
	 * Can be used to add an Order or an OrderItem to a route.
	 * @param d
	 */
	public void addDelivarable(IDelivarable d) {
		d.setRoute(this);
		if(d.getClass().equals((Order.class))) {
			Order o = (Order)d;
			this.orderItems.addAll(o.getOrderItems());
		}else if(d.getClass().equals(OrderItem.class)){
			OrderItem oi = (OrderItem)d;
			this.orderItems.add(oi);
		}	
	}
	
	
	
	public boolean hasDeliveries() {
		return !this.orderItems.isEmpty();
		
	}
	
	public List<Delivery> getDeliveries(){
		Set<Address> addresses = this.getAllAddresses(false);
		List<Delivery> deliveries = new ArrayList<Delivery>(addresses.size());
		for(Address adress: addresses) {
			Delivery delivery = new Delivery(this);
			delivery.setAddress(adress);
			delivery.addItems((this.getAllAtAddress(adress)));
			deliveries.add(delivery);
		}
		return deliveries;
	}
	
	public int countDeliveries() {
		return this.getAllAddresses(false).size();
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
	public boolean isFull() {
		return !this.isCapacitySatified();
	}
	

	public int getSize() {
		int s = 0;
		for (IDelivarable d : orderItems) {
			s += d.getSize();
		}
		return s;
	}

	public int getWeight() {
		int w = 0;
		for (IDelivarable d : orderItems) {
			w += d.getWeight();
		}
		return w;
	}
	
	public int getDrivenDistance(AddressDistanceManager distanceManager) {
		this.sortRoute(distanceManager);
		return this.drivenDistance;
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
		List<OrderItem> sorted = new ArrayList<OrderItem>(orderItems.size());
		Address currentAddress = deposit;
		Address nextAddress;
		Set<Address> addresses = this.getAllAddresses(false);
		while (!addresses.isEmpty()) {
			nextAddress = distanceManager.getNeigherstAddress(currentAddress, addresses, false);
			addresses.remove(nextAddress);
			sorted.addAll(this.getAllAtAddress(nextAddress));
			drivenDistance+= distanceManager.getDistance(currentAddress, nextAddress);
			
			currentAddress = nextAddress;
		}
		
		drivenDistance+= distanceManager.getDistance(currentAddress, deposit);

		assert sorted.size() == orderItems.size();
		orderItems = sorted;
		this.isSorted = true;

	}

	

	public List<OrderItem> getOrderItems() {
		return this.orderItems;
	}

	
	public String toString() {
		String s = "Route using truck: "+this.truck+"\n";
		for(IDelivarable i: orderItems) {
			s = s+"  "+i.getAddress()+"\n";
		}
		s = s+"Size: "+getSize()+"/"+truck.getMaxCargoSpace()+", Weight: "+getWeight()+"/"+truck.getMaxLoadCapacity();
		return s;
	}
	
	
	public String calculateCapacity() {
		int cargoSpace = this.getTruck().getMaxCargoSpace();
		 return this.getSize()+ "/ " +cargoSpace; 
	}
	
	
	
	

	public long getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(long estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public long getMeasuredTime() {
		return measuredTime;
	}

	public void setMeasuredTime(long measuredTime) {
		this.measuredTime = measuredTime;
	}
	
	/**
	 * Returns a set with all the addresses where something must be delivered
	 */
	public Set<Address> getAllAddresses(boolean includeDeposit) {
		Set<Address> addresses = new HashSet<Address>();
		if(includeDeposit) {
			addresses.add(this.deposit);
		}
		
		for (OrderItem oi : this.orderItems) {
			addresses.add(oi.getAddress());
		}
		return addresses;
	}
	
	/**
	 *
	 * Returns an ArrayList of all the Delivarables with the address a.
	 * 
	 */
	private  List<OrderItem> getAllAtAddress(Address a) {
		ArrayList<OrderItem> ret = new ArrayList<OrderItem>();
		for (OrderItem oi : this.orderItems) {
			if (oi.getAddress().equals(a)) {
				ret.add(oi);
			}
		}
		return ret;
	}

		

}
