package ch.ese.team6.Service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.Customer;
import ch.ese.team6.Model.Item;
import ch.ese.team6.Model.Order;
import ch.ese.team6.Model.OrderItem;
import ch.ese.team6.Model.Role;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Model.Truck;
import ch.ese.team6.Model.User;
import ch.ese.team6.Repository.AddressRepository;
import ch.ese.team6.Repository.CustomerRepository;
import ch.ese.team6.Repository.DeliveryRepository;
import ch.ese.team6.Repository.ItemRepository;
import ch.ese.team6.Repository.OrderRepository;
import ch.ese.team6.Repository.RoleRepository;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Repository.TruckRepository;
import ch.ese.team6.Repository.UserRepository;

@Service
public class SampleDataServiceImpl implements SampleDataService{
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private TruckRepository truckRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired 
	private RouteRepository routeRepository;
	@Autowired
	private DeliveryRepository deliveryRepository;
	@Autowired
	private RoleRepository roleRepository;
	
	public void loadData() {
		if (roleRepository.count() == 0) this.loadRoles();
		if (userRepository.count() == 0) this.loadUsers();
		if (customerRepository.count() == 0)this.loadCustomers();
		if (itemRepository.count() == 0)this.loadItems();
		if (truckRepository.count() == 0)this.loadTrucks();
		if (orderRepository.count() == 0)this.loadOrders();
		if (routeRepository.count() == 0)this.loadRoutes();
	}
	
	public void loadRoles() {
		Role logistician = new Role();
		logistician.setName("LOGISTICS");
		Role driver = new Role();
		driver.setName("DRIVER");
		roleRepository.save(logistician);
		roleRepository.save(driver);
	}
	
	public void loadUsers() {
		String[] users = userCsv.split(";");
		for(int i = 0; i <  10 /*users.length*/; i++) {
			String[] userdata = users[i].split(",");
			User user = new User();
			user.setUsername((userdata[0].toLowerCase()+ userdata[1].substring(0, 1).toLowerCase()).trim());
			user.setFirstname(userdata[0]);
			user.setSurname(userdata[1]);
			user.setEmail(userdata[3]);
			user.setPhoneNumber(userdata[2]);
			user.setPassword("password");
			user.setPasswordConfirm("password");
			user.setRoles(roleRepository.findByName(i < 5 ? "LOGISTICS": "DRIVER"));
			userService.save(user);	
		}
	}
	
	public void loadCustomers() {
		String[] customers = customerCsv.split(";");
		for (int i = 0; i < 10/*customers.length*/; i++) {
			String[] customerData = customers[i].split(",");
			Customer customer = new Customer();
			Address address = new Address();
			customer.setName(customerData[0]);
			customer.setPhone(customerData[3]);
			customer.setEmail(customerData[4]);
			String[] addressData = customerData[1].split(":");
			address.setStreet(addressData[0]);
			address.setCity(addressData[1]);
			address.setCountry(customerData[2]);
			customer.setAddress(address);
			addressRepository.save(address);
			customerRepository.save(customer);
		}
		
	}
	
	public void loadItems() {
		String[] items = itemCsv.split(";");
		for (int i = 0; i < 10/*items.length*/; i++) {
			String[] itemData = items[i].split(",");
			Item item = new Item();
			item.setName(itemData[0]);
			item.setRequiredAmountOfPalettes(Integer.parseInt(itemData[1]));
			itemRepository.save(item);
		}
	}
	
	public void loadTrucks() {
		String[] trucks = truckCsv.split(";");
		for (int i = 0; i < 10 /*trucks.length*/; i++) { 
			String[] truckData = trucks[i].split(",");
			Truck truck = new Truck();
			truck.setTruckname(truckData[0]);
			truck.setMaxCargoSpace(Integer.parseInt(truckData[1].trim()));
			truckRepository.save(truck);
		}
	}
	
	public void loadOrders() {
		for (int i= 0;i<10; i++) {
		int n_cus = customerRepository.findAll().size();
		Customer cus = customerRepository.findAll().get((int) (Math.random()*n_cus));
		Date deliveryDate = Calendar.getInstance().getTime();
		
		Order order = new Order();
		order.setCustomer(cus);
		order.setDeliveryDate(deliveryDate);
			
		for(int j=0;j<(int)(Math.random()*10+1);j++){
			OrderItem oi = new OrderItem(order);
			order.getOrderItems().add(oi);
			oi.setAmount((int)(Math.random()*10));
			int n_item = itemRepository.findAll().size();
			oi.setItem(itemRepository.findAll().get((int) (Math.random()*n_item)));	
			}

			orderRepository.save(order);
		}
	}
	
	public void loadRoutes() {
		for (long i= 1;i<4; i++) {
			String date = "2017-11-01";
			Route route = new Route(date);
			route.setDriver(userRepository.findOne((long)i));
			route.setTruck(truckRepository.findOne((long)i));
			routeRepository.save(route);
		}
	}
	private String userCsv = "Ivan,Mann,031 843 72 24,mann@example.com;" + 
			"Lee,Fisher,031 843 72 25,fisher@example.com;" + 
			"Shannon,Guzman,031 843 72 26,guzman@example.com;" + 
			"Cathy,Maldonado,031 843 72 27,maldonado@example.com;" + 
			"Sylvia,Carter,031 843 72 28,carter@example.com;" + 
			"Raquel,Fernandez,031 843 72 29,fernandez@example.com;" + 
			"Sheryl,Fox,031 843 72 30,fox@example.com;" + 
			"Linda,Ray,031 843 72 31,ray@example.com;" + 
			"Eleanor,Castillo,031 843 72 32,castillo@example.com;" + 
			"Celia,Stokes,031 843 72 33,stokes@example.com;" + 
			"Emma,Ball,031 843 72 34,ball@example.com;" + 
			"Eloise,Keller,031 843 72 35,keller@example.com;" + 
			"Guadalupe,Stone,031 843 72 36,stone@example.com;" + 
			"Isaac,Clarke,031 843 72 37,clarke@example.com;" + 
			"Candice,Miller,031 843 72 38,miller@example.com;" + 
			"Ben,Harmon,031 843 72 39,harmon@example.com;" + 
			"Jason,Curry,031 843 72 40,curry@example.com;" + 
			"Eunice,Mitchell,031 843 72 41,mitchell@example.com;";
	private String customerCsv =  
					"Cyclops Motors,Praceta Rosa 1: 2770-123 Paço de Arcos, Portugal,(977) 828-9760,cyclops.motors@example.com;" +
					"Sail Navigations,98 Porcupine Trail: Dunrobin ON K0A 1T0, Canada,(627) 137-1790,sail.navigations@example.com;" + 
					"Rose Limited,Ronda de el Olivar de los Pozos 4 : 45004 Toledo, Spain,(567) 906-4052,rose.limited@example.com;" + 
					"Robinetworks,Calle Generación 21-9:29196 Málaga:, Spain,(426) 707-3647,robinetworks@example.com;" + 
					"Peachco,Leixlip Co. Kildare:363-371 Castletown,Ireland,(737) 688-0889,peachco@example.com;" + 
					"Whiteoutwares,Stoke-on-Trent ST1 5HR:151-153 Marsh St N,UK,(176) 380-3148,whiteoutwares@example.com;" + 
					"Griffindustries,Rue Alexandre Wouters 7-12: 1090 Jette, Belgium,(356) 649-7639,griffindustries@example.com;" + 
					"Herbbooks,Kaliskiego 25:01-476 Warszawa, Poland,(134) 469-9583,herbbooks@example.com;" + 
					"Shadowtronics,Im Steinkampe 14-16:38110 Braunschweig:,Germany,(676) 246-7637,shadowtronics@example.com;" + 
					"Nightwell,Komsomolskaya pl. 1:Moskva,Russia 107140,(555) 232-8189,nightwell@example.com;" + 
					"Melon Enterprises,Pia?a Doroban?ilor 2: Bucure?ti,Romania,(716) 683-2661,melon.enterprises@example.com;" + 
					"Granite Corporation,Iera Odos 36-44:Athina 104 35,Greece,(822) 998-3532,granite.corporation@example.com;" + 
					"Eclipse Productions,Partizanskog odreda Zvijezda 2-22:Vogoš?a,Bosnia and Herzegovina,(587) 173-5823,eclipse.productions@example.com;" + 
					"Purplelimited,14 Triq Ir-Rifu?jati Tal-Gwerra:Il-Mosta,Malta,(586) 499-4837,purplelimited@example.com;" + 
					"Omegacoustics,Via Bergamina 14-18:20016 Pero MI,Italy,(114) 462-9810,omegacoustics@example.com;" + 
					"Jetechnologies,Árpád u. 45-71:Budapest 1196,Hungary,(979) 370-0150,jetechnologies@example.com;" + 
					"Felinetworks,Grüner Weg 3-1:50825 Köln,Germany,(222) 393-9330,felinetworks@example.com;" + 
					"Nemoair,Perjenerweg 2:6500 Landeck,Austria,(727) 294-5007,nemoair@example.com;" + 
					"Peachstar,Burgerstrasse 13-15:3600 Thun,Schweiz,(177) 593-0085,peachstar@example.com;" + 
					"Hatchman,Vue-des-Alpes 19-1:1627 Vaulruz,Schweiz,(530) 277-8373,hatchman@example.com;" + 
					"Sphinx Enterprises,Rütistrasse 28:6032 Emmen,Schweiz,(912) 643-4135,sphinx.enterprises@example.com;" + 
					"Core Security,Röhrliberg 32:6330 Cham,Schweiz,(494) 888-4751,core.security@example.com;" + 
					"Hurricane Systems,Via Vergiò 8-18:6932 Lugano,Schweiz,(408) 472-1821,hurricane.systems@example.com;" + 
					"Signalimited,Meiefeldstrasse 34:3400 Burgdorf,Schweiz,(743) 775-0073,signalimited@example.com;" + 
					"Stardustechnologies,Les Chandelènes 2-4:1436 Chamblon,Schweiz,(842) 959-3414,stardustechnologies@example.com;" + 
					"Cyclolutions,Chemin du Château 2-8:1920 Martigny,Schweiz,(540) 252-2091,cyclolutions@example.com;" + 
					"Galerprises,Hauptstrasse 37:32 Aarau,Schweiz,(895) 953-6977,galerprises@example.com;" + 
					"Pixelmaster,Neuhofstrasse 2:6340 Baar,Schweiz,(708) 622-2003,pixelmaster@example.com;" + 
					"Shadetales,Chemin de la Combatte 3:2802 Develier,Schweiz,(681) 776-9988,shadetales@example.com;" + 
					"Vortexbar,Giacomettistrasse 114-116:7000 Chur,Schweiz,(804) 820-2271,vortexbar@example.com;";
	
	private String itemCsv = 
			"EPX-WQ-965,0;" + 
			"XKG-TX-156,0;" + 
			"XKG-S-596,2;" + 
			"XKG-X-529,4;" + 
			"XKG-S-68,6;" + 
			"XKG-X-667,5;" + 
			"XKG-S-519,0;" + 
			"XKG-TX-738,4;" + 
			"XKG-WQ-405,7;" + 
			"XKG-X-780,1;" + 
			"XKG-ZZ-759,4;" + 
			"XKG-S-537,2;" + 
			"XKG-S-116,0;" + 
			"XKG-WQ-604,5;" + 
			"XKG-ZZ-712,0;" + 
			"XKG-X-990,0;" + 
			"XKG-X-273,1;" + 
			"XKG-WQ-3,5;" + 
			"XKG-TX-287,7;" + 
			"XKG-S-305,1;" + 
			"XKG-TX-893,0;" + 
			"XKG-ZZ-922,2;" + 
			"XKG-ZZ-645,2;" + 
			"XKG-TX-196,2;" + 
			"XKG-TX-356,2;" + 
			"XKG-S-852,4;" + 
			"XKG-X-180,4;" + 
			"XKG-TX-842,1;" + 
			"XKG-TX-936,5;" + 
			"XKG-ZZ-908,4;" + 
			"XKG-X-58,5;" + 
			"XKG-TX-49,1;" + 
			"XKG-X-277,6;" + 
			"XKG-S-538,5;" + 
			"XKG-ZZ-211,0;" + 
			"XKG-S-555,7;" + 
			"XKG-X-890,7;" + 
			"XKG-WQ-901,0;" + 
			"XKG-S-590,2;" + 
			"XKG-S-895,0;" + 
			"XKG-WQ-27,7;" + 
			"XKG-WQ-905,4;" + 
			"XKG-X-903,0;" + 
			"XKG-S-772,0;" + 
			"CPT-ZZ-352,0;" + 
			"CPT-S-302,7;" + 
			"CPT-TX-129,1;" + 
			"CPT-ZZ-494,3;" + 
			"CPT-S-757,2;" + 
			"CPT-ZZ-240,0;" + 
			"CPT-ZZ-956,3;" + 
			"CPT-ZZ-95,7;" + 
			"WVB-X-579,1;" + 
			"WVB-WQ-24,5;" + 
			"WVB-S-36,0;" + 
			"WVB-ZZ-455,3;" + 
			"WVB-WQ-788,4;" + 
			"WVB-X-152,5;" + 
			"WVB-ZZ-780,7;" + 
			"JQS-TX-13,7;" + 
			"JQS-ZZ-240,4;" + 
			"JQS-ZZ-425,0;" + 
			"JQS-S-227,2;" + 
			"JQS-X-628,6;" + 
			"JQS-ZZ-998,6;" + 
			"JQS-TX-353,7;" + 
			"JQS-X-259,2;" + 
			"JQS-WQ-125,1;" + 
			"JQS-X-255,7;" + 
			"JQS-TX-179,4;" + 
			"JQS-S-997,1;" + 
			"JQS-ZZ-372,0;" + 
			"JQS-WQ-442,3;" + 
			"JQS-WQ-290,5;" + 
			"JQS-WQ-488,4;" + 
			"JQS-WQ-973,6;" + 
			"JQS-ZZ-770,0;" + 
			"JQS-X-152,6;" + 
			"JQS-TX-86,7;" + 
			"JQS-S-69,7;" + 
			"JQS-X-962,1;" + 
			"JQS-X-191,4;" + 
			"JQS-WQ-445,4;" + 
			"JQS-WQ-528,5;" + 
			"JQS-TX-705,7;" + 
			"JQS-ZZ-310,5;" + 
			"JQS-S-597,7;" + 
			"JQS-S-321,0;" + 
			"JQS-ZZ-601,4;" + 
			"JQS-TX-69,1;" + 
			"JQS-S-334,7;" + 
			"JQS-ZZ-630,5;" + 
			"JQS-ZZ-842,5;" + 
			"JQS-X-513,0;" + 
			"JQS-ZZ-534,6;" + 
			"JQS-ZZ-585,5;" + 
			"JQS-WQ-716,7;" + 
			"JQS-TX-418,0;" + 
			"JQS-ZZ-979,1;" + 
			"JQS-X-199,7;" + 
			"JQS-WQ-63,7;" + 
			"JQS-TX-886,2;" + 
			"JQS-S-375,7;" + 
			"JQS-X-557,5;" + 
			"OEB-TX-495,3;" + 
			"OEB-WQ-304,7;" + 
			"OEB-X-165,3;" + 
			"OEB-WQ-733,4;" + 
			"OEB-WQ-171,7;" + 
			"OEB-S-754,5;" + 
			"OEB-S-475,6;" + 
			"OEB-TX-864,1;" + 
			"OEB-WQ-894,1;" + 
			"OEB-S-211,2;" + 
			"OEB-S-916,6;" + 
			"JWQ-WQ-712,5;" + 
			"JWQ-TX-713,5;" + 
			"JWQ-ZZ-159,5;" + 
			"JWQ-WQ-323,4;" + 
			"JWQ-TX-528,4;" + 
			"JWQ-S-789,5;" + 
			"JWQ-S-840,3;" + 
			"JWQ-X-428,3;" + 
			"JWQ-ZZ-314,0;" + 
			"JWQ-WQ-652,2;" + 
			"JWQ-WQ-53,6;" + 
			"JWQ-ZZ-861,2;" + 
			"JWQ-ZZ-142,6;" + 
			"JWQ-WQ-144,5;" + 
			"JWQ-X-200,0;" + 
			"JWQ-WQ-480,0;" + 
			"JWQ-WQ-923,5;" + 
			"JWQ-ZZ-871,1;" + 
			"JWQ-X-133,0;" + 
			"JWQ-TX-613,1;" + 
			"JWQ-X-368,5;" + 
			"JWQ-TX-542,2;" + 
			"JWQ-WQ-961,1;" + 
			"JWQ-S-862,4;" + 
			"JWQ-ZZ-897,5;" + 
			"JWQ-ZZ-253,2;" + 
			"JWQ-TX-980,7;" + 
			"JWQ-ZZ-568,3;" + 
			"JWQ-ZZ-151,5;" + 
			"JWQ-TX-876,0;" + 
			"JWQ-ZZ-431,4;" + 
			"JWQ-TX-879,7;" + 
			"JWQ-S-894,6;" + 
			"ABE-WQ-657,1;" + 
			"ABE-ZZ-94,5;" + 
			"ABE-X-13,5;" + 
			"ABE-ZZ-926,4;" + 
			"ABE-S-955,6;" + 
			"ABE-TX-671,7;" + 
			"ABE-ZZ-948,1;" + 
			"ABE-WQ-700,7;" + 
			"ABE-X-272,5;" + 
			"ABE-ZZ-441,3;";
	
	private String truckCsv = "VW Transporter 1, 1;"
			+ "VW Transporter 2, 1;"
			+ "VW Transporter 3, 1;"
			+ "VW Transporter 4, 1;"
			+ "VW Transporter 5, 1;"
			+ "VW Transporter 6, 1;"
			+ "VW Transporter 7, 1;"
			+ "VW Transporter 8, 1;"
			+ "Scania S 450 B6x4NA 1, 32;"
			+ "Scania S 450 B6x4NA 2, 32;"
			+ "Scania S 450 B6x4NA 3, 32;"
			+ "Scania S 450 B6x4NA 4, 32;"
			+ "Scania S 450 B6x4NA 5, 32;"
			+ "Scania S 450 B6x4NA 6, 32;"
			+ "Mercedes-Benz Actros 1,36;"
			+ "Mercedes-Benz Actros 2,36;";
	
}

