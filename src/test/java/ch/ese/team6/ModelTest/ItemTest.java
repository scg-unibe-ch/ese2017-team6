package ch.ese.team6.ModelTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Model.Item;
public class ItemTest {
@Test
public void setupTest() throws BadSizeException {
	Item item = new Item();
	item.setName("Computer");
	item.setRequiredSpace(10);
	item.setWeight(20);
	assertEquals("Computer",item.getName());
	assertEquals(10,item.getRequiredSpace());
	assertEquals(20,item.getWeight());
}
	
@Test(expected = BadSizeException.class)
public void testBadSize() throws BadSizeException {
	Item item = new Item();
	item.setName(" ");
	item.setRequiredSpace(-1);
}
	
	
}
