package ch.ese.team6.ModelTest;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.Customer;

public class CustomerTest {
	private Customer customer;
	private Address addressDelivery;
	private Address addressDomicil;
	@Before
	public void CustomerTest() {
		customer = new Customer();
		customer.setName("Swisscom");
		customer.setPhone("0800800800");
		customer.setEmail("info@swisscom.ch");
		addressDelivery = new Address();
		addressDelivery.setReachableByTruck(true);
		addressDomicil = new Address();
		addressDomicil.setReachableByTruck(false);	
	}
	
	@Test
	public void SetterTest() {
		assertEquals("Swisscom",customer.getName());
		assertEquals("0800800800",customer.getPhone());
		assertEquals("info@swisscom.ch",customer.getEmail());
	}
	
	@Test(expected=BadSizeException.class)
	public void TestNotReachableAddress() throws BadSizeException {
		Address canada = new Address();
		canada.setReachableByTruck(false);
		customer.setAddress(canada);
	}
	@Test
	public void TestReachableAddress() throws BadSizeException {
		Address canada = new Address();
		canada.setId(0l);
		canada.setReachableByTruck(false);
		Address europe = new Address();
		europe.setId(1l);
		europe.setReachableByTruck(true);
		customer.setAddress(europe);
		assertEquals(europe, customer.getAddress());
		assertEquals(europe,customer.getDomicilAddress());
		customer.setDomicilAddress(canada);
		assertEquals(canada,customer.getDomicilAddress());
		assertEquals(europe,customer.getAddress());
	}
}
