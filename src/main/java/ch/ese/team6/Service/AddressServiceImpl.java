package ch.ese.team6.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.InvalidAddressException;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.Distance;
import ch.ese.team6.Repository.AddressRepository;
import ch.ese.team6.Repository.DistanceRepository;
@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired AddressRepository addressRepository;
	@Autowired MapService mapService;
	@Autowired DistanceRepository distanceRepository;

	@Override
	public boolean isValid(Address address) throws BadSizeException, InvalidAddressException{
		if(!address.isOK()) throw new BadSizeException("Address isn't complete.");
		if(!mapService.checkAddress(address)) throw new InvalidAddressException("Something can't find this address.");
		return true;
	}

	@Override
	public Distance calculateDistance(Address origin, Address destination) throws InvalidAddressException {
		Distance distance = mapService.calculateDistance(origin, destination); 
		return distance;
	}

	@Override
	public void save(Address address) throws BadSizeException, InvalidAddressException {
		if (this.isValid(address)){
			List<Address> addresses = addressRepository.findAll();
			if (addresses.size()>0) {
				addressRepository.save(address);
				for(Address oldAddress : addresses) {
					Distance out = this.calculateDistance(address, oldAddress);
					distanceRepository.save(out);
					Address origin = oldAddress; Address destination = address;
					Distance in = new Distance();
					in.setDestination(destination); 
					in.setOrigin(origin);
					in.setDistanceMetres(out.getDistanceMetres());
					in.setDurationSeconds(out.getDurationSeconds());
					distanceRepository.save(in);
				}
			
			}
		}
	}
}
