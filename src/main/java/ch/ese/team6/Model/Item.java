package ch.ese.team6.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Model.DataStatus;

/**
 * Items are the objects our store sells and holds in its deposit.
 * A Item has a name a weight and a size
 * size may be zero to account for those items the customer told us
 * that "they always fit into the truck"
 */
@Entity
public class Item {
	
	 	@Id
	    @GeneratedValue(strategy=GenerationType.AUTO)
	    private long id;
	 	@NotNull @Column(name = "name", unique = true)
	 	private String name;
	 	@Min(value = 0) @Max(value = 40) //max tons of trucks load capacity
	 	private int requiredSpace;
	 	@Min(value = 0)
	 	private int weight;
		@Column(name = "status")
		private DataStatus status;
		
	 	/**
	 	 * Empty Constructor
	 	 */
	 	public Item() {
	 		this.status = DataStatus.ACTIVE;
	 	}
	 	
		public long getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) throws BadSizeException{
			if (name.trim().isEmpty()) throw new BadSizeException("Itemname can't be empty");
			this.name = name;
		}
		public int getRequiredSpace() {
			return requiredSpace;
		}

		public void setRequiredSpace(int requiredSpace) throws BadSizeException{
			if (requiredSpace < 0) throw new BadSizeException("required space can't be negative.");
			this.requiredSpace = requiredSpace;
		}

		public void setId(long id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return name;
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) throws BadSizeException {
			if (weight < 0) throw new BadSizeException ("weight can't be negative.");
			this.weight = weight;
		}
		
		public DataStatus getStatus() {
			return this.status;			
		}
				
		public void setStatus(DataStatus status) {
			this.status = status;
		}
}

