package ch.ese.team6.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ese.team6.Model.IDelivarable;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Repository.RouteRepository;


@Service
public class RouteServiceImpl implements RouteService {
	@Autowired
	private RouteRepository routeRepository;
	@Override
	public int calculateLeftCapacity(Route route) {
		int openSpace = route.getTruck().getMaxLoadCapacity()-route.getWeight();
		
		return openSpace;
	}
	
	public boolean isTruckFull(Route route) {
		return route.isFull();
	}
	/**
	 * Returns only the rroutes able to transport the delivarable o
	 * @param o
	 * @return
	 */
	@Override
	public List<Route> selectWithLeftCapacity(IDelivarable o){
		List<Route> routes = routeRepository.findAll();
		for(int i = routes.size()-1; i>=0;i--) {
			if(!routes.get(i).doesIDelivarableFit(o)) {
				routes.remove(i);
			}
		}
		return routes;
	}
	
	@Override
	public List<Route> selectWithLeftCapacity(){
		List<Route> routes = routeRepository.findAll();
		for(int i = routes.size()-1; i>=0;i--) {
			if(routes.get(i).isFull()) {
				routes.remove(i);
			}
		}
		return routes;
	}
}
