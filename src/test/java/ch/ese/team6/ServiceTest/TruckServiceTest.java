package ch.ese.team6.ServiceTest;

import static org.junit.Assert.*;

import javax.net.ssl.SSLEngineResult.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.ese.team6.Application;
import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.DupplicateEntryException;
import ch.ese.team6.Model.DataStatus;
import ch.ese.team6.Model.Truck;
import ch.ese.team6.Repository.TruckRepository;
import ch.ese.team6.Service.TruckService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TruckServiceTest {

	@Autowired	private TruckService truckService;
	@Autowired	private TruckRepository truckRepository;
	
	@Test
	public void saveValidTruck() {
		long numberOfEntries = truckRepository.findAll().size();
		try {
			Truck truck = new Truck();
			truck.setTruckname("truck");
			truck.setMaxLoadCapacity(1);
			truck.setMaxCargoSpace(1);
			truck.setStatus(DataStatus.ACTIVE);
			truckService.save(truck);
		} catch (BadSizeException e) {
			e.printStackTrace();
			fail("Valid values shouldn't throw exception.");
		} catch (DupplicateEntryException e) {
			// TODO Auto-generated catch block
			fail("valid truck should be saved.");
		}
		if( numberOfEntries+1 != truckRepository.findAll().size()) fail("Truck wasn't saved");
		
	}
	
	@Test
	public void dupplicateTrucksShouldntSave() {
		try {
			Truck truck1 = new Truck();
			truck1.setTruckname("truck1");
			truck1.setMaxCargoSpace(1);
			truck1.setMaxLoadCapacity(1);
			truck1.setStatus(DataStatus.ACTIVE);
			truckService.save(truck1);
		}
		catch(BadSizeException | DupplicateEntryException e) {
			e.printStackTrace();
			fail("Truck should be saved");
		}
		try{
			Truck truck1 = new Truck();
			truck1.setTruckname("truck1");
			truck1.setMaxCargoSpace(1);
			truck1.setMaxLoadCapacity(1);
			truck1.setStatus(DataStatus.ACTIVE);
			truckService.save(truck1);
			fail("Truck shouldnt be saved.");
		}
		catch(BadSizeException e) {
			fail("Truck should be saved");
		}
		catch(DupplicateEntryException n) {
		}
		
	}
		

}
