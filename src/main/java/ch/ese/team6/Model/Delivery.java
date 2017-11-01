package ch.ese.team6.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="delivery")
public class Delivery {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String address;
	private String item;
	@OneToMany
	private Set<Item> items;
	@OneToOne
	private Order order;
	
	
	public Delivery() {}


	public Delivery(Order order) {
		this.order = order;
		this.address = order.getAddress().toString();
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getItem() {
		return item;
	}


	public void setItem(String item) {
		this.item = item;
	}


	public void addItem(Item item) {
		this.items.add(item);
	}
	
	public boolean hasItems(){
		try {
			return !this.items.isEmpty();
		}
		catch(NullPointerException exception){
			return false;
		}
	}
	
	public List<OrderItem> getItems(){
		return this.order.getOrderItems();
		/*if (!this.hasItems()) return null;
		List<Item> deliveryItems = new ArrayList<Item>(this.items);
		return deliveryItems;*/
	}
	
	public Order getOrder() {
		return this.order;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}
	
}
