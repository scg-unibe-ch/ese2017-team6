package ch.ese.team6.models.routes;

import java.sql.Date;
import java.util.List;

import ch.ese.team6.models.orderitems.OrderItemRepository;
import ch.ese.team6.models.orderitems.OrderItems;
import ch.ese.team6.models.trucks.TruckRepository;
import ch.ese.team6.models.trucks.Trucks;
import ch.ese.team6.models.users.UserRepository;
import ch.ese.team6.models.users.Users;

public class RouteHelper {
	public List<Trucks> aviableTrucks;
	public List<Users> aviableDrivers;
	public List<OrderItems> dueItems;
	
	public Date routeDate;
	
	private RouteRepository routeRepository;
	private UserRepository userRepository;
	private TruckRepository truckRepository;
	private OrderItemRepository orderItemRepository;

	
	public RouteHelper(Date routeDate) {
		this.routeDate = routeDate;
		this.aviableTrucks = truckRepository.findAll();
		this.aviableDrivers = userRepository.findAll();
		this.dueItems = orderItemRepository.findAll();
		this.removeOccupiedRessources(routeDate);
	}
	
	private void removeOccupiedRessources(Date routeDate) {
		int index = 0;
		for(Trucks truck : aviableTrucks) {
			if(truck.getId() == routeRepository.findVehicleIdByRouteDate(routeDate))aviableTrucks.remove(index);
			else index++;
		}
		index = 0;
		for(Users driver: aviableDrivers) {
			if(driver.getId() ==routeRepository.findDriverByRouteDate(routeDate))aviableDrivers.remove(index);
			else index++;
		}
	}
}
