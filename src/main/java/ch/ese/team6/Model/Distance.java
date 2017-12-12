package ch.ese.team6.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import ch.ese.team6.Service.CalendarService;

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
	
	public Distance() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Address getOrigin() {
		return origin;
	}

	public void setOrigin(Address origin) {
		this.origin = origin;
	}

	public Address getDestination() {
		return destination;
	}

	public void setDestination(Address destination) {
		this.destination = destination;
	}

	public long getDistanceMetres() {
		return distanceMetres;
	}

	public void setDistanceMetres(long distanceMetres) {
		this.distanceMetres = distanceMetres;
	}

	public long getDurationSeconds() {
		return durationSeconds;
	}

	public void setDurationSeconds(long durationSeconds) {
		this.durationSeconds = durationSeconds;
	}
	
	public String getDurationStr() {
		return CalendarService.formatMinutes(durationSeconds/60);
	}
	public String getDistanceStr() {
		return (this.distanceMetres/1000)+" km";
	}
	
	
}
