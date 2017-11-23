package ch.ese.team6.Service;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.InvalidAddressException;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.Distance;

public interface AddressService {

	boolean isValid(Address address) throws BadSizeException, InvalidAddressException;

	Distance calculateDistance(Address origin, Address destination) throws InvalidAddressException;

	void save(Address oldAddress) throws BadSizeException, InvalidAddressException;

}
