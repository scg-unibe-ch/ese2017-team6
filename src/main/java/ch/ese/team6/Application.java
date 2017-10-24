package ch.ese.team6;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ch.ese.team6.Model.Truck;
import ch.ese.team6.Model.User;
import ch.ese.team6.Repository.TruckRepository;
import ch.ese.team6.Repository.UserRepository;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner initUsers(UserRepository userRepository) {
		return (evt) -> Arrays.asList("alvaro,dominic,mauro,nathalie,logista,brumBrumm".split(","))
				.forEach(a -> {User user = userRepository.save(new User(a));});
	}
	
	@Bean
	CommandLineRunner initDrivers(UserRepository userRepository) {
		return (evt) -> Arrays.asList("Driver1, Driver2, Driver3".split(","))
				.forEach(a -> {User user = userRepository.save(new User(a));});
	}

	/**
	@Bean
	CommandLineRunner initOrders(OrderRepository orderRepository) throws ParseException {
		
		return (evt) -> Arrays.asList("sbb,swisscom,ibm,postfinance,google,brumBrumm".split(","))
				.forEach(a -> {Orders order = orderRepository.save(new Orders(a,a+"'s Home", new Date()));});
	}
	
	/** Auskommentiert, da für das erstellen eines Kunden eine Adresse nötig ist.
	@Bean
	CommandLineRunner initCustomers(CustomerRepository customerRepository) {
	
		return (evt) -> Arrays.asList("sbb,swisscom,ibm,postfinance,google,brumBrumm".split(","))
				.forEach(
						a -> {customerRepository.save(new Customer(a,null));
				});
	}
	*/
	/*
	@Bean
	CommandLineRunner initAddress(AddressRepository addressRepository) {
		return (evt) -> Arrays.asList("1,2,3,4,5,6".split(","))
				.forEach(a -> {addressRepository.save(new Address("Haupstrasse "+a,"300"+a+" Bern"));});
	}
	*/
	/*
	@Bean
	CommandLineRunner initItems(ItemRepository itemRepository) {
		return (evt) -> Arrays.asList("maschine1,maschine2,werkzeug1,werkzeug2".split(","))
				.forEach(a -> {itemRepository.save(new Items(a));});
	}
	*/
	@Bean
	CommandLineRunner initTrucks(TruckRepository truckRepository) {
		return (evt) -> Arrays.asList("Iveco1,Iveco2,Iveco3,Iveco4".split(","))
				.forEach(a -> {truckRepository.save(new Truck(a));});
	}
}
