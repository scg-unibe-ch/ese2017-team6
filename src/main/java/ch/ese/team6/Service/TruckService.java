package ch.ese.team6.Service;


import java.util.Date;
import java.util.List;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.DupplicateEntryException;
import ch.ese.team6.Model.IDelivarable;
import ch.ese.team6.Model.Truck;

public interface TruckService {
	List<Truck> findFreeTrucks(Date date);

	List<Truck> findFreeTrucks(Date date,IDelivarable o);

	void save(Truck truck) throws DupplicateEntryException, BadSizeException;
}
