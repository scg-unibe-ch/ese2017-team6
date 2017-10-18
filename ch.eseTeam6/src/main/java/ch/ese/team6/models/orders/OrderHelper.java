package ch.ese.team6.models.orders;

import java.util.Date;
import java.util.List;

import ch.ese.team6.models.customers.Customer;
import ch.ese.team6.models.customers.CustomerRepository;
import ch.ese.team6.models.items.ItemRepository;
import ch.ese.team6.models.items.Items;

public class OrderHelper {
	
	private Customer customer;
	private long customerId;
	private List<Items> items;
	private Date deliveryTime;
	
	private CustomerRepository customerRepository;
	private ItemRepository itemRepository;
	
	public OrderHelper() {
		 this.items = itemRepository.findAll();
	}
	
	public void setDeliveryTime(Date date) {
		this.deliveryTime = date;
	}
	
	public Date getDeliveryTime() {
		return deliveryTime;
	}
	
	public long getCustomerId() {
		return customerId;
	}
	
	public List<Items> getItems() {
		return items;
	}

}
