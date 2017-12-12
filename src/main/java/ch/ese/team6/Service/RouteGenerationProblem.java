package ch.ese.team6.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.AddressDistanceManager;
import ch.ese.team6.Model.IDelivarable;
import ch.ese.team6.Model.Truck;
import ch.ese.team6.Model.User;

/**
 * 
 * A RouteGeneratorionProblem is solved by the {@link}IAtomaticRouteGenerator.java
 * Stores:
 * The deposit where the route starts
 * The trucks we can use for optimization
 * The orders or orderitems we want to deliver
 * The AddressManger we use to compute distances
 *
 */
public class RouteGenerationProblem {
private Address depositAddress;
private ArrayList<IDelivarable> orders;
private List<Truck> trucks;
private AddressDistanceManager addressManager;
private List<User> drivers;

@DateTimeFormat(pattern = "yyyy-MM-dd")
private Date deliveryDate;

public boolean isOK() {
	/*
	System.out.println("Depsoit"+(depositAddress==null));
	System.out.println("Orders"+(orders==null));
	System.out.println("Trucks"+(trucks==null));
	System.out.println("Address Manger"+(addressManager==null));
	System.out.println("drivers"+(drivers==null));
	*/
	if(depositAddress==null || orders==null||trucks==null||addressManager==null||drivers==null) {
		return false;
	}
	
	return true;
}

public boolean theoreticallySolvable() {
	
	if(!this.isOK()) {
		return false;
	}
	if(orders.isEmpty()||trucks.isEmpty()||drivers.isEmpty()) {
		return false;
	}
	return (this.getOrdersSize()<=this.getTrucksSize() && this.getOrdersWeight()<=this.getTrucksWeight());
}


public int getTrucksSize() {
	int truckCapacitySize = 0;
	
	for(Truck t: trucks) {
		truckCapacitySize+=t.getMaxCargoSpace();
		
	}
	return truckCapacitySize;
}
public int getTrucksWeight() {
	int truckCapacityWeight = 0;
	
	for(Truck t: trucks) {
		truckCapacityWeight+=t.getMaxLoadCapacity();
		
	}
	return truckCapacityWeight;
}

public int getOrdersWeight() {

	int orderWeight=0;
	for(IDelivarable o:orders) {
		orderWeight+=o.getOpenWeight();
	}
	
	return orderWeight; 
}

public int getOrdersSize() {
	int orderSize = 0;
	for(IDelivarable o:orders) {
		orderSize+=o.getOpenSize();
	}
	
	return orderSize; 
}

public Address getDepositAddress() {
	return depositAddress;
}




public void setDepositAddress(Address deposit) {
	this.depositAddress = deposit;
}
public ArrayList<IDelivarable> getOrders() {
	return orders;
}
public void setOrders(ArrayList<IDelivarable> orders) {
	this.orders = orders;
}
public List<Truck> getTrucks() {
	return trucks;
}
public void setTrucks(List<Truck> trucks) {
	this.trucks = trucks;
}
public AddressDistanceManager getAddressManager() {
	return addressManager;
}
public void setAddressManager(AddressDistanceManager addressManager) {
	this.addressManager = addressManager;
}


public void setDeliveryDate(Date date) {
	
	this.deliveryDate = CalendarService.setMidnight(date);
}



public Date getDeliveryDate() {
	return deliveryDate;
}


public List<User> getDrivers() {
	return drivers;
}


public void setDrivers(List<User> drivers) {
	this.drivers = drivers;
}






}
