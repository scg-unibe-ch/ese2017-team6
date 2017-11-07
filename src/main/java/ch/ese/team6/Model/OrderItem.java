package ch.ese.team6.Model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class OrderItem {
	
	 	@Id
	    @GeneratedValue(strategy=GenerationType.AUTO)
	    private long id;
	 	@NotNull private String orderItemStatus;
	 	@NotNull private int amount;
	 	
		
	 	@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="ITEMS_ID")
	 	@NotNull private Item item;
	 	
	 	
	 	@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="ORDERS_ID")
	 	private Order orders;
	 	
	 	/**
	 	 * Constructor with parameter
	 	 * @param item, amount
	 	 */
	 	public OrderItem(Item item,int amount)
	 	{
	 		this.item= item;
	 		this.orderItemStatus = "not delivered";
	 		this.amount = amount;
	 	}
	 	
	 	/**
	 	 * Empty Constructor
	 	 */
	 	public OrderItem()
	 	{
	 		this.orderItemStatus = "not delivered";
	 	}
	    
	 	public OrderItem(Order order) {
	 		this.orderItemStatus = "not delivered";
	 		this.orders = order;
		}

		public OrderItem(Order o, Item items, int i) {
			this.orderItemStatus = "not delivered";
	 		this.orders = o;
	 		this.item = items;
	 		this.amount = i;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public Item getItem() {
			return this.item;
		}

		public void setItem(Item itemId) {
			this.item = itemId;
		}

		public Order getOrder() {
			return this.orders;
		}
		
		public void setOrder(Order o) {
			this.orders = o;
		}


		public String getOrderItemStatus() {
			return orderItemStatus;
		}

		public void setOrderItemStatus(String orderItemStatus) {
			this.orderItemStatus = orderItemStatus;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}
		
		public String toString() {
			return this.amount+" x "+this.item;
			
		}
		

}
