package models.Items;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import Address.Address;

@Entity
public class Items {
	
	 	@Id
	    @GeneratedValue(strategy=GenerationType.AUTO)
	    private Integer id;
	 	private String name;
	    private Address address;
	    private status stat = status.notdelivered;
	    
	    private enum status{notdelivered, onroute, delivered};
	    
	    public void switchStatus() {
	    	
	    	if(this.stat.equals(status.notdelivered))
	    		stat = status.onroute;
	    	
	    	if(this.stat.equals(status.onroute))
	    		stat = status.delivered;
	    }
	    
	    
	    public status getStatus() {
			return stat;
		}

		public void setStatus(status status) {
			this.stat = status;
		}
	    
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Address getAddress() {
			return address;
		}

		public void setAddress(Address address) {
			this.address = address;
		}

	
	

}
