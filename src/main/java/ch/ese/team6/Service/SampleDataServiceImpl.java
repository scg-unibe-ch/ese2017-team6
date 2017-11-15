package ch.ese.team6.Service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.DupplicateEntryException;
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
import ch.ese.team6.Repository.ItemRepository;
import ch.ese.team6.Repository.OrderRepository;
import ch.ese.team6.Repository.RoleRepository;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Repository.TruckRepository;
import ch.ese.team6.Repository.UserRepository;

@Service
public class SampleDataServiceImpl implements SampleDataService{
	
	@Autowired	private UserService userService;
	@Autowired	private UserRepository userRepository;
	@Autowired	private AddressRepository addressRepository;
	@Autowired	private CustomerRepository customerRepository;
	@Autowired	private ItemRepository itemRepository;
	@Autowired	private TruckRepository truckRepository;
	@Autowired	private OrderRepository orderRepository;
	@Autowired	private RouteRepository routeRepository;
	@Autowired	private RoleRepository roleRepository;
	
	public void loadData() throws BadSizeException, DupplicateEntryException {
		try {
		if (roleRepository.count() == 0) this.loadRoles();
		if (userRepository.count() == 0) this.loadUsers();
		if (customerRepository.count() == 0)this.loadCustomers();
		if (itemRepository.count() == 0)this.loadItems();
		if (truckRepository.count() == 0)this.loadTrucks();
		if (orderRepository.count() == 0)this.loadOrders();
		if (routeRepository.count() == 0)this.loadRoutes();}
		catch (BadSizeException e){
			e.printStackTrace();
			throw new BadSizeException("Data coudn't load because of a data mismatch.\n"
					+ e.getMessage());
			
		}
		catch (NumberFormatException n) {
			n.printStackTrace();
			throw new BadSizeException("Data couldn't load. You have wrong numbers.");
		}
	}
	
	public String[][] parseCsv(String csv){
		int i = 0;
		String[] lines = csv.split(";");
		String[][] data = new String[lines.length][lines[0].length()];
		for (String line : lines) {
			data[i] = line.split(",");
			i++;
		}
		for(int j = 0; j < data.length ; j++) {
			for(int k = 0; k < data[j].length; k++) {
				data[j][k] = data[j][k].trim();
			}
		}
		return data;
	}
	
	public void loadRoles() {
		Role logistician = new Role();
		logistician.setName("LOGISTICS");
		Role driver = new Role();
		driver.setName("DRIVER");
		roleRepository.save(logistician);
		roleRepository.save(driver);
	}
	
	public void loadUsers() throws BadSizeException, DupplicateEntryException {
		/*String[] users = userCsv.split(";");
		for(int i = 0; i <  10 /*users.length*//*; i++) {
			String[] userdata = users[i].split(",");
			User user = new User();			
			user.setUsername(userService.generateUsername(userdata));
			user.setFirstname(userdata[0]);
			user.setSurname(userdata[1]);
			user.setPhoneNumber(userdata[2]);
			user.setEmail(userdata[3]);
			user.setPassword("password");
			user.setPasswordConfirm("password");
			user.setRoles(roleRepository.findByName(i < 5 ? "LOGISTICS": "DRIVER"));
			userService.save(user);	
		} */
		int i = 0;
		String[][]users = this.parseCsv(userCsv);
		for (String[] line: users) {
			User user = new User();			
			user.setUsername(userService.generateUsername(line));
			user.setFirstname(line[0]);
			user.setSurname(line[1]);
			user.setPhoneNumber(line[2]);
			user.setEmail(line[3]);
			user.setPassword("password");
			user.setPasswordConfirm("password");
			user.setRoles(roleRepository.findByName(i < 5 ? "LOGISTICS": "DRIVER"));
			i ++;
			if(user.isValid()) userService.save(user);
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
	
	public void loadItems() throws NumberFormatException, BadSizeException {
		String[] items = itemCsv.split(";");
		for (int i = 0; i < 10/*items.length*/; i++) {
			String[] itemData = items[i].split(",");
			Item item = new Item();
			item.setName(itemData[0]);
			item.setRequiredSpace(Integer.parseInt(itemData[1]));
			item.setWeight(Integer.parseInt(itemData[2]));
			itemRepository.save(item);
		}
	}
	
	public void loadTrucks() throws BadSizeException{
		String[] trucks = truckCsv.split(";");
		for (int i = 0; i < 10 /*trucks.length*/; i++) { 
			String[] truckData = trucks[i].split(",");
			Truck truck = new Truck();
			truck.setTruckname(truckData[0]);
			truck.setMaxCargoSpace(Integer.parseInt(truckData[1].trim()));
			truck.setMaxLoadCapacity(Integer.parseInt(truckData[2].trim()));
			truckRepository.save(truck);
		}
	}
	
	public void loadOrders() {
		for (int i= 0;i<10; i++) {
		int n_cus = customerRepository.findAll().size();
		Customer cus = customerRepository.findAll().get((int) (Math.random()*n_cus));
		Calendar deliveryDateC = Calendar.getInstance();
		deliveryDateC.add(Calendar.DAY_OF_YEAR, (int) (Math.random()*5));
		Date deliveryDate = deliveryDateC.getTime();
		
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
	"EPX-WQ-965,0,5;" + 
	"XKG-TX-156,0,4;" + 
	"XKG-S-596,2,1135;" + 
	"XKG-X-529,4,1422;" + 
	"XKG-S-68,6,869;" + 
	"XKG-X-667,5,1562;" + 
	"XKG-S-519,0,8;" + 
	"XKG-TX-738,4,1679;" + 
	"XKG-WQ-405,7,1198;" + 
	"XKG-X-780,1,1636;" + 
	"XKG-ZZ-759,4,1279;" + 
	"XKG-S-537,2,1894;" + 
	"XKG-S-116,0,1;" + 
	"XKG-WQ-604,5,712;" + 
	"XKG-ZZ-712,0,1044;" + 
	"XKG-X-990,0,1912;" + 
	"XKG-X-273,1,1403;" + 
	"XKG-WQ-3,5,1669;" + 
	"XKG-TX-287,7,1857;" + 
	"XKG-S-305,1,839;" + 
	"XKG-TX-893,0,4;" + 
	"XKG-ZZ-922,2,1778;" + 
	"XKG-ZZ-645,2,1395;" + 
	"XKG-TX-196,2,1584;" + 
	"XKG-TX-356,2,979;" + 
	"XKG-S-852,4,846;" + 
	"XKG-X-180,4,1526;" + 
	"XKG-TX-842,1,1849;" + 
	"XKG-TX-936,5,720;" + 
	"XKG-ZZ-908,4,1155;" + 
	"XKG-X-58,5,1637;" + 
	"XKG-TX-49,1,1837;" + 
	"XKG-X-277,6,1879;" + 
	"XKG-S-538,5,1857;" + 
	"XKG-ZZ-211,0,3;" + 
	"XKG-S-555,7,1403;" + 
	"XKG-X-890,7,1675;" + 
	"XKG-WQ-901,0,1758;" + 
	"XKG-S-590,2,1230;" + 
	"XKG-S-895,0,2;" + 
	"XKG-WQ-27,7,1656;" + 
	"XKG-WQ-905,4,1178;" + 
	"XKG-X-903,0,2;" + 
	"XKG-S-772,0,2;" + 
	"CPT-ZZ-352,0,3;" + 
	"CPT-S-302,7,1840;" + 
	"CPT-TX-129,1,1161;" + 
	"CPT-ZZ-494,3,738;" + 
	"CPT-S-757,2,1729;" + 
	"CPT-ZZ-240,0,6;" + 
	"CPT-ZZ-956,3,1620;" + 
	"CPT-ZZ-95,7,1230;" + 
	"WVB-X-579,1,538;" + 
	"WVB-WQ-24,5,773;" + 
	"WVB-S-36,0,2;" + 
	"WVB-ZZ-455,3,1866;" + 
	"WVB-WQ-788,4,1025;" + 
	"WVB-X-152,5,875;" + 
	"WVB-ZZ-780,7,1465;" + 
	"JQS-TX-13,7,1970;" + 
	"JQS-ZZ-240,4,1202;" + 
	"JQS-ZZ-425,0,3;" + 
	"JQS-S-227,2,1556;" + 
	"JQS-X-628,6,1446;" + 
	"JQS-ZZ-998,6,1124;" + 
	"JQS-TX-353,7,1015;" + 
	"JQS-X-259,2,1421;" + 
	"JQS-WQ-125,1,864;" + 
	"JQS-X-255,7,646;" + 
	"JQS-TX-179,4,518;" + 
	"JQS-S-997,1,1845;" + 
	"JQS-ZZ-372,0,2;" + 
	"JQS-WQ-442,3,1528;" + 
	"JQS-WQ-290,5,1440;" + 
	"JQS-WQ-488,4,1919;" + 
	"JQS-WQ-973,6,1995;" + 
	"JQS-ZZ-770,0,1;" + 
	"JQS-X-152,6,1573;" + 
	"JQS-TX-86,7,1087;" + 
	"JQS-S-69,7,1712;" + 
	"JQS-X-962,1,1864;" + 
	"JQS-X-191,4,1664;" + 
	"JQS-WQ-445,4,1165;" + 
	"JQS-WQ-528,5,1544;" + 
	"JQS-TX-705,7,687;" + 
	"JQS-ZZ-310,5,1477;" + 
	"JQS-S-597,7,863;" + 
	"JQS-S-321,0,1;" + 
	"JQS-ZZ-601,4,1608;" + 
	"JQS-TX-69,1,814;" + 
	"JQS-S-334,7,982;" + 
	"JQS-ZZ-630,5,1187;" + 
	"JQS-ZZ-842,5,1354;" + 
	"JQS-X-513,0,6;" + 
	"JQS-ZZ-534,6,1057;" + 
	"JQS-ZZ-585,5,654;" + 
	"JQS-WQ-716,7,603;" + 
	"JQS-TX-418,0,3;" + 
	"JQS-ZZ-979,1,1359;" + 
	"JQS-X-199,7,1381;" + 
	"JQS-WQ-63,7,1478;" + 
	"JQS-TX-886,2,1362;" + 
	"JQS-S-375,7,1734;" + 
	"JQS-X-557,5,928;" + 
	"OEB-TX-495,3,1425;" + 
	"OEB-WQ-304,7,1285;" + 
	"OEB-X-165,3,1937;" + 
	"OEB-WQ-733,4,1487;" + 
	"OEB-WQ-171,7,696;" + 
	"OEB-S-754,5,1150;" + 
	"OEB-S-475,6,767;" + 
	"OEB-TX-864,1,1095;" + 
	"OEB-WQ-894,1,726;" + 
	"OEB-S-211,2,632;" + 
	"OEB-S-916,6,1610;" + 
	"JWQ-WQ-712,5,822;" + 
	"JWQ-TX-713,5,1782;" + 
	"JWQ-ZZ-159,5,639;" + 
	"JWQ-WQ-323,4,1404;" + 
	"JWQ-TX-528,4,1815;" + 
	"JWQ-S-789,5,1134;" + 
	"JWQ-S-840,3,1531;" + 
	"JWQ-X-428,3,1724;" + 
	"JWQ-ZZ-314,0,2;" + 
	"JWQ-WQ-652,2,1068;" + 
	"JWQ-WQ-53,6,1529;" + 
	"JWQ-ZZ-861,2,1578;" + 
	"JWQ-ZZ-142,6,1257;" + 
	"JWQ-WQ-144,5,1406;" + 
	"JWQ-X-200,0,2;" + 
	"JWQ-WQ-480,0,4;" + 
	"JWQ-WQ-923,5,1124;" + 
	"JWQ-ZZ-871,1,1037;" + 
	"JWQ-X-133,0,2;" + 
	"JWQ-TX-613,1,1864;" + 
	"JWQ-X-368,5,1196;" + 
	"JWQ-TX-542,2,1239;" + 
	"JWQ-WQ-961,1,916;" + 
	"JWQ-S-862,4,1318;" + 
	"JWQ-ZZ-897,5,1221;" + 
	"JWQ-ZZ-253,2,1100;" + 
	"JWQ-TX-980,7,992;" + 
	"JWQ-ZZ-568,3,1439;" + 
	"JWQ-ZZ-151,5,1400;" + 
	"JWQ-TX-876,0,5;" + 
	"JWQ-ZZ-431,4,731;" + 
	"JWQ-TX-879,7,1088;" + 
	"JWQ-S-894,6,556;" + 
	"ABE-WQ-657,1,1387;" + 
	"ABE-ZZ-94,5,1031;" + 
	"ABE-X-13,5,1531;" + 
	"ABE-ZZ-926,4,1115;" + 
	"ABE-S-955,6,1111;" + 
	"ABE-TX-671,7,1310;" + 
	"ABE-ZZ-948,1,1853;" + 
	"ABE-WQ-700,7,1757;" + 
	"ABE-X-272,5,1671;" + 
	"ABE-ZZ-441,3,1408;";
	
	private String truckCsv = "VW Transporter 1, 10,50;"
			+ "VW Transporter 2, 100,50000;"
			+ "VW Transporter 3, 100,40000;"
			+ "VW Transporter 4, 200,10000;"
			+ "VW Transporter 5, 100,40000;"
			+ "VW Transporter 6, 200,10000;"
			+ "VW Transporter 7, 100,40000;"
			+ "VW Transporter 8, 200,10000;"
			+ "Scania S 450 B6x4NA 1, 320,40000;"
			+ "Scania S 450 B6x4NA 2, 320,40000;"
			+ "Scania S 450 B6x4NA 3, 100,40000;"
			+ "Scania S 450 B6x4NA 4, 500,50000;"
			+ "Scania S 450 B6x4NA 5, 700,50000;"
			+ "Scania S 450 B6x4NA 6, 800,60000;"
			+ "Mercedes-Benz Actros 1,900,70000;"
			+ "Mercedes-Benz Actros 2,1000,80000;";

	
}

