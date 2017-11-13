package ch.ese.team6.Model;

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
	 	@NotNull private String name;
	 	@Min(value = 0)
	 	private int requiredAmountOfPalettes;
	 	@Min(value = 0)
	 	private int weight;
		@NotNull @Min(value = 0) @Max( value = 1)
		private int availability;
	 	/**
	 	 * Constructor with parameter
	 	 * @param nameOfObject
	 	 */
	 	public Item(String nameOfObject)
	 	{
	 		this.name = nameOfObject;
	 	}
	    
	 	/**
	 	 * Empty Constructor
	 	 */
	 	public Item() {
	 		this.availability = 0;
	 		
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
			return requiredAmountOfPalettes;
		}

		public void setRequiredSpace(int requiredSpace) throws BadSizeException{
			if (requiredSpace < 0) throw new BadSizeException("required space can't be negative.");
			this.requiredAmountOfPalettes = requiredSpace;
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
		
		public int getAvailability() {
			return this.availability;			
		}
		
		public String getAvailabilityAsString() {
			if (this.getAvailability() == 0)
				return "available";
			return "not available";
		}
				
		public void setAvailability(int availability) throws BadSizeException{
			if(!(availability == 0 || availability == 1)) throw new BadSizeException("availability has to be 1 or 0");
			this.availability = availability;
		}
}

