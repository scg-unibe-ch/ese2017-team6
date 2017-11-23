package ch.ese.team6.Service;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.InvalidAddressException;
import ch.ese.team6.Model.Address;

public interface AddressService {

	boolean isValid(Address address) throws BadSizeException, InvalidAddressException;

	int calculateDistance(Address origin, Address destination);

	void save(Address oldAddress);

}
