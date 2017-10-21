package ch.ese.team6.models.orderitems;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import ch.ese.team6.models.items.Items;
import ch.ese.team6.models.orders.Orders;


@Entity
public class OrderItems {
	
	 	@Id
	    @GeneratedValue(strategy=GenerationType.AUTO)
	    private long id;
	 	@NotNull private String orderItemStatus;
	 	@NotNull private int amount;
	 	
		
	 	@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="ITEMS_ID")
	 	@NotNull private Items item;
	 	
	 	
	 	@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="ORDERS_ID")
	 	@NotNull private Orders order;
	 	/**
	 	 * Constructor with parameter
	 	 * @param item, amount
	 	 */
	 	public OrderItems(Items item,int amount)
	 	{
	 		this.item= item;
	 		this.orderItemStatus = "not delivered";
	 		this.amount = amount;
	 	}
	 	
	 	/**
	 	 * Empty Constructor
	 	 */
	 	public OrderItems()
	 	{
	 		this.orderItemStatus = "not delivered";
	 	}
	    
	 	public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public Items getItem() {
			return this.item;
		}

		public void setItem(Items itemId) {
			this.item = itemId;
		}

		public Orders getOrder() {
			return this.order;
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

}
