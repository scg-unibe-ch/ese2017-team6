package ch.ese.team6.Model;

/**
 * Dummyimplementation of the Computation of addresses.
 * Works with the sample data.
 */
public class AddressDistanceDummyManager extends AddressDistanceManager {

	@Override
	public int getDistanceAssym(Address a1, Address a2) {
		
		String city1 =  a1.getCity();
		String city2 = a2.getCity();
		int distance = 0;
		
		distance = (a1.getCountry().hashCode() -a2.getCountry().hashCode()) % 10000;
		distance += (a1.getCity().hashCode() -a2.getCity().hashCode()) % 1000;
		distance += (a1.getStreet().hashCode() -a2.getStreet().hashCode()) % 100;
		return distance;
		
	}

}
