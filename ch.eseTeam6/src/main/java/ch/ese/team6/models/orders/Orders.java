package ch.ese.team6.models.orders;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import ch.ese.team6.models.clients.Address;
import ch.ese.team6.models.clients.Client;


@Entity
public class Orders {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@NotNull private Client clientName;
	@NotNull private Address deliveryAddress;
	@NotNull private String orderStatus;
	
	
	public Orders(Client client, Address address, String status) {
		this.clientName = client;
		this.deliveryAddress = address;
		this.orderStatus = status;
	}
	
	public Orders() {
		clientName = null;
		deliveryAddress = null;
		orderStatus = "OPEN";
	}
	
	public enum Status {OPEN, SCHEDULED, ONTOUR, DELIVERED};

    public Client getClientName() {
        return clientName;
    }

    public void setClientName(Client name) {
        this.clientName = name;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address address) {
        this.deliveryAddress = address;
    }
    
    public String getStatus() {
    	return orderStatus;
    }
    
    public void setStatus(Status newStatus ) {
    	this.orderStatus = newStatus.toString();
    }
}
