package ch.ese.team6.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.InvalidAddressException;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Repository.AddressRepository;
@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired AddressRepository addressRepository;
	@Autowired MapService mapService;

	@Override
	public boolean isValid(Address address) throws BadSizeException, InvalidAddressException{
		if(!address.isOK()) throw new BadSizeException("Address isn't complete.");
		if(!mapService.checkAddress(address)) throw new InvalidAddressException("Something can't find this address.");
		return true;
	}

	@Override
	public int calculateDistance(Address origin, Address destination) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void save(Address oldAddress) {
		// TODO Auto-generated method stub
		
	}

}
