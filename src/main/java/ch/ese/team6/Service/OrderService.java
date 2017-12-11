package ch.ese.team6.Service;

import java.util.Date;
import java.util.List;


import ch.ese.team6.Model.Order;

public interface OrderService {
	public List<Date> findAllDatesWithOpenOrders();

	public List<Order> findOpenOrders(Date date);

}
