package ch.ese.team6.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
}

