package ch.ese.team6.ModelTest;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.Customer;
import ch.ese.team6.Model.Order;
import ch.ese.team6.Model.OrderItem;
import ch.ese.team6.Model.OrderStatus;

public class OrderTest {
Order order;
Address europe;
Customer cus;
OrderItem i1;
OrderItem i2;
OrderItem i3;
@Before
public void setUP() {
	order = new Order();
	cus = mock(Customer.class);
	i1 = mock(OrderItem.class);
	when(i1.getSize()).thenReturn(10);
	when(i1.getWeight()).thenReturn(20);
	when(i1.getOpenSize()).thenReturn(1);
	when(i1.getOpenWeight()).thenReturn(2);
	when(i1.invariant()).thenReturn(true);
	i2 = mock(OrderItem.class);
	when(i2.getSize()).thenReturn(5);
	when(i2.getWeight()).thenReturn(6);

	
	i3 = mock(OrderItem.class);
	when(i3.getSize()).thenReturn(7);
	when(i3.getWeight()).thenReturn(8);
	
	
	europe = mock(Address.class);
	when(cus.getAddress()).thenReturn(europe);
	order.setCustomer(cus);
	assertEquals(cus,order.getCustomer());
	assertEquals(europe,order.getAddress());
	assertEquals(0,order.getNumberOfItems());
	assertEquals(0,order.getOpenOrderItems().size());
	assertEquals(0,order.getWeight());
	assertEquals(0,order.getOpenWeight());
	assertEquals(0,order.getSize());
	assertEquals(0,order.getOpenSize());
	
	Calendar c = Calendar.getInstance();
	c.set(2017, Calendar.DECEMBER, 14);
	order.setDeliveryDate(c.getTime());
	c.clear();
	c.set(2017, Calendar.DECEMBER,14);
	assertEquals(c.getTime(),order.getDeliveryDate());
	assertEquals("2017-12-14",order.getDeliveryDateStr());
	assertEquals(OrderStatus.OPEN,order.getStatus());
	assertFalse(order.invariant());
}
@Test
public void ItemsTest() {
	when(i1.getOrderItemStatus()).thenReturn(OrderStatus.OPEN);
	when(i2.getOrderItemStatus()).thenReturn(OrderStatus.FINISHED);
	when(i3.getOrderItemStatus()).thenReturn(OrderStatus.SCHEDULED);

	order.getOrderItems().add(i1);
	assertEquals(OrderStatus.OPEN,order.getStatus());
	assertTrue(order.isOpen());
	assertTrue(order.invariant());
	when(i1.invariant()).thenReturn(false);
	assertFalse(order.invariant());
	when(i1.invariant()).thenReturn(true);
	assertEquals(1,order.getNumberOfItems());
	assertEquals(1,order.getOpenOrderItems().size());
	assertEquals(10,order.getSize());
	assertEquals(20,order.getWeight());
	assertEquals(1,order.getOpenSize());
	assertEquals(2,order.getOpenWeight());
	

	order.getOrderItems().add(i2);
	assertEquals(OrderStatus.OPEN,order.getStatus());
	assertTrue(order.isOpen());
	assertEquals(2,order.getNumberOfItems());
	assertEquals(1,order.getOpenOrderItems().size());
	assertEquals(15,order.getSize());
	assertEquals(26,order.getWeight());
	assertEquals(1,order.getOpenSize());
	assertEquals(2,order.getOpenWeight());
	

	order.getOrderItems().add(i3);
	assertEquals(OrderStatus.OPEN,order.getStatus());
	assertTrue(order.isOpen());
	assertTrue(order.getOpenOrderItems().contains(i1));
	assertFalse(order.getOpenOrderItems().contains(i2));
	assertFalse(order.getOpenOrderItems().contains(i3));
	assertTrue(order.getOrderItems().contains(i1));
	assertTrue(order.getOrderItems().contains(i2));
	assertTrue(order.getOrderItems().contains(i3));
	
	assertEquals(3,order.getNumberOfItems());
	assertEquals(1,order.getOpenOrderItems().size());
	assertEquals(22,order.getSize());
	assertEquals(34,order.getWeight());
	assertEquals(1,order.getOpenSize());
	assertEquals(2,order.getOpenWeight());
	

	order.getOrderItems().remove(i1);
	assertEquals(OrderStatus.SCHEDULED,order.getStatus());
	assertEquals(2,order.getNumberOfItems());
	assertEquals(0,order.getOpenOrderItems().size());
	assertEquals(12,order.getSize());
	assertEquals(14,order.getWeight());
	assertEquals(0,order.getOpenSize());
	assertEquals(0,order.getOpenWeight());
	
	order.getOrderItems().remove(i3);
	assertEquals(OrderStatus.FINISHED,order.getStatus());
	assertEquals(1,order.getNumberOfItems());
	assertEquals(0,order.getOpenOrderItems().size());
	assertEquals(5,order.getSize());
	assertEquals(6,order.getWeight());
	assertEquals(0,order.getOpenSize());
	assertEquals(0,order.getOpenWeight());
}
}
