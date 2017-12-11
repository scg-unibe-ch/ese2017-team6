package ch.ese.team6.Model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import ch.ese.team6.Exception.BadSizeException;

@Entity 
@Table(name = "Customer")
public class Customer {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	@NotNull
    private String name;
	@NotNull
	private String phone;
	@NotNull
	private String email;
//	@NotNull @Min(value = 0) @Max( value = 1)
	private DataStatus status;
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ADDRESS_ID")
	@NotNull
    private Address address;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DOMICILADDRESS_ID")
	@NotNull
	private Address domiciladdress;

    
    public Customer() {
    	this.status = DataStatus.ACTIVE;
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
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public DataStatus getStatus() {
		return this.status;			
	}
			
	public void setStatus(DataStatus status) {
		this.status = status;
	}

	public void setId(long id) {
		this.id = id;
	}
	public Address getAddress() {
		return address;
	}
	/**
	 * Will set the Delivery address
	 * if address.isReachableByTruck() returns false, nothing will be done and an Exception is thrown
	 * if the customer does not yet have a domicil address the domicil address
	 * will be the same as the delivery address.
	 * @param address
	 */
	public void  setAddress(Address address) throws BadSizeException{
		if(!address.isReachableByTruck()) {throw new BadSizeException("You specified an invalid delivery Address");}
		this.address = address;
		if(this.domiciladdress==null) {
			this.domiciladdress=address;
		}
	}
	
	/**
	 * Sets the domicil address of the client, this is optional
	 * if it is not done the domicil address will be the delivery address.
	 * The domicil address does not necessarily need to be reachable by track.
	 * @param address
	 */
	public void setDomicilAddress(Address address) {
		this.domiciladdress=address;
	}
	
	public Address getDomicilAddress() {
		return domiciladdress;
	}
	
	@Override
	public String toString() {
		return name;
	}
	public boolean isOK() {
		return this.invariant();
	}
	private boolean invariant() {
		return name!=null && address!=null &&name.length()!=0 && address.isReachableByTruck()&&domiciladdress!=null;
	}


}


