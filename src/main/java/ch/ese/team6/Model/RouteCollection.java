package ch.ese.team6.Model;

import java.util.ArrayList;

public class RouteCollection extends ArrayList<RouteTruckIDelivarable> {



public RouteCollection(int size) {
	super(size);
}

public void removeEmptyRoutes() {
	for(int i = this.size()-1; i>=0;i--) {
		if(this.get(i).getDelivarables().isEmpty()) {
			this.remove(i);
		}
	}
}


public int getDrivenDistance(AddressDistanceManager addressDistances) {
	
	int d = 0;
	for(RouteTruckIDelivarable r:this) {
		d+= r.getDrivenDistance(addressDistances);
	}
	return d;
}


}
