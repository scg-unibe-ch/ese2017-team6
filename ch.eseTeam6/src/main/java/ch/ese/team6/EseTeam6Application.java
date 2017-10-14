package ch.ese.team6;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ch.ese.team6.models.customers.Customer;
import ch.ese.team6.models.customers.CustomerRepository;
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
	CommandLineRunner initOrders(OrderRepository orderRepository) {
		return (evt) -> Arrays.asList("alvaro,dominic,mauro,nathalie,logista,brumBrumm".split(","))
				.forEach(a -> {Orders order = orderRepository.save(new Orders(a,a+"'s Home"));});
	}
	
	@Bean
	CommandLineRunner initCustomers(CustomerRepository customerRepository) {
		return (evt) -> Arrays.asList("sbb,swisscom,ibm,postfinance,google,brumBrumm".split(","))
				.forEach(a -> {customerRepository.save(new Customer(a));});
	}
}
