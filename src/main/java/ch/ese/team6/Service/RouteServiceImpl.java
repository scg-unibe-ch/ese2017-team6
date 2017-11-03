package ch.ese.team6.Service;

import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ese.team6.Model.Delivery;
import ch.ese.team6.Model.Item;
import ch.ese.team6.Model.OrderItem;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Repository.DeliveryRepository;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Repository.TruckRepository;

@Service
public class RouteServiceImpl implements RouteService {
	@Autowired
	private TruckRepository truckRepository;
	@Autowired
	private RouteRepository routeRepository;
	@Autowired
	private DeliveryRepository deliveryRepository;
	@Override
	public int calculateLeftCapacity(Route route) {
		int openSpace = route.getTruck().getMaxLoadCapacity();
		List<Delivery> deliveries = route.getDeliveries();
		for (Delivery delivery : deliveries){
			List<OrderItem> items = delivery.getItems();
			for (OrderItem item:items) {
				openSpace -= item.getAmount()*item.getItem().getRequiredAmountOfPalettes();
			}
		}
		return openSpace;
	}
	
	public boolean isTruckFull(Route route) {
		return false;
	}
	
	public List<Route> selectWithLeftCapacity(){
		List<Route> routes = routeRepository.findAll();
		Stack<Integer> fullRoutes = new Stack<Integer>();
		for(Route route: routes) {
			if(route.isFull()) fullRoutes.add(routes.indexOf(route));
		}
		while (!fullRoutes.isEmpty()) {
			routes.remove((int)fullRoutes.pop());
		}
		return routes;
	}
}
