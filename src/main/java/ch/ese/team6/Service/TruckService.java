package ch.ese.team6.Service;

import java.util.Date;
import java.util.List;

import ch.ese.team6.Model.Truck;

public interface TruckService {
	List<Truck> findFreeTrucks(Date date);
}
