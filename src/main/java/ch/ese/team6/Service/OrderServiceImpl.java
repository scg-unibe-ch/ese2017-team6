package ch.ese.team6.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ese.team6.Model.Order;
import ch.ese.team6.Model.OrderStatus;
import ch.ese.team6.Repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

@Autowired private OrderRepository orderRepository;
	
	/**
	 * Returns a List with all the Dates where there is an open order in the system
	 * Each date only appears once. Dates are sorted in ascending way If no dates
	 * exist an empty list will be retured
	 * 
	 * @return
	 */
@Override
	public List<Date> findAllDatesWithOpenOrders() {
		ArrayList<Date> dates = new ArrayList<Date>();
		List<Order> orders = orderRepository.findAll();
		for (Order o : orders) {
			Date delDate = o.getDeliveryDate();
			if (o.getStatus().equals(OrderStatus.OPEN) && !dates.contains(delDate)) {
				dates.add(delDate);
			}
		}
		Collections.sort(dates);
		return dates;
	}

	@Override
	/**
	 * Returns the open Orders with Deliverydate equal to date
	 */
	public List<Order> findOpenOrders(Date date) {
		date = CalendarService.setMidnight(date);
		List<Order> orders = orderRepository.findAll();

		for (int i = orders.size() - 1; i >= 0; i--) {
			Order o = orders.get(i);
			if (!o.getStatus().equals(OrderStatus.OPEN) || date.before(o.getDeliveryDate())
					|| date.after(o.getDeliveryDate())) {
				orders.remove(o);
			}
		}
		return orders;
	}

}
