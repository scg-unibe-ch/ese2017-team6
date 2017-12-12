package ch.ese.team6.ModelTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.Delivery;
import ch.ese.team6.Model.OrderItem;
import ch.ese.team6.Model.Route;

public class DeliveryTest {
private Delivery delivery;
private Route route;
private Address europe;

	@Before
	public void setUp() {
		route = new Route();
		delivery = new Delivery(route);
		europe = new Address();
		assertEquals(null,delivery.getAddress());
		assertEquals(route,delivery.getRoute());
		assertTrue(delivery.getItems().isEmpty());
		assertEquals(0, delivery.getItems().size());
		assertEquals(0,delivery.getNumberOfItems());
		assertFalse(delivery.hasItems());
		assertEquals("",delivery.getDistanceFromPreviousDeliveryStr());
		assertEquals("",delivery.getDistanceToNextDeliveryStr());
		assertEquals(0,delivery.getWeight());
		assertEquals(0,delivery.getSize());
		
	}
	
	@Test
	public void distanceTest() {
		delivery.setDistanceFromPreviousDelivery(1);
		assertEquals("",delivery.getDistanceToNextDeliveryStr());
		assertEquals("drive 1 minute",delivery.getDistanceFromPreviousDeliveryStr());
		delivery.setDistanceToNextDelivery(2);
		assertEquals("drive 1 minute",delivery.getDistanceFromPreviousDeliveryStr());
		assertEquals("drive 2 minutes",delivery.getDistanceToNextDeliveryStr());
		
	}
	
	@Test
	public void addingItemsTest() {
		OrderItem i1 = mock(OrderItem.class);
		when(i1.getSize()).thenReturn(1);
		when(i1.getWeight()).thenReturn(2);
		OrderItem i2 = mock(OrderItem.class);
		when(i2.getSize()).thenReturn(5);
		when(i2.getWeight()).thenReturn(6);
		
		delivery.addItem(i1);
		assertEquals(i1,delivery.getItems().get(0));
		assertEquals(1,delivery.getItems().size());
		assertTrue(delivery.hasItems());
		assertEquals(1,delivery.getNumberOfItems());
		assertEquals(1,delivery.getSize());
		assertEquals(2,delivery.getWeight());
		
		delivery.addItem(i1);
		assertEquals(i1,delivery.getItems().get(0));
		assertEquals(i1,delivery.getItems().get(1));
		assertEquals(2,delivery.getItems().size());
		assertTrue(delivery.hasItems());
		assertEquals(2,delivery.getNumberOfItems());
		assertEquals(2,delivery.getSize());
		assertEquals(4,delivery.getWeight());
		


		
		List<OrderItem> list = new ArrayList<OrderItem>(2);
		list.add(i2); list.add(i1);
		delivery.addItems(list);

		assertEquals(i1,delivery.getItems().get(0));
		assertEquals(i1,delivery.getItems().get(1));
		assertEquals(i2,delivery.getItems().get(2));
		assertEquals(i1,delivery.getItems().get(3));
		assertEquals(4,delivery.getItems().size());
		assertTrue(delivery.hasItems());
		assertEquals(4,delivery.getNumberOfItems());
		assertEquals(8,delivery.getSize());
		assertEquals(12,delivery.getWeight());
		
	}
}
