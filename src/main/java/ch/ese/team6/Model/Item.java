package ch.ese.team6.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.validation.constraints.NotNull;


@Entity
public class Item {
	
	 	@Id
	    @GeneratedValue(strategy=GenerationType.AUTO)
	    private long id;
	 	@NotNull private String name;
	 	private int requiredAmountOfPalettes;
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

		public void setName(String name) {
			this.name = name;
		}
		public int getRequiredAmountOfPalettes() {
			return requiredAmountOfPalettes;
		}

		public void setRequiredAmountOfPalettes(int requiredAmountOfPalettes) {
			this.requiredAmountOfPalettes = requiredAmountOfPalettes;
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

		public void setWeight(int weight) {
			this.weight = weight;
		}
}

