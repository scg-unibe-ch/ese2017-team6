package ch.ese.team6.Model;

import java.util.ArrayList;
import java.util.List;

public class RouteCollection {

private ArrayList<Route> routes;

public RouteCollection() {
	this.routes = new ArrayList<Route>();
}

public RouteCollection(int size) {
	this.routes = new ArrayList<Route>(size);
}

public void removeEmptyRoutes() {
	for(int i = routes.size()-1; i>=0;i--) {
		if(routes.get(i).getOrderItems().isEmpty()) {
			routes.remove(i);
		}
	}
}

public void add(Route r) {
	this.routes.add(r);
}
public void remove(Route r) {
	this.routes.remove(r);
}
public int getEstimatedTime() {
	
	int d = 0;
	for(Route r:routes) {
		d+= r.getEstimatedTime();
	}
	return d;
}

@Override
public String toString() {
	System.out.println("_____________");
	
	String s = ("Route Collection consists of "+routes.size()+" routes\n");
	
	for(Route r:routes) {
		s = s+("---\n");
		s = s+r.toString()+"\n";
		s=s+("---\n");
	}
	return s;
}



public int size() {
	return routes.size();
}
public List<Route> getRoutes() {
	return this.routes;
}
}
