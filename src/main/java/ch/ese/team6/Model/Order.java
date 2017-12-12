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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import ch.ese.team6.Exception.InconsistentOrderStateException;
import ch.ese.team6.Service.CalendarService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Consists of a customer, and one or several orderItems
 * 
 */
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
	
	
	@OneToMany(fetch=FetchType.EAGER,cascade = {CascadeType.ALL})
	@JoinColumn(name="ORDERS_ID")
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	
	/**
	 * The address is stored even if the customer is stored
	 * The reason: we do not want to have address changes for the customer
	 * affecting its already executed or scheduled orders
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ADDRESS_ID")
	@NotNull
	private Address address;
 	

	
	public Order() {
		super();
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
    
    @Override
	public Address getAddress() {
    	return address;
    }
    
    public void setCustomer(Customer cus) {
    	 this.address = cus.getAddress();
    	 this.customer = cus;
    }
   
    

    /**
     * Returns the order status. The order status is OPEN if the order contains orderitems with status open
     * The status is scheduled if all elements are either scheduled or delivered.
     * The order is finished if all orderitems have the status finished.
     * @return
     */
    public OrderStatus getStatus() {
    	if(this.orderItems.isEmpty()) {
    		return OrderStatus.OPEN;
    	}
    	
    
    	for(OrderItem oi: this.orderItems) {
    		if(oi.getOrderItemStatus().equals(OrderStatus.OPEN)) {
    			return OrderStatus.OPEN;
    		}
    	}
    	
    	for(OrderItem oi: this.orderItems) {
    		if(oi.getOrderItemStatus().equals(OrderStatus.SCHEDULED)) {
    			return OrderStatus.SCHEDULED;
    		}
    	}
    	
    	return OrderStatus.FINISHED;	
    	
    }
    
    public List<OrderItem> getOpenOrderItems(){
		List<OrderItem> openItems = new ArrayList<OrderItem>(this.orderItems);
		for(int i = openItems.size()-1;i>=0;i--) {
			if(!openItems.get(i).getOrderItemStatus().equals(OrderStatus.OPEN)) {
				openItems.remove(i);
			}
		}
		return openItems;
		
	}
    /**
     * Will be true if there is at least one item in the order which is open.
     * @return
     */
    public boolean isOpen() {
    	return this.getStatus().equals(OrderStatus.OPEN);
    }
    
    /**
	 * Weight of the orderItems open for sheduling
	 * @return
	 */
	@Override
	public int getOpenWeight() {
		int w = 0;
		for(OrderItem oi: this.orderItems) {
			w += oi.getOpenWeight();
		}
		return w;
	}
	
	/**
	 * Weight of the orderItems open for scheduling
	 * @return
	 */
	@Override
	public int getOpenSize() {
		int s = 0;
		for(OrderItem oi: this.orderItems) {
			s += oi.getOpenSize();
		}
		return s;
	}
    
    /**
     * Sets the route of the open order items equal to r
     */
    @Override
    public void setRoute(Route r) {
    	for(OrderItem i: this.getOpenOrderItems()) {
    		i.setRoute(r);
    	}
    }

   /**
    * Sets the status of the remaining open order items to scheduled.
    */
    @Override
    public void schedule() throws InconsistentOrderStateException {
    	for(OrderItem oi: this.getOpenOrderItems()) {
    		oi.schedule();
    	}
    }
    @Override
    public void acceptDelivery() throws InconsistentOrderStateException {
    	for(OrderItem oi: this.orderItems) {
    		oi.acceptDelivery();
    	}
    }
    @Override
    public void rejectDelivery() throws InconsistentOrderStateException {
    	for(OrderItem oi: this.orderItems) {
    		oi.rejectDelivery();
    	}
    }
    public void setDeliveryDate(Date date) {

    	this.deliveryDate = CalendarService.setMidnight(date);
   
    }
    
    public Date getDeliveryDate() {
    	return deliveryDate;
    }
    public String getDeliveryDateStr() {
    	return CalendarService.format(this.getDeliveryDate());
    }


    public boolean invariant() {
    	if((deliveryDate!=null)&&(customer!=null)&&(this.orderItems!=null)) {
  
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
	
	public String toString() {
		return "Order "+id+" for "+this.getCustomer().getName();
	}


	@Override
	public int getSize() {
		int s = 0;
		for(OrderItem oi: this.orderItems) {
			s += oi.getSize();
		}
		return s;
	}
	
	public int getNumberOfItems() {
		return this.orderItems.size();
	}
    



	
}
