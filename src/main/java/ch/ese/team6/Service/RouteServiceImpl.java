package ch.ese.team6.Service;

import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ese.team6.Model.Delivery;
import ch.ese.team6.Model.OrderItem;
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
