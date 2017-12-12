package ch.ese.team6.ModelTest;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.junit.Test;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Model.DataStatus;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Model.Truck;

public class TruckModelTest {

	@Test
	public void AcceptValidTrucks() throws BadSizeException {
		Truck truck = new Truck();
		truck.setTruckname("truckname");
		truck.setMaxCargoSpace(10);
		truck.setMaxLoadCapacity(10);
		truck.setStatus(DataStatus.ACTIVE);
		assertTrue(truck.isValid());
	}
	
	@Test
	public void rejectInvalidTrucks() throws BadSizeException {
		Truck truck = new Truck();
		truck.setTruckname("Truck");
		truck.setStatus(DataStatus.ACTIVE);
		assertFalse(truck.isValid());
	}
	
	@Test
	public void unSavedTrucksHaveNoId() {
		try {
			Truck truck = new Truck();
			assertFalse(truck.hasId());
		}
		catch(NullPointerException n) {
			fail("hasId should't throw exception.");
		}
	}
	
	@Test
	public void setWrongLoadCapacity() {
		try {
			Truck truck = new Truck();
			truck.setMaxLoadCapacity(0);
			fail("Wrong load capacity should throw exception");
		}
		catch(BadSizeException e) {
			
		}
	}
	@Test
	public void setCorrectLoadCapacity() {
		try {
			Truck truck = new Truck();
			truck.setMaxLoadCapacity(1);
			
		}
		catch(BadSizeException e) {
			fail("Correct load capacity shouldn't throw exception");
		}
	}
	
	@Test
	public void timetableTest() {
		Route r1 = mock(Route.class);
		Route r2 = mock(Route.class);
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(2017,9, 1, 10, 1, 0);
		when(r1.getRouteStartDate()).thenReturn(c.getTime());
		c.set(2017,9, 1, 9, 1, 0);
		when(r2.getRouteStartDate()).thenReturn(c.getTime());
		Truck truck = new Truck();
		truck.getRoutes().add(r1);
		truck.getRoutes().add(r2);

		Calendar c1 = Calendar.getInstance();
		c1.clear();
		c1.set(2017,9, 1, 10, 2, 0);
		
		
		assertEquals(null,truck.nextAppointment(c1.getTime()));
		c1.set(2017,9, 1, 10, 0, 0);
		assertEquals(r1.getRouteStartDate(),truck.nextAppointment(c1.getTime()));
		
		c1.set(2017,9, 1, 9, 0, 0);
		assertEquals(r2.getRouteStartDate(),truck.nextAppointment(c1.getTime()));
		
	}

}
