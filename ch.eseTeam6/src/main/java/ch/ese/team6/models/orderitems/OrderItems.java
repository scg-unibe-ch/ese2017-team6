package ch.ese.team6.models.orderitems;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.validation.constraints.NotNull;


@Entity
public class OrderItems {
	
	 	@Id
	    @GeneratedValue(strategy=GenerationType.AUTO)
	    private long id;
	 	@NotNull private long ItemId;
	 	@NotNull private long OrderId;
	 	@NotNull private String orderItemStatus;
	 	
	 	/**
	 	 * Constructor with parameter
	 	 * @param itemid, orderid
	 	 */
	 	public OrderItems(long itemid, long orderid)
	 	{
	 		this.ItemId = itemid;
	 		this.OrderId = orderid;
	 		this.orderItemStatus = "not delivered";
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

		public long getItemId() {
			return ItemId;
		}

		public void setItemId(long itemId) {
			ItemId = itemId;
		}

		public long getOrderId() {
			return OrderId;
		}

		public void setOrderId(long orderId) {
			OrderId = orderId;
		}

		public String getOrderItemStatus() {
			return orderItemStatus;
		}

		public void setOrderItemStatus(String orderItemStatus) {
			this.orderItemStatus = orderItemStatus;
		}

}
