package ch.ese.team6.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import ch.ese.team6.Exception.InconsistentOrderStateException;

/**
 * A order consists of one or more orderItems
 * a orderItems consists of a item and a amount
 * 
 * OrderItems are assigned to a route for delivery
 * 
 */
@Entity
public class OrderItem implements IDelivarable {
	
	 	@Id
	    @GeneratedValue(strategy=GenerationType.AUTO)
	    private long id;
	 	@NotNull private OrderStatus orderItemStatus;
	 	@NotNull private int amount;
	 	
		
	 	@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="ITEMS_ID")
	 	@NotNull private Item item;
	 	
	 	
	 	@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="ORDERS_ID")
	 	private Order orders;
	 	

	 	@ManyToOne(fetch=FetchType.LAZY,cascade = {CascadeType.ALL})
		@JoinColumn(name="ROUTE_ID")
	 	private Route route;
	 	
	 	public OrderItem() {
	 		super();
	 		
	 		this.orderItemStatus=OrderStatus.OPEN;
	 	}
	 	
	    
	 	public OrderItem(Order order) {
	 		this.orderItemStatus = OrderStatus.OPEN;
	 		this.orders = order;
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

		@Override
		public void setRoute(Route r) {
			this.route = r;
		}
		public Route getRoute() {
			return this.route;
		}

		public OrderStatus getOrderItemStatus() {
			return orderItemStatus;
		}

		public void setOrderItemStatus(OrderStatus orderItemStatus) {
			this.orderItemStatus = orderItemStatus;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}
		
		@Override
		public String toString() {
			return this.amount+" x "+this.item;
			
		}

		@Override
		public int getWeight() {
			return amount*item.getWeight();
		}

		@Override
		public int getSize() {
			return amount*item.getRequiredSpace();
		}
		
		/**
		 * Returns the weight of the item if it has been delivered
		 * and zero if is still open
		 */
		@Override
		public int getOpenWeight() {
			if(this.orderItemStatus == OrderStatus.OPEN) {
				return this.getWeight();
			}
			return 0;
		}

		/**
		 * Returns the size of the item if it has been delivered
		 * and zero if is still open
		 */
		@Override
		public int getOpenSize() {
			if(this.orderItemStatus == OrderStatus.OPEN) {
				return this.getSize();
			}
			return 0;
		}

		@Override
		public Address getAddress() {
			return this.orders.getAddress();
		}
		
		public boolean hasRoute() {
			return route!=null;
		}
		public boolean invariant() {
			return amount>=0 && item!=null;
		}

		@Override
		public void schedule() throws InconsistentOrderStateException {
			if(!this.orderItemStatus.equals(OrderStatus.OPEN)) {
				throw new InconsistentOrderStateException("You can only schedule a open Order Item");
			}
			this.orderItemStatus = OrderStatus.SCHEDULED;
			
		}
		@Override
		public void acceptDelivery() throws InconsistentOrderStateException {
			if(!this.orderItemStatus.equals(OrderStatus.SCHEDULED)) {
				throw new InconsistentOrderStateException("You can only deliver a sheduled Order Item");
			}
			this.orderItemStatus = OrderStatus.FINISHED;
		}
		
		@Override
		public void rejectDelivery() throws InconsistentOrderStateException {
			if(!this.orderItemStatus.equals(OrderStatus.SCHEDULED)) {
				throw new InconsistentOrderStateException("You can only deliver a sheduled Order Item");
			}
			this.orderItemStatus = OrderStatus.OPEN;
		}
		
		
		

}
