package ch.ese.team6.ModelTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.*;

import java.sql.Date;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import com.mysql.jdbc.Driver;

import ch.ese.team6.Exception.RouteTimeException;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.IDelivarable;
import ch.ese.team6.Model.OrderItem;
import ch.ese.team6.Model.OrderStatus;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Model.RouteStatus;
import ch.ese.team6.Model.Truck;
import ch.ese.team6.Model.User;
import ch.ese.team6.Service.OurCompany;

public class RouteTest {
Address deposit;
Address bern;
Address basel;
Address zurich;
Truck truck;
User driver;
Route route;
OrderItem item1;
OrderItem item2;
OrderItem item3;
	
	@Before
	public void setUp() {
		this.setUpAddresses();
		this.setUpItems();
		truck = mock(Truck.class);
		driver = mock(User.class);
		

		when(truck.getMaxCargoSpace()).thenReturn(10);
		when(truck.getMaxLoadCapacity()).thenReturn(20);
		when(truck.nextAppointment(any(Date.class))).thenReturn(null);//truck does not have next route
		when(driver.nextAppointment(any(Date.class))).thenReturn(null);
		
		Calendar c = Calendar.getInstance();
		c.set(2017, 10, 11);
		route  = new Route(c.getTime(),deposit);
		c.clear();
		c.set(2017, 10, 11);
		c.set(Calendar.HOUR,OurCompany.WORKDAYSTART);
		assertEquals(c.getTime(),route.getRouteStartDate());
		assertEquals(deposit,route.getDeposit());
		
		route.setDriver(driver);
		route.setTruck(truck);
		assertEquals(driver,route.getDriver());
		assertEquals(truck,route.getTruck());
		assertEquals(RouteStatus.OPEN,route.getRouteStatus());
		assertEquals(0,route.getOpenDeliveries().size());
		assertFalse(route.hasDeliveries());
		assertEquals(0,route.getAllDeliveries().size());
		assertEquals(0,route.countDeliveries());
		assertFalse(route.isFull());
		assertEquals(0,route.getSize());
		assertEquals(0,route.getWeight());
		assertEquals(0,route.getEstimatedTime());
		assertEquals(0,route.getOrderItems().size());
		assertEquals("0/ 10",route.calculateCapacitySize());
		assertEquals("0/ 20",route.calculateCapacityWeight());
		assertEquals(c.getTime(),route.getRouteEndDate());
		assertEquals(0,route.getAllAddresses(false, true).size());
		assertEquals(0,route.getAllAddresses(false, false).size());


		
	}
	
	
	private void setUpAddresses() {
		deposit = mock(Address.class);
		when(deposit.getId()).thenReturn(3l);
		
		
		bern = mock(Address.class);
		when(bern.getId()).thenReturn(0l);
		 zurich = mock(Address.class);
		when(zurich.getId()).thenReturn(1l);
		 basel = mock(Address.class);
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

	}
	private void setUpItems() {
		item1 = mock(OrderItem.class);
		when(item1.getOpenSize()).thenReturn(1);
		when(item1.getOpenWeight()).thenReturn(2);
		when(item1.getAddress()).thenReturn(zurich);

		item2 = mock(OrderItem.class);
		when(item2.getOpenSize()).thenReturn(5);
		when(item2.getOpenWeight()).thenReturn(6);
		when(item2.getAddress()).thenReturn(basel);
		

		item3 = mock(OrderItem.class);
		when(item3.getOpenSize()).thenReturn(8);
		when(item3.getOpenWeight()).thenReturn(9);
		when(item3.getAddress()).thenReturn(bern);
	}
	@Test
	public void doesDelivarableFitTestSatisfiesScheduleTest() {

		when(truck.getMaxCargoSpace()).thenReturn(10);
		when(truck.getMaxLoadCapacity()).thenReturn(20);
		when(truck.nextAppointment(any(Date.class))).thenReturn(null);//truck does not have next route
		when(driver.nextAppointment(any(Date.class))).thenReturn(null);
		assertTrue(route.doesIDelivarableFit(item1));
		assertTrue(route.doesIDelivarableFit(item2));
		assertTrue(route.doesIDelivarableFit(item3));

		when(item3.getOpenWeight()).thenReturn(21);
		assertFalse(route.doesIDelivarableFit(item3));
		
		when(item3.getOpenWeight()).thenReturn(20);
		assertTrue(route.doesIDelivarableFit(item3));


		when(item3.getOpenSize()).thenReturn(11);
		assertFalse(route.doesIDelivarableFit(item3));
		
		when(item3.getOpenSize()).thenReturn(10);
		assertTrue(route.doesIDelivarableFit(item3));
		
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(2017, 10,11, 10, 3, 0);//this is 2 hours 3 minute after the route starts,
		//so only the item i3 (Bern) and i2 (Zurich) should fit
		when(truck.nextAppointment(any(Date.class))).thenReturn(c.getTime());
		assertTrue(route.doesIDelivarableFit(item3));
		assertFalse(route.doesIDelivarableFit(item2));
		assertTrue(route.doesIDelivarableFit(item1));
		assertTrue(route.satisfiesSchedule(item3));
		assertFalse(route.satisfiesSchedule(item2));
		assertTrue(route.satisfiesSchedule(item1));
		c.set(2017, 10,11,9,15,0);//this is only 1 hour 15 minutes after the route start
		//so only item 3 (Bern) should fit.
		when(driver.nextAppointment(any(Date.class))).thenReturn(c.getTime());
		assertTrue(route.doesIDelivarableFit(item3));
		assertFalse(route.doesIDelivarableFit(item2));
		assertFalse(route.doesIDelivarableFit(item1));
		assertTrue(route.satisfiesSchedule(item3));
		assertFalse(route.satisfiesSchedule(item2));
		assertFalse(route.satisfiesSchedule(item1));
		
		//because we have changed them
		this.setUpItems();
 	}
	
	@Test
	public void addDeliveriesTest() {
		when(truck.getMaxCargoSpace()).thenReturn(10);
		when(truck.getMaxLoadCapacity()).thenReturn(20);
		when(truck.nextAppointment(any(Date.class))).thenReturn(null);//truck does not have next route
		when(driver.nextAppointment(any(Date.class))).thenReturn(null);
		

		when(item3.getOpenSize()).thenReturn(11);//does not fit!
		route.addDelivarable(item3);
		assertEquals(0,route.getOpenDeliveries().size());
		assertFalse(route.hasDeliveries());
		assertEquals(0,route.getAllDeliveries().size());
		assertEquals(0,route.countDeliveries());
		assertFalse(route.isFull());
		assertEquals(0,route.getSize());
		assertEquals(0,route.getWeight());
		assertEquals(0,route.getEstimatedTime());
		assertEquals(0,route.getOrderItems().size());
		assertEquals("0/ 10",route.calculateCapacitySize());
		assertEquals("0/ 20",route.calculateCapacityWeight());
		assertEquals(route.getRouteStartDate(),route.getRouteEndDate());
		assertEquals(0,route.getAllAddresses(false, true).size());
		assertEquals(0,route.getAllAddresses(false, false).size());
		
	}
	@Test
	public void statusTest() throws RouteTimeException {
		assertEquals(RouteStatus.OPEN, route.getRouteStatus());
		route.startRoute();
		assertEquals(RouteStatus.ONROUTE, route.getRouteStatus());
		route.stopRoute();
		assertEquals(RouteStatus.FINISHED, route.getRouteStatus());
		
		
	}
	
}
