package ch.ese.team6.Model;

import java.util.ArrayList;

public class RouteCollection extends ArrayList<Route> {



public RouteCollection(int size) {
	super(size);
}

public void removeEmptyRoutes() {
	for(int i = this.size()-1; i>=0;i--) {
		if(this.get(i).getOrderItems().isEmpty()) {
			this.remove(i);
		}
	}
}


public int getDrivenDistance(AddressDistanceManager addressDistances) {
	
	int d = 0;
	for(Route r:this) {
		d+= r.getDrivenDistance(addressDistances);
	}
	return d;
}

public String toString() {
	System.out.println("_____________");
	
	String s = ("Route Collection consists of "+this.size()+" routes\n");
	
	for(Route r:this) {
		s = s+("---\n");
		s = s+r.toString()+"\n";
		s=s+("---\n");
	}
	return s;
}
}
