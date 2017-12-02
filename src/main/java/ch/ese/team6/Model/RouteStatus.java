package ch.ese.team6.Model;

public enum RouteStatus {

	OPEN("OPEN"),
    ONROUTE("ON ROUTE"),
    FINISHED("FINISHED");
	
	
    
    
    public static final RouteStatus[] ALL = { OPEN, FINISHED, ONROUTE};
    
    
    private final String name;

    
    public static RouteStatus forName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null for type");
        }
        if (name.toUpperCase().equals("OPEN")) {
            return OPEN;
        } else if (name.toUpperCase().equals("FINISHED")) {
            return FINISHED;
        }
        throw new IllegalArgumentException("Name \"" + name + "\" does not correspond to any Type");
    }
    
    
    private RouteStatus(final String name) {
        this.name = name;
    }
    
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return getName();
    }
}
