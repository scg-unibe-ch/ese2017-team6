package ch.ese.team6.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}