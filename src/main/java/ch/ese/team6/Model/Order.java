package ch.ese.team6.Model;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Order  implements IDelivarable{
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
	
	@OneToMany(fetch=FetchType.EAGER,cascade = {CascadeType.ALL})
	@JoinColumn(name="ORDERS_ID")
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
 	

	
	public Order() {
		super();
		this.orderStatus=OrderStatus.OPEN;
	}
	
	
	
    public List<OrderItem> getOrderItems() {
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
    @Override
    public void setRoute(Route r) {
    	for(OrderItem i: this.orderItems) {
    		i.setRoute(r);
    	}
    }

    public void setStatus(OrderStatus s ) {
    	this.orderStatus = s;
    }
    
    public void scheduleOrder() {
    	this.orderStatus = OrderStatus.SCHEDULED;
    }
    
    public void setDeliveryDate(Date date) {
    	this.deliveryDate = date;
    }
    
    public Date getDeliveryDate() {
    	return deliveryDate;
    }


    public boolean invariant() {
    	if((deliveryDate!=null)&&(customer!=null)&&(this.orderStatus!=null)&&(this.orderItems!=null)) {
  
    		if(this.orderItems.size()==0) {
    			return false;
    		}else {
    			for(OrderItem oi: this.orderItems) {
    				if(oi==null) {
    					
    					return false;
    				}
    				if(!oi.invariant()) {
    					return false;
    				}
    			}
    			
    			return true;
    		}
    	}
    	
    	return false;
    }
	public boolean isOK() {
		return this.invariant();
	}



	@Override
	public int getWeight() {
		int w = 0;
		for(OrderItem oi: this.orderItems) {
			w += oi.getWeight();
		}
		return w;
	}
	



	@Override
	public int getSize() {
		int s = 0;
		for(OrderItem oi: this.orderItems) {
			s += oi.getSize();
		}
		return s;
	}
    



	
}
