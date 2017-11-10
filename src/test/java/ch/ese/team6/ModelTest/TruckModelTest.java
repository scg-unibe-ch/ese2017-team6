package ch.ese.team6.ModelTest;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Model.Truck;

public class TruckModelTest {

	@Test
	public void AcceptValidTrucks() throws BadSizeException {
		Truck truck = new Truck();
		truck.setTruckname("truckname");
		truck.setMaxCargoSpace(10);
		truck.setMaxLoadCapacity(10);
		truck.setVehicleCondition(0);
		assertTrue(truck.isValid());
	}
	
	@Test
	public void rejectInvalidTrucks() throws BadSizeException {
		Truck truck = new Truck();
		truck.setTruckname("Truck");
		truck.setVehicleCondition(0);
		assertFalse(truck.isValid());
	}

}
