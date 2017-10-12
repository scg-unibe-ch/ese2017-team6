package ch.ese.team6.models.orders;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Orders")
public class Orders {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@NotNull private String clientName;
	@NotNull private String deliveryAddress;
	@NotNull private String orderStatus;
	
	
	public Orders(String client, String address, String status) {
		this.clientName = client;
		this.deliveryAddress = address;
		this.orderStatus = status;
	}
	
	public Orders(String client, String address) {
		this.clientName = client;
		this.deliveryAddress = address;
		this.orderStatus = "OPEN";
	}
	
	public Orders() {}
	
	//public enum Status {OPEN, SCHEDULED, ONTOUR, DELIVERED};

	public long getId() {
		return id;
	}
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String name) {
        this.clientName = name;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String address) {
        this.deliveryAddress = address;
    }
    
    public String getStatus() {
    	return orderStatus;
    }
    
    public void setStatus(String newStatus ) {
    	this.orderStatus = newStatus.toString();
    }
}
