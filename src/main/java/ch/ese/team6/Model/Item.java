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
		//@Column(name = "status")
		private Status status;
		public enum Status {
			AVAILABLE(0, "available"), UNAVAILABLE(1, "not available");
			
			private int value;
			private String title;
			public static final Status[] ALL = { AVAILABLE, UNAVAILABLE };
			
			private Status (int value, String title) {
				this.value = value;
				this.title = title;
			}
			
			public int getValue() {
				return value;
			}
			
			public String getTitle() {
				return title;
			}
			
			public void setValue(int value) {
				this.value = value;
			}
						
			public static String getByValue(int value) {
				for(Status s : Status.values()) {
					if (value == s.getValue())
						return "AVAILABLE";
				}
				return "UNAVAILABLE";
			}
		}
	 	/**
	 	 * Constructor with parameter
	 	 * @param nameOfObject
	 	 */
	 	public Item(String nameOfObject)
	 	{
	 		this.name = nameOfObject;
	 		this.status = Status.AVAILABLE;
	 	}
	    
	 	/**
	 	 * Empty Constructor
	 	 */
	 	public Item() {
	 		this.status = Status.AVAILABLE;
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
		
		public Status getStatus() {
			return this.status;			
		}
				
		public void setStatus(Status status) {
			this.status = status;
		}
}

