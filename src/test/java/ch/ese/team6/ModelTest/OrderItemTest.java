package ch.ese.team6.ModelTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import ch.ese.team6.Exception.InconsistentOrderStateException;
import ch.ese.team6.Model.Item;
import ch.ese.team6.Model.OrderItem;
import ch.ese.team6.Model.OrderStatus;

public class OrderItemTest {
	OrderItem oi;
	Item item;
	@Before
	public void setUp() {
		oi = new OrderItem();
		item = mock(Item.class);
		when(item.getRequiredSpace()).thenReturn(1);
		when(item.getWeight()).thenReturn(2);
		assertFalse(oi.invariant());
		oi.setItem(item);
		oi.setAmount(5);
		assertTrue(oi.invariant());
		assertEquals(OrderStatus.OPEN,oi.getOrderItemStatus());
		assertEquals(10,oi.getWeight());
		assertEquals(5,oi.getSize());
		assertEquals(10,oi.getOpenWeight());
		assertEquals(5,oi.getOpenSize());
		
		
	}
	@Test
	public void stautsTest1() throws InconsistentOrderStateException {
		

		oi.setOrderItemStatus(OrderStatus.OPEN);
		oi.schedule();
		assertEquals(OrderStatus.SCHEDULED,oi.getOrderItemStatus());
		assertEquals(10,oi.getWeight());
		assertEquals(5,oi.getSize());
		assertEquals(0,oi.getOpenWeight());
		assertEquals(0,oi.getOpenSize());
		
		oi.rejectDelivery();

		assertEquals(OrderStatus.OPEN,oi.getOrderItemStatus());
		assertEquals(10,oi.getWeight());
		assertEquals(5,oi.getSize());
		assertEquals(10,oi.getOpenWeight());
		assertEquals(5,oi.getOpenSize());
		
		oi.schedule();
		oi.acceptDelivery();

		assertEquals(OrderStatus.FINISHED,oi.getOrderItemStatus());
		assertEquals(10,oi.getWeight());
		assertEquals(5,oi.getSize());
		assertEquals(0,oi.getOpenWeight());
		assertEquals(0,oi.getOpenSize());
		
		oi.setOrderItemStatus(OrderStatus.OPEN);
	}
	
	@Test(expected = InconsistentOrderStateException.class)
	public void statusTest2() throws InconsistentOrderStateException {

		oi.setOrderItemStatus(OrderStatus.OPEN);
		oi.acceptDelivery();
	}
	
	@Test(expected = InconsistentOrderStateException.class)
	public void statusTest3() throws InconsistentOrderStateException {

		oi.setOrderItemStatus(OrderStatus.OPEN);
		oi.rejectDelivery();
	}
	@Test(expected = InconsistentOrderStateException.class)
	public void statusTest4() throws InconsistentOrderStateException {

		oi.setOrderItemStatus(OrderStatus.FINISHED);
		oi.rejectDelivery();
	}
	@Test(expected = InconsistentOrderStateException.class)
	public void statusTest5() throws InconsistentOrderStateException {

		oi.setOrderItemStatus(OrderStatus.FINISHED);
		oi.acceptDelivery();
	}
	@Test(expected = InconsistentOrderStateException.class)
	public void statusTest6() throws InconsistentOrderStateException {

		oi.setOrderItemStatus(OrderStatus.SCHEDULED);
		oi.schedule();
	}
	@Test(expected = InconsistentOrderStateException.class)
	public void statusTest7() throws InconsistentOrderStateException {

		oi.setOrderItemStatus(OrderStatus.FINISHED);
		oi.schedule();
	}
}
