package ch.ese.team6.Model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Our Application will need an Object, that tells us
 * how far an adress is away from some other Address.
 * 
 */
public class AddressDistanceManager {
	
	/**
	 * Computes the nearest address in the set addresses to address1
	 * if the list addresses contains address1 and ignoreSelf is true, then the method will not take into account
	 * address1 and will give the nearest Address different from address1
	 * Precondition addresses has at least one element.
	 * If ignoreSelf is true then addresses must contain one element different from address1
	 * The implementation is stable: meaning that two addresses having 
	 * the same same distance to address1 always the same address will be returned.
	 * @return
	 */
	public Address getNeigherstAddress(Address address1, Set<Address> addresses, boolean ignoreSelf) {
		assert address1 != null;
		assert addresses != null;
		assert(addresses.size()>=1 && (!ignoreSelf || !addresses.contains(address1) || addresses.size()>=2));//We require
		
		
		if(ignoreSelf && addresses.contains(address1)) {
			addresses.remove(address1);
		}
		
		int min = Integer.MAX_VALUE;
		Address ret = addresses.iterator().next();
		
		for(Address t: addresses) {
			if(!ignoreSelf || (ignoreSelf&&!t.equals(address1))) {
				int distancetToAddress1 = this.getDistance(address1, t);
				if(distancetToAddress1<min) {
					ret = t;
					min = distancetToAddress1;
				}
				
				//We want this algorithm to be absolutely stable
				//i. e. if there are two addresses with the same
				//distance to address1 we want it to allways return
				//the same address
				if(distancetToAddress1==min) {
					if(ret.hashCode()>t.hashCode()) {
						ret=t;
					}
				}
			}
			
		}
		
		return ret;
	}
	
	
	/**
	 * This method reorders the List addresses such that the total time
	 * of a tour starting at the deposit at going to all addresses in the
	 * List address goes fast
	 * To accomplish this we use a myopic algorithm that starts at the deposit
	 * and moves to the nearest address we have not yet visited.
	 * 
	 * Does not modify the Set addresses
	 * 
	 * @param addresses
	 * @return a set (LinkedHashSet) containing all addresses including the
	 * deposit. the set starts with the deposit and then contains the addreses
	 * in an optimal order
	 */
	public LinkedHashSet<Address> optimalRoute(Set<Address> addresses,Address deposit){
		
		Set<Address> addresses_copy = new HashSet<Address>();
		addresses_copy.addAll(addresses);
		
		//the linkedHashSet retains the order
		LinkedHashSet<Address> sortedAddresses = new LinkedHashSet<Address>();
		sortedAddresses.add(deposit);
		
		Address currentAddress = deposit;
		Address nextAddress;
		
		while (!addresses_copy.isEmpty()) {
			nextAddress = this.getNeigherstAddress(currentAddress, addresses_copy, false);
			addresses_copy.remove(nextAddress);
			sortedAddresses.add(nextAddress);
			currentAddress = nextAddress;
		}

		
		return sortedAddresses;	
	}
	
	/**
	 * Returns the time in minutes it takes (computing an optimal route)
	 * to start at the deposit, visit all addresses and return to the deposit
	 * 
	 */
	public int estimateRouteTime(Set<Address> addresses,Address deposit){
		LinkedHashSet<Address> sortedAddresses = this.optimalRoute(addresses, deposit);
		int estimatedTime = 0;
		
		Address current = deposit;
		sortedAddresses.remove(deposit);
		
		for(Address next: sortedAddresses) {
			estimatedTime+=current.getDistance(next);
			current = next;
		}
		
		
		estimatedTime += current.getDistance(deposit);
		
		return estimatedTime;
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
	 * @param a1
	 * @param a2
	 * @return
	 */
	public int getDistance(Address a1, Address a2) {
		return (int) a1.getDistance(a2);
	}


}
