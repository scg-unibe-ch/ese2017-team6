package ch.ese.team6.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Distance {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	@JoinColumn(name="origin")
	private Address origin;
	
	@ManyToOne
	@JoinColumn(name="destination")
	private Address destination;
	
	@NotNull
	private long distanceMetres;
	
	@NotNull
	private long durationSeconds;
	
}
