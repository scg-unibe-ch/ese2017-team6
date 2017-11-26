package ch.ese.team6.Model;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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

import org.springframework.format.annotation.DateTimeFormat;

import ch.ese.team6.Exception.InconsistentOrderStateException;
import ch.ese.team6.Exception.RouteTimeException;
import ch.ese.team6.Service.CalendarService;
import ch.ese.team6.Service.OurCompany;

@Entity
@Table(name = "route")
public class Route {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date routeStartDate;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="truck")
	private Truck truck;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="driver")
	private User driver;
	
	public long estimatedTime;
	public long measuredTime;
	private long routeStart = 0;
	private long routeStop = 0;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ADDRESS_ID")
	private Address deposit;//The starting address of the Route
	
	
	@OneToMany(fetch=FetchType.EAGER,cascade = {CascadeType.ALL})
	@JoinColumn(name="ROUTE_ID")
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
 	
	
	public Route() {
		
	}
	


	public Route(Date date,Address deposit) {
		this.deposit = deposit;
		this.setRouteStartDate(date);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getRouteStartDate() {
		return routeStartDate;
	}

	/**
	 * The route will start at date but at the working Start hour
	 * specified in {@link OurCompany.java}
	 * @param date
	 */
	public void setRouteStartDate(Date date) {
	 	this.routeStartDate = CalendarService.getWorkingStart(date);
	    
	}
	
	
	public Address getDeposit() {
		return this.deposit;
				
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
	 * The delivarable will be added to the route
	 * idependently of wheter it fits or not
	 * You should check if it fits using
	 * doesIDelivarableFit
	 * @param d
	 */
	public void addDelivarable(IDelivarable d) {
		d.setRoute(this);
		if(d.getClass().equals((Order.class))) {
			Order o = (Order)d;
			this.orderItems.addAll(o.getOpenOrderItems());
		}else if(d.getClass().equals(OrderItem.class)){
			OrderItem oi = (OrderItem)d;
			this.orderItems.add(oi);
		}	
	}
	
	
	
	public boolean hasDeliveries() {
		return !this.orderItems.isEmpty();
		
	}

	public List<Delivery> getOpenDeliveries(){
		return this.getDeliveries(true);
	}
	

	/**
	 * returns a list of all the deliveries (delivered or not)
	 * @return
	 */
	public List<Delivery> getAllDeliveries(){
		return this.getDeliveries(false);
	}
	/**
	 * Return the open deliveries of the route
	 * if onlyOpen is set you will only get deliveries where there are open Items.
	 * @return
	 */
	private List<Delivery> getDeliveries(boolean onlyOpen){
		
		Set<Address> addresses = this.getAllAddresses(false,onlyOpen);
		List<Delivery> deliveries = new ArrayList<Delivery>(addresses.size());
		Address lastAddress = deposit;
		
		
		for(Address address: addresses) {		
			Delivery delivery = new Delivery(this);
			delivery.setAddress(address);
			delivery.addItems((this.getAllAtAddress(address)));
			delivery.setDistanceFromPreviousDelivery(lastAddress.getDistance(address));
			deliveries.add(delivery);
			lastAddress = address;
		}
		if(!deliveries.isEmpty()){
			Delivery lastDelivery = deliveries.get(deliveries.size()-1);
			lastDelivery.setDistanceToNextDelivery(lastAddress.getDistance(deposit));		
		}
		return deliveries;
	}
	
	
	public int countDeliveries() {
		return this.getAllAddresses(false,false).size();
	}
	
	
	/**
	 * Return true if the Delivarable d may be added to the route without violating
	 * size or weight constraints and without 
	 * extending the route so much time, that it ends after the next 
	 * route of either the driver or the truck starts.
	 * @param d
	 * @return
	 */
	public boolean doesIDelivarableFit(IDelivarable d) {
		if (truck == null || driver==null) {
			return false;
		}
		if (d.getOpenSize() <= (truck.getMaxCargoSpace() - this.getSize())) {
			if (d.getOpenWeight() <= (truck.getMaxLoadCapacity() - this.getWeight())) {
				if(this.statisfiesSchedule(d)) {
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * Returns true if and only if when we add the Delivarable d
	 * to the route we do not extend the route time in a way
	 * that the route ends after the starting point of the next route of the driver
	 * or after the starting point of the next route of the truck
	 * @param d
	 * @return
	 */
	public boolean statisfiesSchedule(IDelivarable d) {
		
		long additionalTimeNeededMillis = this.getAdditionalTime(d)*60l*1000l;
		Date currentEndOfThisRoute = this.getRouteEndDate();
		Date newEndOfThisRoute = CalendarService.computeTaskEnd(currentEndOfThisRoute, additionalTimeNeededMillis);
		
		
		Route nextRouteTruck = truck.nextRoute(currentEndOfThisRoute);
		if(nextRouteTruck!=null) {
			if(nextRouteTruck.getRouteStartDate().before(newEndOfThisRoute)) {
				return false;
			}
		}
		
		/** Checks if it is ok for the Driver
		 * 
		 */
		Route nextRouteDriver =driver.nextRoute(currentEndOfThisRoute);
		if(nextRouteDriver!=null) {
			if(nextRouteDriver.getRouteStartDate().before(newEndOfThisRoute)) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isCapacitySatified() {
		if(truck == null) {
			return false;
		}
		return (getSize()<=truck.getMaxCargoSpace()) &&(getWeight()<= truck.getMaxLoadCapacity());
	}
	public boolean isFull() {
		return (getSize()>=truck.getMaxCargoSpace()) &&(getWeight()>= truck.getMaxLoadCapacity());
	
	}
	
	/**
	 * Returns the Size of the all the order Items (open, rejected or delivered) in the route. 
	 * @return
	 */
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
	
	/**
	 * Returns the estimated time in minutes
	 * The route is sorted, ie. the deliveries are ordered in a way that the distance is optimal
	 * @return
	 */
	public long getEstimatedTime() {
		
		AddressDistanceManager am = new AddressDistanceManager();
		this.estimatedTime= am.estimateRouteTime(this.getAllAddressesUnsortedWithoutDeposit(), deposit);
		return estimatedTime;
	}
	
	

	

	public ArrayList<OrderItem> getOrderItems() {
		return (ArrayList<OrderItem>) this.orderItems;
	}

	
	public String toString() {
		String s = "Route "+id+" ("+this.driver+"/"+this.truck+")";
		return s;
	}
	
	
	public String calculateCapacitySize() {
		int cargoSpace = this.getTruck().getMaxCargoSpace();
		 return this.getSize()+ "/ " +cargoSpace; 
	}
	
	public String calculateCapacityWeight() {
		int cargoSpace = this.getTruck().getMaxLoadCapacity();
		 return this.getWeight()+ "/ " +cargoSpace; 
	}
	
	
	/**
	 * Returns the addtional driving time in minutes it takes to include the Order o in this route
	 * For computing the additional time, the optimal order of the deliveries is computed
	 * @param o
	 * @return
	 */
	public long getAdditionalTime(IDelivarable o) {
		
		long actualRouteTime = this.getEstimatedTime();
	
		
		Address additionalAddress = o.getAddress();
		Set<Address> currentAddresses = this.getAllAddressesUnsortedWithoutDeposit();
		currentAddresses.add(additionalAddress);
		
		AddressDistanceManager am = new AddressDistanceManager();
		long newRouteTime = am.estimateRouteTime(currentAddresses, deposit);
		
		return Math.max(newRouteTime- actualRouteTime,0l);
		
	}
	
	public String getAdditionalTimeStr(IDelivarable o) {
		return CalendarService.formatMinutes((int)this.getAdditionalTime(o));
	}
	
	/**
	 * Return the Date where the route ends (i. e. the latest Date where the route uses the truck and the driver
	 * are occupied.
	 * This takes into account the working Days specified in {@link OurCompany.java}
	 * @return
	 */

	public Date getRouteEndDate() {
		long estimatedTimeMillis = this.getEstimatedTime()*60l*1000l;
		
		return CalendarService.computeTaskEnd(routeStartDate, estimatedTimeMillis);
		
	}
	
	public String getEndDateStr() {
		return CalendarService.formatH(this.getRouteEndDate());
	}
	public String getStartDateStr() {
		return CalendarService.formatH(this.getRouteStartDate());
	}
	public String getEstimatedTimeStr() {
		return CalendarService.formatMinutes(this.getEstimatedTime());
	}



	public long getMeasuredTime() {
		return measuredTime;
	}

	public void setMeasuredTime(long measuredTime) {
		this.measuredTime = measuredTime;
	}
	
	/**

	 * Returns a set with all the addresses where something must still be delivered
	 */
	public Set<Address> getAllOpenAddresses(boolean includeDeposit) {
		return getAllAddresses(includeDeposit,true);
		}
	
	/**
	 * Returns all addresses (unsorted, i.e not in the optimal ordering) 
	 * Does not include the deposit (except if the route is supposed to
	 * deliver something at the deposit Address)
	 * @return
	 */
	private Set<Address> getAllAddressesUnsortedWithoutDeposit(){
		Set<Address> addresses = new HashSet<Address>();
		for (OrderItem oi : this.orderItems) {
			addresses.add(oi.getAddress());	
		}
		return addresses;
	}
	/**
	 * Returns a set with all the addresses of this route (already delivered or not)
	 * The addresses are sorted in the optimal order, such that the time to 
	 * drive the route is minimal
	 * 
	 * Returns a set with all the addresses where something must be delivered.
	 * If you pass onlyOpen you will only get the addresses where there are items not yet delivered.
	 * @param onlyOpen 
	 */
	public Set<Address> getAllAddresses(boolean includeDeposit, boolean onlyOpen) {
		Set<Address> addresses = this.getAllAddressesUnsortedWithoutDeposit();
		
		AddressDistanceManager am = new AddressDistanceManager();
		//optimal order of the addresses, does allways include the deposit
		LinkedHashSet<Address> addressesSorted = am.optimalRoute(addresses, deposit);
		
		//remove the deposit if not wanted
		if(!includeDeposit) {
			addressesSorted.remove(this.deposit);
		}
		
		// remove the addresses where nothing must be delivered
		if(onlyOpen) {
			ArrayList<Address> addressesToRemove = new ArrayList<Address>();
			for(Address ad: addressesSorted) {
				
				List<OrderItem> orderItemsAtAddress = getAllAtAddress(ad);
				boolean allItemsClosed = true;
				
				// check if there are Open orderItems
				for(OrderItem oi: orderItemsAtAddress) {
					if(oi.getOrderItemStatus().equals(OrderStatus.SCHEDULED)) {
						allItemsClosed = false;
						break;
					}
				}
				
				if(allItemsClosed) {
					if(!ad.equals(deposit)) {
						addressesToRemove.add(ad);
					}
				}
			}
			addressesSorted.removeAll(addressesToRemove);
		}
		
			
		return addressesSorted;
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
	
	public void startRoute() throws RouteTimeException {
		if(this.isRouteStarted()) throw new RouteTimeException ("Route has already started.");
		if(this.isRouteFinished()) throw new RouteTimeException ("Route has already finished."); 
		this.routeStart = System.currentTimeMillis();
	}
	
	public boolean isRouteStarted() {
		return (this.routeStart != 0  && this.routeStop == 0);
	}
	
	public boolean isRouteFinished() {
		return (this.routeStop != 0);
	}

	public void stopRoute() throws RouteTimeException {
		if(this.isRouteStarted()) this.routeStop = System.currentTimeMillis();
		else throw new RouteTimeException("Can't finish a unstarted route.");
	}

	/**
	 * returns a linked list of deliveries
	 * @return
	 */
	public LinkedList<Delivery> getLinkedDeliveries(boolean onlyOpen){
		LinkedList<Delivery> deliveries = new LinkedList<Delivery>();
		
		deliveries.addAll(this.getDeliveries(onlyOpen));
		
		return deliveries;
	}
	
	/**
	 * @return
	 */
	public void acceptDelivery(Address address){
		
		for(OrderItem oi: this.getAllAtAddress(address)) {
			try {
				oi.acceptDelivery();
			} catch (InconsistentOrderStateException e) {
				e.printStackTrace();
			}
		}
	}
	
public void rejectDelivery(Address address){
		
		for(OrderItem oi: this.getAllAtAddress(address)) {
			try {
				oi.rejectDelivery();
			} catch (InconsistentOrderStateException e) {
				e.printStackTrace();
			}
			this.remove(oi);
		}
	}
	
	
	/**
	 * @return
	 */
	public void acceptCurrentDelivery(){
		
		if(!this.getDeliveries(true).isEmpty()) {
			Address currentaddress = this.getCurrentAddress();
			this.acceptDelivery(currentaddress);
		}
	}
	
	
	
	public Address getCurrentAddress() {
		if(!this.getDeliveries(true).isEmpty())
			{return this.getDeliveries(true).get(0).getAddress();}
		else
			return deposit;
	}
	
	public Delivery getCurrentDelivery() {
		if(!this.getDeliveries(true).isEmpty())
			{return this.getDeliveries(true).get(0);}
		return null;
	}
	
	public ArrayList<OrderItem> getArrayOfOI() {
		ArrayList<OrderItem> oi = new ArrayList<OrderItem>();
		oi.addAll(orderItems);
		
		return oi;

	}

		

}
