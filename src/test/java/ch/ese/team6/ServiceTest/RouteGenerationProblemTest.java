package ch.ese.team6.ServiceTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.AddressDistanceManager;
import ch.ese.team6.Model.Customer;
import ch.ese.team6.Model.IDelivarable;
import ch.ese.team6.Model.Order;
import ch.ese.team6.Model.OrderItem;
import ch.ese.team6.Model.OrderStatus;
import ch.ese.team6.Model.Truck;
import ch.ese.team6.Model.User;
import ch.ese.team6.Service.RouteGenerationProblem;

public class RouteGenerationProblemTest {
private RouteGenerationProblem rgp;
	

@Test
public void setUpTest() {
	rgp = setUpRouteGenerationProblem();
	Calendar c = Calendar.getInstance();
	c.clear();
	c.set(2012, 9, 21, 0, 0, 0);
	
	assertEquals(c.getTime(),rgp.getDeliveryDate());
	assertTrue(rgp.theoreticallySolvable());
	assertEquals(6,rgp.getOrdersWeight());
	assertEquals(4,rgp.getOrdersSize());

	assertEquals(40,rgp.getTrucksSize());
	assertEquals(60,rgp.getTrucksWeight());
	assertEquals(2,rgp.getTrucks().size());
	assertEquals(3,rgp.getDrivers().size());
}
	public static RouteGenerationProblem setUpRouteGenerationProblem() {
		RouteGenerationProblem rgp = new RouteGenerationProblem();
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(2012, 9, 21, 22, 20, 0);
		rgp.setDeliveryDate(c.getTime());

		c.set(2012, 9, 21, 0, 0, 0);
		

		
		Truck truck1 = mock(Truck.class);
		when(truck1.getMaxCargoSpace()).thenReturn(10);
		when(truck1.getMaxLoadCapacity()).thenReturn(20);

		Truck truck2 = mock(Truck.class);
		when(truck2.getMaxCargoSpace()).thenReturn(30);
		when(truck2.getMaxLoadCapacity()).thenReturn(40);

		ArrayList<Truck> trucks = new ArrayList<Truck>();
		trucks.add(truck1);trucks.add(truck2);
		
		
		User driver1 = mock(User.class);
		User driver2 = mock(User.class);
		User driver3 = mock(User.class);
		ArrayList<User> drivers = new ArrayList<User>();
		drivers.add(driver1);drivers.add(driver2);drivers.add(driver3);
		
		Address deposit = mock(Address.class);
		when(deposit.getId()).thenReturn(3l);
		Address bern = mock(Address.class);
		when(bern.equals(bern)).thenReturn(true);
		when(bern.getId()).thenReturn(0l);
		Address zurich = mock(Address.class);
		when(zurich.equals(zurich)).thenReturn(true);
		when(zurich.getId()).thenReturn(1l);
		Address basel = mock(Address.class);
		when(basel.equals(basel)).thenReturn(true);
		when(basel.getId()).thenReturn(2l);	
		when(deposit.getDistance(bern)).thenReturn(1l);
		when(deposit.getDistance(zurich)).thenReturn(61l);
		when(deposit.getDistance(basel)).thenReturn(71l);
		when(bern.getDistance(deposit)).thenReturn(1l);
		when(zurich.getDistance(deposit)).thenReturn(61l);
		when(basel.getDistance(deposit)).thenReturn(71l);
        when(bern.getDistance(zurich)).thenReturn(60l);
        when(zurich.getDistance(bern)).thenReturn(60l);
        when(basel.getDistance(zurich)).thenReturn(60l);
        when(zurich.getDistance(basel)).thenReturn(60l);
        when(basel.getDistance(bern)).thenReturn(70*60l);
        when(bern.getDistance(basel)).thenReturn(70*60l);
		
        
        Customer baselCust = mock(Customer.class);
        when(baselCust.getAddress()).thenReturn(basel);
        Order o1 = new Order();
        o1.setCustomer(baselCust);
        OrderItem oi1 = mock(OrderItem.class);
        when(oi1.getOpenSize()).thenReturn(1);
        when(oi1.getOpenWeight()).thenReturn(2);
        when(oi1.getOrderItemStatus()).thenReturn(OrderStatus.OPEN);
        o1.getOrderItems().add(oi1);
        
        Customer zurichCust = mock(Customer.class);
        when(zurichCust.getAddress()).thenReturn(zurich);
        Order o2 = new Order();
        o2.setCustomer(zurichCust);
        OrderItem oi2 = mock(OrderItem.class);
        when(oi2.getOpenSize()).thenReturn(3);
        when(oi2.getOpenWeight()).thenReturn(4);
        when(oi2.getOrderItemStatus()).thenReturn(OrderStatus.OPEN);
        o2.getOrderItems().add(oi2);
        
        ArrayList<IDelivarable> orders = new ArrayList<IDelivarable>();
        orders.add(o1);orders.add(o2);
        
        rgp.setAddressManager(new AddressDistanceManager());
        rgp.setDepositAddress(deposit);
        rgp.setDrivers(drivers);
        rgp.setTrucks(trucks);
        rgp.setOrders(orders);
        return rgp;
	}
}
