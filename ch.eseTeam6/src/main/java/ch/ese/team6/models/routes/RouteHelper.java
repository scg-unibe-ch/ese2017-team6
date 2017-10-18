package ch.ese.team6.models.routes;

import java.util.Date;
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

	
	public RouteHelper(java.util.Date date) {
		this.routeDate = date;
		this.aviableTrucks = truckRepository.findAll();
		this.aviableDrivers = userRepository.findAll();
		this.dueItems = orderItemRepository.findAll();
		this.removeOccupiedRessources(date);
	}
	
	private void removeOccupiedRessources(java.util.Date date) {
		int index = 0;
		for(Trucks truck : aviableTrucks) {
			if(truck.getId() == routeRepository.findVehicleIdByRouteDate(date))aviableTrucks.remove(index);
			else index++;
		}
		index = 0;
		for(Users driver: aviableDrivers) {
			if(driver.getId() ==routeRepository.findDriverByRouteDate(date))aviableDrivers.remove(index);
			else index++;
		}
	}
}
