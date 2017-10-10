package ch.ese.team6.models.items;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * All Items from the library of the firm
 * @author mauro
 *
 */
@Entity
public class Item {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
 	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
