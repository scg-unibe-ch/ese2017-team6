package ch.ese.team6.ModelTest;

import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;



import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.AddressDistanceManager;

public class AddressDistanceMangerTest {
Address bern;
Address zurich;
Address basel;
Set<Address> allAddresses;
	@Before
	public void AddressDistanceMangerSetUp() {
		// We need to mock the objects because the distancees cannot be initialized in the test Enviroment.
		 bern = mock(Address.class);
		when(bern.getId()).thenReturn(0l);
		 zurich = mock(Address.class);
		when(zurich.getId()).thenReturn(1l);
		 basel = mock(Address.class);
		when(basel.getId()).thenReturn(2l);
		
        when(bern.getDistance(zurich)).thenReturn(60*60l);
        
        when(zurich.getDistance(bern)).thenReturn(60*60l);

        when(basel.getDistance(zurich)).thenReturn(40*60l);
        when(zurich.getDistance(basel)).thenReturn(40*60l);
        

        when(basel.getDistance(bern)).thenReturn(70*60l);
        when(bern.getDistance(basel)).thenReturn(70*60l);
        allAddresses = new HashSet<Address>();
        allAddresses.add(basel);
        allAddresses.add(zurich);
        allAddresses.add(bern);
	} 
	
	@Test
	public void getNeighrestAddressTest() {
		AddressDistanceManager am = new AddressDistanceManager();
		assertEquals(bern,am.getNeigherstAddress(bern, allAddresses, false));
		assertEquals(zurich,am.getNeigherstAddress(bern, allAddresses, true));
	}
	
	@Test
	public void optimalRouteTest() {
		AddressDistanceManager am = new AddressDistanceManager();
		LinkedHashSet<Address> optimalRoute = am.optimalRoute(allAddresses, bern);
		Iterator<Address> iterator = optimalRoute.iterator();
		assertEquals(bern,iterator.next());
		assertEquals(zurich,iterator.next());
		assertEquals(basel,iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void estimateRouteTimeTest() {

		AddressDistanceManager am = new AddressDistanceManager();
		assertEquals((60+40+70)*60,am.estimateRouteTime(allAddresses, bern));
	}
	
	@Test
	public void getDistanceToNeihgrestAddressTest() {

		AddressDistanceManager am = new AddressDistanceManager();
		Set<Address> addresses = new HashSet<Address>();
		addresses.add(basel); addresses.add(zurich);
		assertEquals((60)*60,am.getDistanceToNeighrestAddress(bern, addresses));

		assertEquals((0)*60,am.getDistanceToNeighrestAddress(zurich, addresses));
	
	}
	
}
