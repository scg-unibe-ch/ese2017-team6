package models;

public class Order {
	
    private String customerName; // change to Customer objects
    private String deliveryAddress; // change to Address objects
    

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String name) {
        this.customerName = name;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String address) {
        this.deliveryAddress = address;
    }
}
