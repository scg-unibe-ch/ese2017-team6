package ch.ese.team6.Service;

import ch.ese.team6.Exception.InvalidAddressException;
import ch.ese.team6.Model.Address;

public interface MapService {

	boolean checkAddress(Address address) throws InvalidAddressException;
	
	long calculateDistance(Address origin, Address destination) throws InvalidAddressException;
}
