package ch.ese.team6.models.Deliveries;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import ch.ese.team6.models.customers.Address;
import ch.ese.team6.models.orderitems.OrderItems;
import ch.ese.team6.models.orders.Orders;




@Entity
public class Delivery {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private Calendar deliveryDate;

	
	@OneToMany(fetch=FetchType.LAZY,cascade = {CascadeType.ALL})
	@JoinColumn(name="DELIVERIES_ID")
	private List<OrderItems> orderItems = new ArrayList<OrderItems>();
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ADDRESS_ID")
	private Orders order;
	
	public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		this.order = order;
	}

	private String status;
	
	
	public long getId() {
		return id;
	}
	
	public Delivery(Orders order) {
		this.deliveryDate = Calendar.getInstance();
		this.order = order;
		this.status = "not delivered";
	}
	
	public Delivery() {
		this.deliveryDate = Calendar.getInstance();
	}
	

	public Address getAddress() {
		return this.order.getAddress();
	}


	public List<OrderItems> getOrderItems() {
        return this.orderItems;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	
	
}
