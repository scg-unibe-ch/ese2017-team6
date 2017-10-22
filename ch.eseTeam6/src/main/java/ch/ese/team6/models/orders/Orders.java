package ch.ese.team6.models.orders;

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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import ch.ese.team6.models.customers.Address;
import ch.ese.team6.models.customers.Customer;
import ch.ese.team6.models.orderitems.OrderItems;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Orders {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CUSTOMER_ID")
	@NotNull
	private Customer customer;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull private Date deliveryDate;
	
	@NotNull
	private OrderStatus orderStatus;
	
	@OneToMany(fetch=FetchType.LAZY,cascade = {CascadeType.ALL})
	@JoinColumn(name="ORDERS_ID")
	private List<OrderItems> orderItems = new ArrayList<OrderItems>();
 	

	
	public Orders() {
		super();
		this.orderStatus=OrderStatus.OPEN;
	}
	
	
	
    public List<OrderItems> getOrderItems() {
        return this.orderItems;
    }

	public long getId() {
		return id;
	}

    
    public Customer getCustomer() {
    	return this.customer;
    }
    
    public Address getAddress() {
    	return this.customer.getAddress();
    }
    
    public Customer setCustomer(Customer cus) {
    	return this.customer = cus;
    }
   
    public OrderStatus getStatus() {
    	return orderStatus;
    }
    

    public void setStatus(OrderStatus s ) {
    	this.orderStatus = s;
    }
    
    public void setDeliveryDate(Date date) {
    	this.deliveryDate = date;
    }
    
    public Date getDeliveryDate() {
    	return deliveryDate;
    }
    



	
}
