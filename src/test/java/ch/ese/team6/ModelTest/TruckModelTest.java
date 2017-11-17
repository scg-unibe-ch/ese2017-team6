package ch.ese.team6.ModelTest;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Model.DataStatus;
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

}
