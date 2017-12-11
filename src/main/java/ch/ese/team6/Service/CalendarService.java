package ch.ese.team6.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * This class static formatters and parsers for dates and time.
 * Moreover it provides static methods to do timetable computations like: "When does a worker complete a task 
 * that needs 8 hours and 40 Minutes when he works between 10am and 4pm every day.
 * 
 */
public class CalendarService {

	/**
	 * Tries to parse the String s using 
	 * first yyyy-MM-dd HH:mm
	 * then yyyy-MM-dd
	 * and if this fails
	 * dd-MM-yyyy
	 * if this fails null is returned.
	 * @param stringToParse
	 * @return
	 */
	public static Date parseDate(String stringToParse) {
		
		SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date date = parser.parse(stringToParse);
			return date;
		} catch (Exception e) {
		}


		 parser = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = parser.parse(stringToParse);
			return date;
		} catch (Exception e) {
		}

		parser = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date date = parser.parse(stringToParse);
			return date;
		} catch (Exception e) {
		}

		return null;
		
	}

	/**
	 * Returns a new date with the same day of month, month and year as the Date
	 * date but without hours, minutes, seconds, milliseconds
	 * 
	 * @param date
	 * @return
	 */
	public static Date setMidnight(Date date) {
		return CalendarService.setHour(date, 0);
	}

	/**
	 * Returns a new date with the same day of month, month and year as the Date
	 * date but hour set to hour. Minutes and seconds are cleared.
	 * If hour is not between 0 and 23 we calculate it modulo 24
	 * 
	 * @param date, hour
	 * @return
	 */

	public static Date setHour(Date date, int hour) {
		hour = hour % 24;
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		Calendar ret = Calendar.getInstance();
		ret.clear();
		ret.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		ret.set(Calendar.HOUR_OF_DAY, hour);
		return ret.getTime();
	}

	/**
	 * Returns a new Date with the same Calendar day but with cleared minutes and
	 * seconds and with the hour set to the Wokring time start of the company.
	 * 
	 * @param date
	 * @return
	 */

	public static Date getWorkingStart(Date date) {
		return CalendarService.setHour(date, OurCompany.WORKDAYSTART);
	}

	public static Date getWorkingEnd(Date date) {
		return CalendarService.setHour(date, OurCompany.WORKDAYSTOP);
	}

	/**
	 * Same method as getWorkingEnd but with Calendar
	 */
	public static Calendar getWorkingEnd(Calendar date) {
		Calendar ret = Calendar.getInstance();
		ret.setTime(CalendarService.getWorkingEnd(date.getTime()));
		return ret;
	}

	/**
	 * Same method as getWorkingEnd but with Calendar
	 */
	public static Calendar getWorkingStart(Calendar date) {
		Calendar ret = Calendar.getInstance();
		ret.setTime(CalendarService.getWorkingStart(date.getTime()));
		return ret;
	}

	/**
	 * Computes the date where a Task starting at taskStart and taking milliSeconds
	 * time ends when a worker only works during the Working Hours specified in
	 * OurCompany
	 * 
	 * @param taskStart
	 * @param milliSeconds
	 * @return
	 */
	public static Date computeTaskEnd(Date taskStart, long milliSeconds) {

		long remainingTaksLength = milliSeconds;
		if(taskStart.before(CalendarService.getWorkingStart(taskStart))) {
			taskStart = CalendarService.getWorkingStart(taskStart);
		}
		
		Calendar taskEnd = Calendar.getInstance();
		taskEnd.setTime(taskStart);
		while (remainingTaksLength > 0) {
			long capacityToday = Math
					.max(CalendarService.getWorkingEnd(taskEnd).getTimeInMillis() - taskEnd.getTimeInMillis(), 0);

			long allocatedToday = Math.min(capacityToday, remainingTaksLength);
			remainingTaksLength -= allocatedToday;
			taskEnd.setTimeInMillis(taskEnd.getTimeInMillis() + allocatedToday);

			// Task not finished, we need to increase a day
			if (remainingTaksLength > 0) {
				taskEnd.add(Calendar.DAY_OF_YEAR, 1);
				taskEnd = CalendarService.getWorkingStart(taskEnd);
			}

		}
		return taskEnd.getTime();
	}

	/**
	 * Returns true if and only if dateToCheck>=start && dateToCheck<=end
	 * 
	 * @param start
	 * @param end
	 * @param dateToCheck
	 * @return
	 */
	public static boolean intersects(Date start, Date end, Date dateToCheck) {
		return dateToCheck.getTime() >= start.getTime() && dateToCheck.getTime() <= end.getTime();
	}

	/**
	 * Formats the date showing the Day and the hour and minute
	 * @param routeStartDate
	 * @return
	 */
	public static String formatH(Date routeStartDate) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(routeStartDate);
	}
	
	

	public static String formatMinutes(long minutes) {
		
		int h =  ((int) minutes) / 60;
		
		
		int m = ((int) minutes) % 60;
		
		
		
		String result = "";
		
		if(h>0) {
			result = h+" hour"+((h!=1) ? "s" : "")+", ";
		}
		
	
		result = result+m+" minute"+((m!=1) ? "s" : "");
		
		return result;
		
		}

	/**
	 * Format date without hours and minutes
	 * @param deliveryDate
	 * @return
	 */
	public static String format(Date date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

}
