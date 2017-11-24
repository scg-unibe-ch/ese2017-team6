package ch.ese.team6.Service;

import ch.ese.team6.Exception.InvalidAddressException;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.Distance;

public interface MapService {

	boolean checkAddress(Address address) throws InvalidAddressException;
	
	Distance calculateDistance(Address origin, Address destination) throws InvalidAddressException;
}
