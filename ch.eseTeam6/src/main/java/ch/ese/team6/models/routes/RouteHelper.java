package ch.ese.team6.models.routes;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ese.team6.models.orderitems.OrderItemRepository;
import ch.ese.team6.models.orderitems.OrderItems;
import ch.ese.team6.models.trucks.TruckRepository;
import ch.ese.team6.models.trucks.Trucks;
import ch.ese.team6.models.users.UserRepository;
import ch.ese.team6.models.users.Users;


@Service
public class RouteHelper {
	public List<Trucks> aviableTrucks;
	public List<Users> aviableDrivers;
	public List<OrderItems> dueItems;
	
	public Date routeDate;
	@Autowired
	private RouteRepository routeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TruckRepository truckRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;

	
	public RouteHelper(Date date) {
		this.routeDate = date;
		this.aviableTrucks = truckRepository.findAll();
		this.aviableDrivers = userRepository.findAll();
		this.dueItems = orderItemRepository.findAll();
		this.removeOccupiedRessources(date);
	}
	
	public RouteHelper() {}
	
	public Date getRouteDate() {
		return this.routeDate;
	}
	
	public List<Trucks> showTrucks(){
		return this.aviableTrucks;
	}
	
	public List<Users> showDrivers(){
		return this.aviableDrivers;
	}
	
	private void removeOccupiedRessources(Date date) {
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
