package ch.ese.team6;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.util.DateUtils;

import ch.ese.team6.models.customers.Customer;
import ch.ese.team6.models.customers.CustomerRepository;
import ch.ese.team6.models.items.ItemRepository;
import ch.ese.team6.models.items.Items;
import ch.ese.team6.models.orders.OrderRepository;
import ch.ese.team6.models.orders.Orders;
import ch.ese.team6.models.users.UserRepository;
import ch.ese.team6.models.users.Users;

@SpringBootApplication
public class EseTeam6Application {

	public static void main(String[] args) {
		SpringApplication.run(EseTeam6Application.class, args);
	}
	
	@Bean
	CommandLineRunner initUsers(UserRepository userRepository) {
		return (evt) -> Arrays.asList("alvaro,dominic,mauro,nathalie,logista,brumBrumm".split(","))
				.forEach(a -> {Users user = userRepository.save(new Users(a,a, "password",0));});
	}

	
	@Bean
	CommandLineRunner initOrders(OrderRepository orderRepository) throws ParseException {
		
		return (evt) -> Arrays.asList("sbb,swisscom,ibm,postfinance,google,brumBrumm".split(","))
				.forEach(a -> {Orders order = orderRepository.save(new Orders(a,a+"'s Home", new Date()));});
	}
	
	@Bean
	CommandLineRunner initCustomers(CustomerRepository customerRepository) {
		return (evt) -> Arrays.asList("sbb,swisscom,ibm,postfinance,google,brumBrumm".split(","))
				.forEach(a -> {customerRepository.save(new Customer(a));});
	}
	
	@Bean
	CommandLineRunner initItems(ItemRepository itemRepository) {
		return (evt) -> Arrays.asList("maschine1,maschine2,werkzeug1,werkzeug2".split(","))
				.forEach(a -> {itemRepository.save(new Items(a));});
	}
}
