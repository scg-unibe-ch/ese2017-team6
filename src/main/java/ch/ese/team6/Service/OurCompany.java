package ch.ese.team6.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ese.team6.Model.Address;
import ch.ese.team6.Repository.AddressRepository;

@Service
public class OurCompany {
	
	@Autowired AddressRepository addressRepository; 
	
	/**
	 * The hour where a work day starts
	 */
	public static final int WORKDAYSTART = 8;
	/**
	 * The hours where a work day stops 17 means the work day
	 * stops 16:59
	 */
	public static final int WORKDAYSTOP = 17;
	
	/**
	 * The address Id of the deposit
	 */

	public static long depositId;;
	
	public static void setDepositId(long id) {
		depositId = id;
	}
}
