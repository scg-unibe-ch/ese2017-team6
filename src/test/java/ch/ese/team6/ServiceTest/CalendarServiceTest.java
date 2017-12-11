package ch.ese.team6.ServiceTest;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.ese.team6.Application;
import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.InvalidAddressException;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.Distance;
import ch.ese.team6.Service.AddressService;
import ch.ese.team6.Service.CalendarService;
import ch.ese.team6.Service.OurCompany;

public class CalendarServiceTest {
	
	
	@Test
	public void DateParserTest() {
		Date d = CalendarService.parseDate("2017-12-1");
		Calendar dc = Calendar.getInstance();
		dc.setTime(d);
		assertEquals(Calendar.DECEMBER,dc.get(Calendar.MONTH));
		assertEquals(1,dc.get(Calendar.DAY_OF_MONTH));
		assertEquals(2017,dc.get(Calendar.YEAR));
		
		d = CalendarService.parseDate("2017-1-2 13:25");
		dc.setTime(d);
		assertEquals(Calendar.JANUARY,dc.get(Calendar.MONTH));
		assertEquals(2,dc.get(Calendar.DAY_OF_MONTH));
		assertEquals(2017,dc.get(Calendar.YEAR));
		assertEquals(13,dc.get(Calendar.HOUR_OF_DAY));
		assertEquals(25,dc.get(Calendar.MINUTE));
		
		d = CalendarService.parseDate("Kein Datum");
		assertEquals(null,d);
	}
	@Test
	public void setMidnightTest() {
		Calendar c = Calendar.getInstance();
		c.set(2012, Calendar.NOVEMBER, 5);
		c.setTime(CalendarService.setMidnight(c.getTime()));
		assertEquals(Calendar.NOVEMBER,c.get(Calendar.MONTH));
		assertEquals(5,c.get(Calendar.DAY_OF_MONTH));
		assertEquals(2012,c.get(Calendar.YEAR));
		assertEquals(0,c.get(Calendar.HOUR_OF_DAY));
		assertEquals(0,c.get(Calendar.MINUTE));
		assertEquals(0,c.get(Calendar.SECOND));
		
	}
	@Test
	public void setHourTest() {
		Calendar c = Calendar.getInstance();
		c.set(2013, Calendar.SEPTEMBER, 10);
		c.setTime(CalendarService.setHour(c.getTime(),13));
		assertEquals(Calendar.SEPTEMBER,c.get(Calendar.MONTH));
		assertEquals(10,c.get(Calendar.DAY_OF_MONTH));
		assertEquals(2013,c.get(Calendar.YEAR));
		assertEquals(13,c.get(Calendar.HOUR_OF_DAY));
		assertEquals(0,c.get(Calendar.MINUTE));
		assertEquals(0,c.get(Calendar.SECOND));
		
	}
	@Test
	public void getWorkingStartTest() {
		Calendar c = Calendar.getInstance();
		c.set(2013, Calendar.SEPTEMBER, 10);
		c.setTime(CalendarService.getWorkingStart(c.getTime()));
		assertEquals(Calendar.SEPTEMBER,c.get(Calendar.MONTH));
		assertEquals(10,c.get(Calendar.DAY_OF_MONTH));
		assertEquals(2013,c.get(Calendar.YEAR));
		assertEquals(OurCompany.WORKDAYSTART,c.get(Calendar.HOUR_OF_DAY));
		assertEquals(0,c.get(Calendar.MINUTE));
		assertEquals(0,c.get(Calendar.SECOND));
		
	}
	@Test
	public void getWorkingEndTest() {
		Calendar c = Calendar.getInstance();
		c.set(2013, Calendar.SEPTEMBER, 10);
		c.setTime(CalendarService.getWorkingEnd(c.getTime()));
		assertEquals(Calendar.SEPTEMBER,c.get(Calendar.MONTH));
		assertEquals(10,c.get(Calendar.DAY_OF_MONTH));
		assertEquals(2013,c.get(Calendar.YEAR));
		assertEquals(OurCompany.WORKDAYSTOP,c.get(Calendar.HOUR_OF_DAY));
		assertEquals(0,c.get(Calendar.MINUTE));
		assertEquals(0,c.get(Calendar.SECOND));
		
	}
	@Test
	public void getWorkingStartCalendarTest() {
		Calendar c = Calendar.getInstance();
		c.set(2013, Calendar.SEPTEMBER, 10);
		c = (CalendarService.getWorkingStart(c));
		assertEquals(Calendar.SEPTEMBER,c.get(Calendar.MONTH));
		assertEquals(10,c.get(Calendar.DAY_OF_MONTH));
		assertEquals(2013,c.get(Calendar.YEAR));
		assertEquals(OurCompany.WORKDAYSTART,c.get(Calendar.HOUR_OF_DAY));
		assertEquals(0,c.get(Calendar.MINUTE));
		assertEquals(0,c.get(Calendar.SECOND));
		
	}
	@Test
	public void getWorkingEndCalendarTest() {
		Calendar c = Calendar.getInstance();
		c.set(2013, Calendar.SEPTEMBER, 10);
		c = CalendarService.getWorkingEnd(c);
		assertEquals(Calendar.SEPTEMBER,c.get(Calendar.MONTH));
		assertEquals(10,c.get(Calendar.DAY_OF_MONTH));
		assertEquals(2013,c.get(Calendar.YEAR));
		assertEquals(OurCompany.WORKDAYSTOP,c.get(Calendar.HOUR_OF_DAY));
		assertEquals(0,c.get(Calendar.MINUTE));
		assertEquals(0,c.get(Calendar.SECOND));
		
	}
	
	
	@Test
	public void computeTaskEndTest() {
		/**
		 * Here we test a task starting at 1 in the morning (i. e. it will start effectively at OurCompany.Workdaystart
		 */
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(2013, Calendar.SEPTEMBER, 10);
		c.set(Calendar.HOUR, 1);
		
		c.setTime(CalendarService.computeTaskEnd(c.getTime(), 1000*60*60));//Task takes exactly one hour and starts
		assertEquals(Calendar.SEPTEMBER,c.get(Calendar.MONTH));
		assertEquals(10,c.get(Calendar.DAY_OF_MONTH));
		assertEquals(2013,c.get(Calendar.YEAR));
		assertEquals(OurCompany.WORKDAYSTART+1,c.get(Calendar.HOUR_OF_DAY));
		assertEquals(0,c.get(Calendar.MINUTE));
		assertEquals(0,c.get(Calendar.SECOND));
		
	}
	
	
	@Test
	public void computeTaskEndTest2() {
		/**
		 * Here we test a task starting at 10 in the morning and taking 2 wokdays 3 hour and 15 minutes
		 */
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(2013, Calendar.SEPTEMBER, 10);
		c.set(Calendar.HOUR, 10);
		
		long duration = 2*(OurCompany.WORKDAYSTOP-OurCompany.WORKDAYSTART)*60*60*1000+3*60*60*1000+15*60*1000;
		c.setTime(CalendarService.computeTaskEnd(c.getTime(), duration));//Task takes exactly one hour and starts
		assertEquals(Calendar.SEPTEMBER,c.get(Calendar.MONTH));
		assertEquals(10+2,c.get(Calendar.DAY_OF_MONTH));
		assertEquals(2013,c.get(Calendar.YEAR));
		assertEquals(10+3,c.get(Calendar.HOUR_OF_DAY));
		assertEquals(15,c.get(Calendar.MINUTE));
		assertEquals(0,c.get(Calendar.SECOND));
		
	}
	@Test
	public void intersectsTest() {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(2013, Calendar.SEPTEMBER, 10);
		c.set(Calendar.HOUR, 10);
		
		Date start = c.getTime();//2013-09-10 10:00
		c.add(Calendar.HOUR, 1);
		Date test = c.getTime();
		c.add(Calendar.HOUR,1);
		Date end = c.getTime();
		assertTrue(CalendarService.intersects(start, end, test));
		assertFalse(CalendarService.intersects(start, test, end));
		assertTrue(CalendarService.intersects(end, end, end));
		assertTrue(CalendarService.intersects(start, end, end));
		
	}
	
	@Test
	public void formatHTest() {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(2013, Calendar.SEPTEMBER, 10);
		c.set(Calendar.HOUR, 21);
		
		assertEquals("2013-09-10 21:00",CalendarService.formatH(c.getTime()));
		c.set(Calendar.MINUTE, 34);
		assertEquals("2013-09-10 21:34",CalendarService.formatH(c.getTime()));
		
	}
	@Test
	public void formatMinutesTest() {
		long min = 60;
		
		assertEquals("1 hour, 0 minutes",CalendarService.formatMinutes(min));
		min = 61;
		assertEquals("1 hour, 1 minute",CalendarService.formatMinutes(min));
		min = 119;
		assertEquals("1 hour, 59 minutes",CalendarService.formatMinutes(min));
		min = 121;
		assertEquals("2 hours, 1 minute",CalendarService.formatMinutes(min));
		min = 32;
		assertEquals("32 minutes",CalendarService.formatMinutes(min));
		min = 0;
		assertEquals("0 minutes",CalendarService.formatMinutes(min));
		
		
	}

	
	}
