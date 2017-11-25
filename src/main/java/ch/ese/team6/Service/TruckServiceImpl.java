package ch.ese.team6.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.DupplicateEntryException;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.IDelivarable;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Model.Truck;
import ch.ese.team6.Model.User;
import ch.ese.team6.Repository.AddressRepository;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Repository.TruckRepository;

@Service
public class TruckServiceImpl implements TruckService{
	
	@Autowired
	private TruckRepository truckRepository;
	@Autowired
	private RouteRepository routeRepository;
	@Autowired
	private AddressRepository addressRepository;
	
	public List<Truck> findFreeTrucks(Date date){
		List<Truck>freeTrucks = truckRepository.findAll();
		for(int i =freeTrucks.size()-1; i>=0; i--) {
			Truck truck = freeTrucks.get(i);
			if(truck.isOccupied(date)) {
				freeTrucks.remove(truck);
			}
		}
		return freeTrucks;
	}
	
	public void save(Truck truck) throws DupplicateEntryException, BadSizeException {
		if (!truck.isValid()) throw new BadSizeException("Truck is invalid.");
		if ((truck.hasId()) || this.existByTruckname(truck.getTruckname())) {
    		throw new DupplicateEntryException("truckname already exists.");
		}
		truckRepository.save(truck);
	}

	private boolean existByTruckname(String truckname) {
		return truckRepository.existsByTruckname(truckname);
	}

	@Override
	/**
	 * Will return the trucks free at date data and with enough capacity to transport o
	 * and with enough time to transport o
	 */
	public List<Truck> findFreeTrucks(Date date, IDelivarable o) {
		List<Truck> freeTrucks = this.findFreeTrucks(date);
		for(int i = freeTrucks.size()-1; i>=0; i--) {
			Truck truck = freeTrucks.get(i);
			if(o.getOpenWeight()>truck.getMaxLoadCapacity() || o.getOpenSize()>truck.getMaxCargoSpace()) {
				freeTrucks.remove(i);
				continue;
			}
			// Check if the truck has enough time to drive to the deliveryAddres
		    Route nextRouteOfthisTruck = truck.nextRoute(date);
				if(nextRouteOfthisTruck!=null) {
				    Date nextTimeThisTruckIsOccupied = nextRouteOfthisTruck.getRouteStartDate();
				    Address deposit = addressRepository.findOne(OurCompany.depositId);
				    //drive to the address and back
				    long requiredTime = 2*deposit.getDistance(o.getAddress())*60l*1000l;
				    Date truckWouldFinish = CalendarService.computeTaskEnd(CalendarService.getWorkingStart(date), requiredTime);
				    if(nextTimeThisTruckIsOccupied.before(truckWouldFinish)) {
				    	freeTrucks.remove(i);
				    }
				}
			
			
		}
		return freeTrucks;
	}
}