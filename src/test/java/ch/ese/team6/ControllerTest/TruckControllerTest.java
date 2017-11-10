package ch.ese.team6.ControllerTest;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.ese.team6.Application;
import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Model.Truck;
import ch.ese.team6.Repository.TruckRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TruckControllerTest {
	
	@Autowired TruckRepository truckRepository;
	
	@Test
	public void shouldSaveValidTruck() {
		Truck truck = new Truck();
		try {
		truck.setTruckname("VW Bus");
		truck.setMaxCargoSpace(10);
		truck.setMaxLoadCapacity(10);
		truck.setVehicleCondition(0);
		truckRepository.save(truck);
		}
		catch(BadSizeException e) {
			fail("Should save valid truck");
		}
	}

}
