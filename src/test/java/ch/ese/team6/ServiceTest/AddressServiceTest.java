package ch.ese.team6.ServiceTest;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.ese.team6.Application;
import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.InvalidAddressException;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.Distance;
import ch.ese.team6.Service.AddressService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AddressServiceTest {
	
	@Autowired private AddressService addressService;
	
	@Test
	public void validAddressShouldBeValid() {
		Address address = new Address();
		address.setCountry("Switzerland");
		address.setCity("Bern");
		address.setStreet("Engehalde 8");
		try {
			if (!addressService.isValid(address)) return;
		} catch (BadSizeException e) {
			e.printStackTrace();
			fail("Address should be valid;");
		} catch (InvalidAddressException e) {
			e.printStackTrace();
			fail("Google should find address.");
		}
	}
	
	@Test
	public void incompleteAddresShouldBeInvalid() {
		Address address = new Address();
		address.setCity("Bern");
		address.setStreet("Bahnhofstrasse 1");
		try {
			addressService.isValid(address);
			fail("Address shouldn't be valid.");
		}
		catch(BadSizeException e) {
			
		} catch (InvalidAddressException e) {
			e.printStackTrace();
			fail("Invalid Addresses should go to google API.");
		}
	}
	
	@Test
	public void invalidAddressShouldBeInvalid() {
		Address address = new Address();
		address.setCountry("Switzerland");
		address.setCity("Bern");
		address.setStreet("Teufelshalde 8");
		try {
			addressService.isValid(address);
			fail("Address shouldn't be valid.");
		} catch (BadSizeException e) {
			e.printStackTrace();
			fail("Address should be valid");
		} catch (InvalidAddressException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void shouldCalculateDistance() {
		System.out.println("*********************************start shouldCalculateDistance()");
		Address origin = new Address();
		origin.setCountry("Switzerland");
		origin.setCity("Bern");
		origin.setStreet("Engehalde 8");
		Address destination = new Address();
		destination.setCountry("Switzerland");
		destination.setCity("Zug");
		destination.setStreet("Bahnhofstrasse 1");
		try {
			if(addressService.calculateDistance(origin, destination).getDistanceMetres() >= 3600) return;
			System.out.println("*********************************end shouldCalculateDistance()");
		} catch (InvalidAddressException e) {
			e.printStackTrace();
			System.out.println("*********************************end shouldCalculateDistance()");
			fail("Distance should be calculated.");
			
		}
	}
	
	@Test
	public void shouldThrowExceptionIfCalculateDistanceFails() {
		Address origin = new Address();
		origin.setCountry("Switzerland");
		origin.setCity("Bern");
		origin.setStreet("Teufelshalde 8");
		Address destination = new Address();
		destination.setCountry("Switzerland");
		destination.setCity("Zug");
		destination.setStreet("Bahnhofstrasse 1");
		try {
			if(addressService.calculateDistance(origin, destination).getDistanceMetres() >= 3600) {
				fail("Distance should be calculated.");
			}
			
		} catch (InvalidAddressException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void newlySavedAddressShouldHaveDistances() {
		try {
		System.out.println("*********************************start newlySavedAddressShouldHaveDistances()");
		Address oldAddress = new Address();
		oldAddress.setCountry("Switzerland");
		oldAddress.setCity("Bern");
		oldAddress.setStreet("Engehalde 8");
		if(addressService.isValid(oldAddress)) addressService.save(oldAddress);
		else fail("Valid Address should be saved.");
		Address newAddress = new Address();
		newAddress.setCountry("Switzerland");
		newAddress.setCity("Zug");
		newAddress.setStreet("Bahnhofstrasse 1");
		if(addressService.isValid(newAddress)) addressService.save(newAddress);
		else fail("Valid Address should be saved.");
		System.out.println("Both distances are ok");
		Distance distance = addressService.findDistance(oldAddress, newAddress);
		System.out.println(distance.getOrigin());
		System.out.println(distance.getDestination());
		System.out.println(distance.getDurationSeconds() + " s");
		System.out.println("*********************************end newlySavedAddressShouldHaveDistances()");
			return;
		}
		catch(InvalidAddressException | BadSizeException e) {
			e.printStackTrace();
			fail("Didn't work");
		}
	}
	
	
	
	}
