package ch.ese.team6.Model;

import java.util.List;
import java.util.Set;

/**
 * Our Application will need an Object, that tells us
 * how far an adress is away from some other Address.
 * The implementation may be with Google Maps and may
 * or may not cache data.
 * In a first place we will use a DummyAddressManger
 * 
 */
public abstract class AddressDistanceManager {
	
	/**
	 * Computes the nearest address in the set addresses to address1
	 * if the list addresses contains address1 and ignoreSelf is true, then the method will not take into account
	 * address1 and will give the nearest Address different from address1
	 * Precondition addresses has at least one element.
	 * If ignoreSelf is true then addresses must contain one element different from address1
	 * @return
	 */
	public Address getNeigherstAddress(Address address1, Set<Address> addresses, boolean ignoreSelf) {
		assert address1 != null;
		assert addresses != null;
		
		assert(addresses.size()>1 && (!ignoreSelf || !addresses.contains(address1) || addresses.size()>2));//We require
		int min = Integer.MAX_VALUE;
		Address ret = address1;
		
		for(Address t: addresses) {
			if(!ignoreSelf || (ignoreSelf&&!t.equals(address1))) {
				if(this.getDistance(address1, t)<min) {
					ret = t;
				}
			}
			
		}
		
		return ret;
	}
	
	/**
	 * Returns the distance of address1 to the neareast address in addresses.
	 * Will be 0 if addresses contains address1.
	 * Precondition addresses must contain at least one element.
	 * @param address1
	 * @param addresses
	 * @return
	 */
	public int getDistanceToNeighrestAddress(Address address1, Set<Address> addresses) {
		assert address1 != null;
		assert addresses != null;
		assert(addresses.size()>0);//We require
		
		
		if (addresses.contains(address1)){
			return 0;
		}
		return getDistance(this.getNeigherstAddress(address1, addresses,false),address1);
	}
	
	/**
	 * Returns the distance from Address a1 to Address a2. 
	 * The method makes sure that the distance from a1 to a2 is the same
	 * as the distance from a2 to a1
	 * @param a1
	 * @param a2
	 * @return
	 */
	public int getDistance(Address a1, Address a2) {
		return (int) (getDistanceAssym(a1,a2)+getDistanceAssym(a2,a1))/2;
	}
	/**
	 * Will implemented using GoogleMaps or using a Dummy in a first place.
	 * Does not need to be symmetric.
	 * @param a1
	 * @param a2
	 * @return
	 */
	protected abstract int getDistanceAssym(Address a1, Address a2);
}
