package ch.ese.team6.Helper;

import java.util.Date;
import java.util.List;

import ch.ese.team6.Model.Customer;
import ch.ese.team6.Model.Item;
import ch.ese.team6.Repository.CustomerRepository;
import ch.ese.team6.Repository.ItemRepository;

public class OrderHelper {
	
	private Customer customer;
	private long customerId;
	private List<Item> items;
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
	
	public List<Item> getItems() {
		return items;
	}

}
