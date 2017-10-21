package ch.ese.team6.models.orders;

public enum OrderStatus {

    
    OPEN("OPEN"), 
    FINISHED("FINISHED");
    
    
    public static final OrderStatus[] ALL = { OPEN, FINISHED };
    
    
    private final String name;

    
    public static OrderStatus forName(final String name) {
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
    
    
    private OrderStatus(final String name) {
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

