package ch.ese.team6.models.items;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.validation.constraints.NotNull;


@Entity
public class Items {
	
	 	@Id
	    @GeneratedValue(strategy=GenerationType.AUTO)
	    private Integer id;
	 	@NotNull private String name;
	 	
	 	/**
	 	 * Constructor with parameter
	 	 * @param nameOfObject
	 	 */
	 	public Items(String nameOfObject)
	 	{
	 		this.name = nameOfObject;
	 	}
	    
	 	/**
	 	 * Empty Constructor
	 	 */
	 	public Items() {
	 		
	 	}
	 	
		public Integer getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
}

