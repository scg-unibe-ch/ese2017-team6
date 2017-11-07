package ch.ese.team6.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "route")
public class Route {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	private String routeDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="truck")
	public Truck truck;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="driver")
	public User driver;
	/*
	@OneToMany(mappedBy="route")
	public List<Delivery> deliveries;
	*/
	
	@OneToMany
	private Set<Delivery> deliveries;
	public long estimatedTime;
	public long measuredTime;

	public Route() {}
		
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

	public void addDelivery(Delivery delivery) {
		System.out.println(delivery.getAddress());
		try {
			deliveries.add(delivery);
		}
		catch(NullPointerException exception) {
			this.deliveries = new HashSet<Delivery>();
			deliveries.add(delivery);
		}
		
	}
	
	public boolean hasDeliveries() {
		try {
			return !this.deliveries.isEmpty();
		}
		catch(NullPointerException exception){
			return false;
		}
	}
	
	public List<Delivery> getDeliveries(){
		if (this.hasDeliveries()){
		List<Delivery> deliveries = new ArrayList<Delivery>(this.deliveries);
		return deliveries;
		}
		return null;
	}
	
	public int countDeliveries() {
		if (this.hasDeliveries()){
			List<Delivery> deliveries = new ArrayList<Delivery>(this.deliveries);
			return deliveries.size();
			}
			return 0;
	}
	
	public String calculateCapacity() {
		int cargoSpace = this.getTruck().getMaxCargoSpace();
		 return this.calculateUsedSpace()+ "/ " +cargoSpace; 
	}
	
	public boolean isFull() {
		return this.getTruck().getMaxCargoSpace()<this.calculateUsedSpace();
	}
	
	private int calculateUsedSpace() {
		if (this.hasDeliveries()) {
			int usedSpace = 0;
			List<Delivery> deliveries = this.getDeliveries();
			for (Delivery delivery : deliveries) {
				List<OrderItem> items = delivery.getItems();
				for (OrderItem item: items) {
					usedSpace += item.getItem().getRequiredAmountOfPalettes();
				}
			}
			return usedSpace;
		}
		return 0;
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


}
