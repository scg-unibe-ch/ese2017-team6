package ch.ese.team6.Model;

/**
 * Dummyimplementation of the Computation of addresses.
 * Works with the sample data.
 * The idea is:
 * when countries do not match: add distance of magnitude 1000km
 * if cities do not match: add distance of magnitude 100km
 * if street does not mach: add distance of magnitude 10 km
 * Use has and not Random number because we want this to be stable!
 * When calling two times the same procedure we want to have the same result.
 */
public class AddressDistanceDummyManager extends AddressDistanceManager {

	@Override
	public int getDistanceAssym(Address a1, Address a2) {
		
		int distance = 0;
		
		distance += Math.abs((a1.getCountry().hashCode() -a2.getCountry().hashCode())) % 10000;
		distance += Math.abs((a1.getCity().hashCode() -a2.getCity().hashCode())) % 1000;
		distance += Math.abs((a1.getStreet().hashCode() -a2.getStreet().hashCode())) % 100;
		return distance;
		
	}

}
