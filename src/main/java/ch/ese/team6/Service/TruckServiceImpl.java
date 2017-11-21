package ch.ese.team6.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.DupplicateEntryException;
import ch.ese.team6.Model.IDelivarable;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Model.Truck;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Repository.TruckRepository;

@Service
public class TruckServiceImpl implements TruckService{
	
	@Autowired
	private TruckRepository truckRepository;
	@Autowired
	private RouteRepository routeRepository;
	
	public List<Truck> findFreeTrucks(String date){
		List<Route> routes = routeRepository.findByRouteDate(date);
		List<Truck> occupiedTrucks = new ArrayList<Truck>();
		for(Route route: routes) {
			occupiedTrucks.add(route.getTruck());
		}
		List<Truck>freeTrucks = truckRepository.findAll();
		freeTrucks.removeAll(occupiedTrucks);
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
	 */
	public List<Truck> findFreeTrucks(String date, IDelivarable o) {
		List<Truck> freeTrucks = this.findFreeTrucks(date);
		for(int i = freeTrucks.size()-1; i>=0; i--) {
			if(o.getOpenWeight()>freeTrucks.get(i).getMaxLoadCapacity() || o.getOpenSize()>freeTrucks.get(i).getMaxCargoSpace()) {
				freeTrucks.remove(i);
			}
		}
		return freeTrucks;
	}
}