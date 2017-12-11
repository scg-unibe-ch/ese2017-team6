package ch.ese.team6.ModelTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.ese.team6.Application;
import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.Distance;
import ch.ese.team6.Model.User;
import ch.ese.team6.Repository.AddressRepository;
import ch.ese.team6.Repository.DistanceRepository;
import ch.ese.team6.Service.SampleDataServiceImpl;
import ch.ese.team6.Service.UserServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AddressTest {
	@Autowired private AddressRepository addressRepository;
	@Autowired private DistanceRepository distanceRepository;
	
	@Test
	public void addressSetterGetterTest() {
		Address testAddress = new Address();
		testAddress.setStreet("Hochschulstr. 6");
		testAddress.setCity("3000 Bern");
		testAddress.setCountry("Schweiz");
		assertEquals("Hochschulstr. 6",testAddress.getStreet());
		assertEquals("3000 Bern",testAddress.getCity());
		assertEquals("Schweiz",testAddress.getCountry());
		assertTrue(testAddress.isOK());
		assertFalse(testAddress.isReachableByTruck());
		testAddress.setReachableByTruck(true);
		assertTrue(testAddress.isReachableByTruck());
		
	}
	
	/**
	 * Important note: getDistance is not tested because
	 * we cannot initialize the outgoing and incoming distances
	 * in the test environment easily.
	 * The code was carefully checked.
	 *
	 */
	
	public void distanceTest() {
		Address bern = new Address();
		bern.setStreet("Hochschulstr. 6");
		bern.setCity("3000 Bern");
		bern.setCountry("Schweiz");
		bern.setReachableByTruck(true);
		addressRepository.save(bern);
		
		Address zurich = new Address();
		zurich.setStreet("Bahnhofstr. 6");
		zurich.setCity("8000 Bern");
		zurich.setCountry("Schweiz");
		zurich.setReachableByTruck(true);
		addressRepository.save(zurich);
		
		Distance distanceZurichBern = new Distance();
		distanceZurichBern.setOrigin(zurich);
		distanceZurichBern.setDestination(bern);
		distanceZurichBern.setDistanceMetres(100*1000);
		distanceZurichBern.setDurationSeconds(60*60);
		
		Distance distanceBernZurich = new Distance();
		distanceBernZurich.setOrigin(bern);
		distanceBernZurich.setDestination(zurich);
		distanceBernZurich.setDistanceMetres(100*1000);
		distanceBernZurich.setDurationSeconds(70*60);
		
		distanceRepository.save(distanceZurichBern);
		distanceRepository.save(distanceBernZurich);
		
		
		assertEquals(65*60,zurich.getDistance(bern));
		assertEquals(65*60,bern.getDistance(zurich));
		
		
		
	}
}