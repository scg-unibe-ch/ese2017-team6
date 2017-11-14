package ch.ese.team6.Service;

import java.util.Calendar;
import java.util.Date;

public class CalendarService {

	/**
	 * Returns a new date with the same day of month, month and year as the Date date but without hours, minutes, seconds, milliseconds
	 * @param date
	 * @return
	 */
	public static Date setMidnight(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		Calendar ret = Calendar.getInstance();
		ret.clear();
		ret.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		return ret.getTime();
	}
	
	
}
