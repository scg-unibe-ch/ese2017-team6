package ch.ese.team6.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.DupplicateEntryException;
import ch.ese.team6.Exception.InvalidAddressException;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.Customer;
import ch.ese.team6.Model.Distance;
import ch.ese.team6.Model.Item;
import ch.ese.team6.Model.Order;
import ch.ese.team6.Model.OrderItem;
import ch.ese.team6.Model.Role;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Model.Truck;
import ch.ese.team6.Model.User;
import ch.ese.team6.Repository.AddressRepository;
import ch.ese.team6.Repository.CustomerRepository;
import ch.ese.team6.Repository.DistanceRepository;
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
	@Autowired private DistanceRepository distanceRepository;
	@Autowired private AddressService addressService;
	
	@Override
	public void loadData() throws BadSizeException, DupplicateEntryException {
		try {
		if (roleRepository.count() == 0) this.loadRoles();
		if (userRepository.count() == 0) this.loadUsers();
		if (customerRepository.count() == 0)this.loadCustomers();
		if (itemRepository.count() == 0)this.loadItems();
		if (truckRepository.count() == 0)this.loadTrucks();
		if (orderRepository.count() == 0)this.loadOrders();
		//if (routeRepository.count() == 0)this.loadRoutes();
		}
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
		
		this.loadDepositAddress();
		
		String[] customers = customerCsv.split(";");
		for (int i = 0; i < 26/*6/*customers.length*/; i++) {
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
			this.saveDistances(address);	
			customerRepository.save(customer);
			
			
		}
	
	}
	
	/**
	 * Creates the deposit of the items
	 */
	private void loadDepositAddress() {

		Address deposit = new Address();
		deposit.setStreet("Hochschulstrasse 6");
		deposit.setCity("3012 Bern");
		deposit.setCountry("Schweiz");
		addressRepository.save(deposit);
		this.saveDistances(deposit);
		OurCompany.setDepositId(deposit.getId());
	}

	private void saveDistances(Address address) {
	for(Address otherAddress: addressRepository.findAll()) {
		Distance newDistance = new Distance();
		newDistance.setDestination(otherAddress);
		newDistance.setOrigin(address);
		this.completeDistance(newDistance);
		distanceRepository.save(newDistance);
		
		if(!address.toString().equals(otherAddress.toString())) {
		Distance newDistanceRev = new Distance();
		newDistanceRev.setOrigin(otherAddress);
		newDistanceRev.setDestination(address);
		this.completeDistance(newDistanceRev);
		
		distanceRepository.save(newDistanceRev);
		}
	}
	}

	/**
	 * Assigns the duration and the metres based on the distancesCSV string
	 * @param dis
	 */
	private void completeDistance(Distance dis) {
		String[] distancesCSV = {distancesCSV0,distancesCSV1,distancesCSV2,distancesCSV3,distancesCSV4,distancesCSV5,distancesCSV6,distancesCSV7,distancesCSV8,distancesCSV9,distancesCSV10,distancesCSV11};
		for(int j = 0; j < distancesCSV.length;j++) {
		String[] distances = distancesCSV[j].split("<<");

		for(int i = 0; i< distances.length;i++) {
			String[] line = distances[i].split("<");
			if(line[0].equals(dis.getOrigin().toString())) {
				if(line[1].equals(dis.getDestination().toString())) {
					dis.setDurationSeconds(Integer.parseInt(line[2]));
					dis.setDistanceMetres(Integer.parseInt(line[3]));
					return;
				}
			}
		}
		}
		
	}

	public void loadItems() throws NumberFormatException, BadSizeException {
		String[] items = itemCsv.split(";");
		for (int i = 0; i < items.length; i++) {
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
		for (int i = 0; i <  trucks.length; i++) { 
			String[] truckData = trucks[i].split(",");
			Truck truck = new Truck();
			truck.setTruckname(truckData[0]);
			truck.setMaxCargoSpace(Integer.parseInt(truckData[1].trim()));
			truck.setMaxLoadCapacity(Integer.parseInt(truckData[2].trim()));
			truckRepository.save(truck);
		}
	}
	
	public void loadOrders() {

		Random random = new Random(2017);
		for (int i= 0;i<10; i++) {
		int n_cus = customerRepository.findAll().size();
		Customer cus = customerRepository.findAll().get(i%(n_cus));
		Calendar deliveryDateC = Calendar.getInstance();
		deliveryDateC.add(Calendar.DAY_OF_YEAR,  (random.nextInt(2)));
		Date deliveryDate = deliveryDateC.getTime();
		
		Order order = new Order();
		order.setCustomer(cus);
		order.setDeliveryDate(deliveryDate);
			
		for(int j=0;j<(random.nextInt(10)+1);j++){
			OrderItem oi = new OrderItem(order);
			order.getOrderItems().add(oi);
			oi.setAmount((int)(random.nextInt(10)+1));
			int n_item = itemRepository.findAll().size();
			oi.setItem(itemRepository.findAll().get(random.nextInt(n_item)));	
			}

			orderRepository.save(order);
		}
	}
	
	public void loadRoutes() {
		for (long i= 1;i<4; i++) {
			Calendar date = Calendar.getInstance();
			Route route = new Route(date.getTime(),addressRepository.findOne(OurCompany.depositId));
			Set<Role> roles = roleRepository.findByName("DRIVER");
			List<User> users = userRepository.findByRoles(roles);
			List<Truck> trucks = truckRepository.findAll();
			route.setDriver(users.get((int) i));
			route.setTruck(trucks.get((int) i));
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
			"Peachstar,Burgerstrasse 13-15:3600 Thun,Schweiz,(177) 593-0085,peachstar@example.com;" + 
			"Omegacoustics,Via Bergamina 14-18:20016 Pero MI,Italy,(114) 462-9810,omegacoustics@example.com;" + 
					"Jetechnologies,Árpád u. 45-71:Budapest 1196,Hungary,(979) 370-0150,jetechnologies@example.com;" + 
					"Felinetworks,Grüner Weg 3-1:50825 Köln,Germany,(222) 393-9330,felinetworks@example.com;" + 
					"Nemoair,Perjenerweg 2:6500 Landeck,Austria,(727) 294-5007,nemoair@example.com;" + 
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
					"Vortexbar,Giacomettistrasse 114-116:7000 Chur,Schweiz,(804) 820-2271,vortexbar@example.com;"+
					"Cyclops Motors,Praceta Rosa 1: 2770-123 Paço de Arcos, Portugal,(977) 828-9760,cyclops.motors@example.com;" +
					"Rose Limited,Ronda de el Olivar de los Pozos 4 : 45004 Toledo, Spain,(567) 906-4052,rose.limited@example.com;" + 
					"Robinetworks,Calle Generación 21-9:29196 Málaga:, Spain,(426) 707-3647,robinetworks@example.com;" + 
					"Griffindustries,Rue Alexandre Wouters 7-12: 1090 Jette, Belgium,(356) 649-7639,griffindustries@example.com;" + 
					"Eclipse Productions,Partizanskog odreda Zvijezda 2-22:Vogošća,Bosnia and Herzegovina,(587) 173-5823,eclipse.productions@example.com;" + 
					"Herbbooks,Kaliskiego 25:01-476 Warszawa, Poland,(134) 469-9583,herbbooks@example.com;" + 
					"Shadowtronics,Im Steinkampe 14-16:38110 Braunschweig:,Germany,(676) 246-7637,shadowtronics@example.com;" + 
					"Nightwell,Komsomolskaya pl. 1:Moskva,Russia 107140,(555) 232-8189,nightwell@example.com;" + 
					"Melon Enterprises,Piata Dorobantilor 2: Bucuresti,Romania,(716) 683-2661,melon.enterprises@example.com;" + 
					"Sail Navigations,98 Porcupine Trail: Dunrobin ON K0A 1T0, Canada,(627) 137-1790,sail.navigations@example.com;" +
					"Peachco,Leixlip Co. Kildare:363-371 Castletown,Ireland,(737) 688-0889,peachco@example.com;" + 
					"Whiteoutwares,Stoke-on-Trent ST1 5HR:151-153 Marsh St N,UK,(176) 380-3148,whiteoutwares@example.com;" + 
					"Granite Corporation,Iera Odos 36-44:Athina 104 35,Greece,(822) 998-3532,granite.corporation@example.com;" + 
					"Purplelimited,14 Triq Ir-Rifu?jati Tal-Gwerra:Il-Mosta,Malta,(586) 499-4837,purplelimited@example.com;";
	
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

	//< separates fields, << separates rows, could not use , because "," is part of many addresses.
String distancesCSV0 = "Burgerstrasse 13-15, 3600 Thun, Schweiz<Hochschulstrasse 6, 3012 Bern, Schweiz<1675<36381<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Burgerstrasse 13-15, 3600 Thun, Schweiz<1675<36381<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Hochschulstrasse 6, 3012 Bern, Schweiz<13590<344413<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Via Bergamina 14-18, 20016 Pero MI, Italy<13590<344413<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Burgerstrasse 13-15, 3600 Thun, Schweiz<13455<311543<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Via Bergamina 14-18, 20016 Pero MI, Italy<13455<311543<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Hochschulstrasse 6, 3012 Bern, Schweiz<40515<1121735<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Árpád u. 45-71, Budapest 1196, Hungary<40515<1121735<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Burgerstrasse 13-15, 3600 Thun, Schweiz<41299<1147013<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Árpád u. 45-71, Budapest 1196, Hungary<41299<1147013<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Via Bergamina 14-18, 20016 Pero MI, Italy<33246<987967<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Árpád u. 45-71, Budapest 1196, Hungary<33246<987967<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Hochschulstrasse 6, 3012 Bern, Schweiz<21294<589499<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Grüner Weg 3-1, 50825 Köln, Germany<21294<589499<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Burgerstrasse 13-15, 3600 Thun, Schweiz<22078<614777<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Grüner Weg 3-1, 50825 Köln, Germany<22078<614777<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Via Bergamina 14-18, 20016 Pero MI, Italy<30221<824120<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Grüner Weg 3-1, 50825 Köln, Germany<30221<824120<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Árpád u. 45-71, Budapest 1196, Hungary<40900<1171241<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Grüner Weg 3-1, 50825 Köln, Germany<40900<1171241<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Hochschulstrasse 6, 3012 Bern, Schweiz<13438<336902<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Perjenerweg 2, 6500 Landeck, Austria<13438<336902<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Burgerstrasse 13-15, 3600 Thun, Schweiz<14222<362180<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Perjenerweg 2, 6500 Landeck, Austria<14222<362180<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Via Bergamina 14-18, 20016 Pero MI, Italy<15105<354548<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Perjenerweg 2, 6500 Landeck, Austria<15105<354548<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Árpád u. 45-71, Budapest 1196, Hungary<28564<821570<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Perjenerweg 2, 6500 Landeck, Austria<28564<821570<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Grüner Weg 3-1, 50825 Köln, Germany<25130<660316<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Perjenerweg 2, 6500 Landeck, Austria<25130<660316<<";
		
String distancesCSV1 = "Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Hochschulstrasse 6, 3012 Bern, Schweiz<2563<62733<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<2563<62733<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Burgerstrasse 13-15, 3600 Thun, Schweiz<3612<96251<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<3612<96251<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Via Bergamina 14-18, 20016 Pero MI, Italy<14190<331285<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<14190<331285<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Árpád u. 45-71, Budapest 1196, Hungary<42736<1193869<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<42736<1193869<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Grüner Weg 3-1, 50825 Köln, Germany<23219<649210<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<23219<649210<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Perjenerweg 2, 6500 Landeck, Austria<15296<393909<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<15296<393909<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Hochschulstrasse 6, 3012 Bern, Schweiz<4519<108763<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Rütistrasse 28, 6032 Emmen, Schweiz<4519<108763<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Burgerstrasse 13-15, 3600 Thun, Schweiz<5303<134041<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Rütistrasse 28, 6032 Emmen, Schweiz<5303<134041<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Via Bergamina 14-18, 20016 Pero MI, Italy<9848<242078<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Rütistrasse 28, 6032 Emmen, Schweiz<9848<242078<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Árpád u. 45-71, Budapest 1196, Hungary<38095<1048508<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Rütistrasse 28, 6032 Emmen, Schweiz<38095<1048508<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Grüner Weg 3-1, 50825 Köln, Germany<21225<585115<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Rütistrasse 28, 6032 Emmen, Schweiz<21225<585115<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Perjenerweg 2, 6500 Landeck, Austria<10089<231945<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Rütistrasse 28, 6032 Emmen, Schweiz<10089<231945<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<6483<168009<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Rütistrasse 28, 6032 Emmen, Schweiz<6483<168009<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Hochschulstrasse 6, 3012 Bern, Schweiz<4882<127888<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Röhrliberg 32, 6330 Cham, Schweiz<4882<127888<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Burgerstrasse 13-15, 3600 Thun, Schweiz<5666<153166<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Röhrliberg 32, 6330 Cham, Schweiz<5666<153166<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Via Bergamina 14-18, 20016 Pero MI, Italy<9961<246033<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Röhrliberg 32, 6330 Cham, Schweiz<9961<246033<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Árpád u. 45-71, Budapest 1196, Hungary<37187<1027512<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Röhrliberg 32, 6330 Cham, Schweiz<37187<1027512<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Grüner Weg 3-1, 50825 Köln, Germany<21334<595520<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Röhrliberg 32, 6330 Cham, Schweiz<21334<595520<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Perjenerweg 2, 6500 Landeck, Austria<9182<210950<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Röhrliberg 32, 6330 Cham, Schweiz<9182<210950<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<6846<187134<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Röhrliberg 32, 6330 Cham, Schweiz<6846<187134<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Rütistrasse 28, 6032 Emmen, Schweiz<1107<22040<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Röhrliberg 32, 6330 Cham, Schweiz<1107<22040<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Hochschulstrasse 6, 3012 Bern, Schweiz<10910<276438<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Via Vergiò 8-18, 6932 Lugano, Schweiz<10910<276438<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Burgerstrasse 13-15, 3600 Thun, Schweiz<10774<243567<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Via Vergiò 8-18, 6932 Lugano, Schweiz<10774<243567<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Via Bergamina 14-18, 20016 Pero MI, Italy<3491<78051<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Via Vergiò 8-18, 6932 Lugano, Schweiz<3491<78051<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Árpád u. 45-71, Budapest 1196, Hungary<35545<1052078<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Via Vergiò 8-18, 6932 Lugano, Schweiz<35545<1052078<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Grüner Weg 3-1, 50825 Köln, Germany<27616<752790<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Via Vergiò 8-18, 6932 Lugano, Schweiz<27616<752790<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Perjenerweg 2, 6500 Landeck, Austria<12377<282765<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Via Vergiò 8-18, 6932 Lugano, Schweiz<12377<282765<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<12874<335684<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Via Vergiò 8-18, 6932 Lugano, Schweiz<12874<335684<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Rütistrasse 28, 6032 Emmen, Schweiz<7195<170961<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Via Vergiò 8-18, 6932 Lugano, Schweiz<7195<170961<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Röhrliberg 32, 6330 Cham, Schweiz<7307<174337<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Via Vergiò 8-18, 6932 Lugano, Schweiz<7307<174337<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Hochschulstrasse 6, 3012 Bern, Schweiz<1494<26404<<";
		
String distancesCSV5 = 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<1494<26404<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Burgerstrasse 13-15, 3600 Thun, Schweiz<2279<51682<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<2279<51682<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Via Bergamina 14-18, 20016 Pero MI, Italy<12892<330708<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<12892<330708<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Árpád u. 45-71, Budapest 1196, Hungary<40201<1117306<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<40201<1117306<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Grüner Weg 3-1, 50825 Köln, Germany<20684<572647<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<20684<572647<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Perjenerweg 2, 6500 Landeck, Austria<12761<317346<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<12761<317346<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<3459<85650<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<3459<85650<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Rütistrasse 28, 6032 Emmen, Schweiz<3877<91291<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<3877<91291<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Röhrliberg 32, 6330 Cham, Schweiz<4225<111099<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<4225<111099<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Via Vergiò 8-18, 6932 Lugano, Schweiz<10051<258861<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<10051<258861<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Hochschulstrasse 6, 3012 Bern, Schweiz<3404<81310<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<3404<81310<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Burgerstrasse 13-15, 3600 Thun, Schweiz<4450<114809<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<4450<114809<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Via Bergamina 14-18, 20016 Pero MI, Italy<15558<370271<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<15558<370271<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Árpád u. 45-71, Budapest 1196, Hungary<43574<1212427<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<43574<1212427<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Grüner Weg 3-1, 50825 Köln, Germany<24057<667768<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<24057<667768<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Perjenerweg 2, 6500 Landeck, Austria<16134<412467<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<16134<412467<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<3235<83191<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<3235<83191<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Rütistrasse 28, 6032 Emmen, Schweiz<7250<186411<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<7250<186411<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Röhrliberg 32, 6330 Cham, Schweiz<7598<206220<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<7598<206220<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Via Vergiò 8-18, 6932 Lugano, Schweiz<13424<353982<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<13424<353982<<";
	
String distancesCSV3 = "Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<4192<103919<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<4192<103919<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Hochschulstrasse 6, 3012 Bern, Schweiz<4889<129036<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Chemin du Château 2-8, 1920 Martigny, Schweiz<4889<129036<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Burgerstrasse 13-15, 3600 Thun, Schweiz<5939<162554<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Chemin du Château 2-8, 1920 Martigny, Schweiz<5939<162554<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Via Bergamina 14-18, 20016 Pero MI, Italy<11923<264958<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Chemin du Château 2-8, 1920 Martigny, Schweiz<11923<264958<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Árpád u. 45-71, Budapest 1196, Hungary<43977<1238985<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Chemin du Château 2-8, 1920 Martigny, Schweiz<43977<1238985<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Grüner Weg 3-1, 50825 Köln, Germany<25546<715513<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Chemin du Château 2-8, 1920 Martigny, Schweiz<25546<715513<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Perjenerweg 2, 6500 Landeck, Austria<17623<460212<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Chemin du Château 2-8, 1920 Martigny, Schweiz<17623<460212<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<2847<70482<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Chemin du Château 2-8, 1920 Martigny, Schweiz<2847<70482<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Rütistrasse 28, 6032 Emmen, Schweiz<8739<234157<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Chemin du Château 2-8, 1920 Martigny, Schweiz<8739<234157<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Röhrliberg 32, 6330 Cham, Schweiz<9087<253965<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Chemin du Château 2-8, 1920 Martigny, Schweiz<9087<253965<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Via Vergiò 8-18, 6932 Lugano, Schweiz<13172<224307<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Chemin du Château 2-8, 1920 Martigny, Schweiz<13172<224307<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<5681<151664<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Chemin du Château 2-8, 1920 Martigny, Schweiz<5681<151664<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<4278<109397<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Chemin du Château 2-8, 1920 Martigny, Schweiz<4278<109397<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Hochschulstrasse 6, 3012 Bern, Schweiz<3666<91596<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Hauptstrasse 37, 32 Aarau, Schweiz<3666<91596<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Burgerstrasse 13-15, 3600 Thun, Schweiz<4450<116874<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Hauptstrasse 37, 32 Aarau, Schweiz<4450<116874<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Via Bergamina 14-18, 20016 Pero MI, Italy<12007<309212<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Hauptstrasse 37, 32 Aarau, Schweiz<12007<309212<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Árpád u. 45-71, Budapest 1196, Hungary<37930<1055551<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Hauptstrasse 37, 32 Aarau, Schweiz<37930<1055551<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Grüner Weg 3-1, 50825 Köln, Germany<19815<545200<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Hauptstrasse 37, 32 Aarau, Schweiz<19815<545200<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Perjenerweg 2, 6500 Landeck, Austria<10489<255591<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Hauptstrasse 37, 32 Aarau, Schweiz<10489<255591<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<5631<150842<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Hauptstrasse 37, 32 Aarau, Schweiz<5631<150842<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Rütistrasse 28, 6032 Emmen, Schweiz<2992<69795<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Hauptstrasse 37, 32 Aarau, Schweiz<2992<69795<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Röhrliberg 32, 6330 Cham, Schweiz<2649<64509<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Hauptstrasse 37, 32 Aarau, Schweiz<2649<64509<<";
		String distancesCSV4 = 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Via Vergiò 8-18, 6932 Lugano, Schweiz<9166<237365<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Hauptstrasse 37, 32 Aarau, Schweiz<9166<237365<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<3027<74573<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Hauptstrasse 37, 32 Aarau, Schweiz<3027<74573<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<6336<168416<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Hauptstrasse 37, 32 Aarau, Schweiz<6336<168416<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Chemin du Château 2-8, 1920 Martigny, Schweiz<7917<217353<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Hauptstrasse 37, 32 Aarau, Schweiz<7917<217353<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Hochschulstrasse 6, 3012 Bern, Schweiz<5198<135175<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Neuhofstrasse 2, 6340 Baar, Schweiz<5198<135175<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Burgerstrasse 13-15, 3600 Thun, Schweiz<5983<160453<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Neuhofstrasse 2, 6340 Baar, Schweiz<5983<160453<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Via Bergamina 14-18, 20016 Pero MI, Italy<10277<253320<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Neuhofstrasse 2, 6340 Baar, Schweiz<10277<253320<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Árpád u. 45-71, Budapest 1196, Hungary<37043<1022426<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Neuhofstrasse 2, 6340 Baar, Schweiz<37043<1022426<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Grüner Weg 3-1, 50825 Köln, Germany<21540<599649<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Neuhofstrasse 2, 6340 Baar, Schweiz<21540<599649<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Perjenerweg 2, 6500 Landeck, Austria<9038<205863<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Neuhofstrasse 2, 6340 Baar, Schweiz<9038<205863<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<7163<194421<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Neuhofstrasse 2, 6340 Baar, Schweiz<7163<194421<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Rütistrasse 28, 6032 Emmen, Schweiz<1424<29327<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Neuhofstrasse 2, 6340 Baar, Schweiz<1424<29327<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Röhrliberg 32, 6330 Cham, Schweiz<556<8803<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Neuhofstrasse 2, 6340 Baar, Schweiz<556<8803<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Via Vergiò 8-18, 6932 Lugano, Schweiz<7436<181474<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Neuhofstrasse 2, 6340 Baar, Schweiz<7436<181474<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<4560<118153<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Neuhofstrasse 2, 6340 Baar, Schweiz<4560<118153<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<7868<211996<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Neuhofstrasse 2, 6340 Baar, Schweiz<7868<211996<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Chemin du Château 2-8, 1920 Martigny, Schweiz<9449<260932<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Neuhofstrasse 2, 6340 Baar, Schweiz<9449<260932<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Hauptstrasse 37, 32 Aarau, Schweiz<2914<67863<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Neuhofstrasse 2, 6340 Baar, Schweiz<2914<67863<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Hochschulstrasse 6, 3012 Bern, Schweiz<4138<91254<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Chemin de la Combatte 3, 2802 Develier, Schweiz<4138<91254<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Burgerstrasse 13-15, 3600 Thun, Schweiz<4922<116532<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Chemin de la Combatte 3, 2802 Develier, Schweiz<4922<116532<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Via Bergamina 14-18, 20016 Pero MI, Italy<14149<343878<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Chemin de la Combatte 3, 2802 Develier, Schweiz<14149<343878<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Árpád u. 45-71, Budapest 1196, Hungary<41458<1130477<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Chemin de la Combatte 3, 2802 Develier, Schweiz<41458<1130477<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Grüner Weg 3-1, 50825 Köln, Germany<20367<538956<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Chemin de la Combatte 3, 2802 Develier, Schweiz<20367<538956<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Perjenerweg 2, 6500 Landeck, Austria<14018<330517<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Chemin de la Combatte 3, 2802 Develier, Schweiz<14018<330517<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<6103<150500<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Chemin de la Combatte 3, 2802 Develier, Schweiz<6103<150500<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Rütistrasse 28, 6032 Emmen, Schweiz<5134<104461<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Chemin de la Combatte 3, 2802 Develier, Schweiz<5134<104461<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Röhrliberg 32, 6330 Cham, Schweiz<5482<124270<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Chemin de la Combatte 3, 2802 Develier, Schweiz<5482<124270<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Via Vergiò 8-18, 6932 Lugano, Schweiz<11308<272032<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Chemin de la Combatte 3, 2802 Develier, Schweiz<11308<272032<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<4062<91935<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Chemin de la Combatte 3, 2802 Develier, Schweiz<4062<91935<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<5610<120536<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Chemin de la Combatte 3, 2802 Develier, Schweiz<5610<120536<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Chemin du Château 2-8, 1920 Martigny, Schweiz<8389<217011<<";
		
		
		String distancesCSV8 = "Chemin du Château 2-8, 1920 Martigny, Schweiz<Chemin de la Combatte 3, 2802 Develier, Schweiz<8389<217011<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Hauptstrasse 37, 32 Aarau, Schweiz<4401<87267<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Chemin de la Combatte 3, 2802 Develier, Schweiz<4401<87267<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Neuhofstrasse 2, 6340 Baar, Schweiz<5750<130975<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Chemin de la Combatte 3, 2802 Develier, Schweiz<5750<130975<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Hochschulstrasse 6, 3012 Bern, Schweiz<9113<242277<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Giacomettistrasse 114-116, 7000 Chur, Schweiz<9113<242277<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Burgerstrasse 13-15, 3600 Thun, Schweiz<9897<267555<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Giacomettistrasse 114-116, 7000 Chur, Schweiz<9897<267555<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Via Bergamina 14-18, 20016 Pero MI, Italy<9386<218394<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Giacomettistrasse 114-116, 7000 Chur, Schweiz<9386<218394<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Árpád u. 45-71, Budapest 1196, Hungary<34420<956906<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Giacomettistrasse 114-116, 7000 Chur, Schweiz<34420<956906<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Grüner Weg 3-1, 50825 Köln, Germany<23941<663008<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Giacomettistrasse 114-116, 7000 Chur, Schweiz<23941<663008<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Perjenerweg 2, 6500 Landeck, Austria<6415<140343<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Giacomettistrasse 114-116, 7000 Chur, Schweiz<6415<140343<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<11078<301524<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Giacomettistrasse 114-116, 7000 Chur, Schweiz<11078<301524<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Rütistrasse 28, 6032 Emmen, Schweiz<5788<139544<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Giacomettistrasse 114-116, 7000 Chur, Schweiz<5788<139544<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Röhrliberg 32, 6330 Cham, Schweiz<4921<119020<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Giacomettistrasse 114-116, 7000 Chur, Schweiz<4921<119020<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Via Vergiò 8-18, 6932 Lugano, Schweiz<6545<146547<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Giacomettistrasse 114-116, 7000 Chur, Schweiz<6545<146547<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<8475<225255<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Giacomettistrasse 114-116, 7000 Chur, Schweiz<8475<225255<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<11783<319098<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Giacomettistrasse 114-116, 7000 Chur, Schweiz<11783<319098<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Chemin du Château 2-8, 1920 Martigny, Schweiz<13364<368035<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Giacomettistrasse 114-116, 7000 Chur, Schweiz<13364<368035<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Hauptstrasse 37, 32 Aarau, Schweiz<6252<162356<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Giacomettistrasse 114-116, 7000 Chur, Schweiz<6252<162356<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Neuhofstrasse 2, 6340 Baar, Schweiz<4766<113982<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Giacomettistrasse 114-116, 7000 Chur, Schweiz<4766<113982<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Chemin de la Combatte 3, 2802 Develier, Schweiz<9692<237992<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Giacomettistrasse 114-116, 7000 Chur, Schweiz<9692<237992<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Hochschulstrasse 6, 3012 Bern, Schweiz<66809<2020003<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<66809<2020003<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Burgerstrasse 13-15, 3600 Thun, Schweiz<67854<2053501<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<67854<2053501<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Via Bergamina 14-18, 20016 Pero MI, Italy<71330<2156682<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<71330<2156682<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Árpád u. 45-71, Budapest 1196, Hungary<102570<3110487<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<102570<3110487<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Grüner Weg 3-1, 50825 Köln, Germany<73220<2238519<<";
		String distancesCSV6="Grüner Weg 3-1, 50825 Köln, Germany<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<73220<2238519<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Perjenerweg 2, 6500 Landeck, Austria<79451<2321735<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<79451<2321735<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<65026<1972290<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<65026<1972290<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Rütistrasse 28, 6032 Emmen, Schweiz<70654<2125104<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<70654<2125104<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Röhrliberg 32, 6330 Cham, Schweiz<71003<2144913<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<71003<2144913<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Via Vergiò 8-18, 6932 Lugano, Schweiz<74039<2226867<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<74039<2226867<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<67597<2042612<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<67597<2042612<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<64470<1957058<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<64470<1957058<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Chemin du Château 2-8, 1920 Martigny, Schweiz<65916<1998048<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<65916<1998048<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Hauptstrasse 37, 32 Aarau, Schweiz<69921<2107910<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<69921<2107910<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Neuhofstrasse 2, 6340 Baar, Schweiz<71270<2151618<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<71270<2151618<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Chemin de la Combatte 3, 2802 Develier, Schweiz<67257<1995756<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<67257<1995756<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Giacomettistrasse 114-116, 7000 Chur, Schweiz<75110<2229246<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<75110<2229246<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Hochschulstrasse 6, 3012 Bern, Schweiz<52618<1611499<<"; 
		
		String distancesCSV2 = "Hochschulstrasse 6, 3012 Bern, Schweiz<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<52618<1611499<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Burgerstrasse 13-15, 3600 Thun, Schweiz<53664<1644998<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<53664<1644998<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Via Bergamina 14-18, 20016 Pero MI, Italy<55572<1657874<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<55572<1657874<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Árpád u. 45-71, Budapest 1196, Hungary<86812<2611679<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<86812<2611679<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Grüner Weg 3-1, 50825 Köln, Germany<61166<1831258<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<61166<1831258<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Perjenerweg 2, 6500 Landeck, Austria<65348<1942656<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<65348<1942656<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<50836<1563787<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<50836<1563787<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Rütistrasse 28, 6032 Emmen, Schweiz<56463<1716601<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<56463<1716601<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Röhrliberg 32, 6330 Cham, Schweiz<56812<1736409<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<56812<1736409<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Via Vergiò 8-18, 6932 Lugano, Schweiz<58282<1728059<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<58282<1728059<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<53406<1634108<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<53406<1634108<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<50279<1548554<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<50279<1548554<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Chemin du Château 2-8, 1920 Martigny, Schweiz<51725<1589545<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<51725<1589545<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Hauptstrasse 37, 32 Aarau, Schweiz<55730<1699407<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<55730<1699407<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Neuhofstrasse 2, 6340 Baar, Schweiz<57079<1743115<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<57079<1743115<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Chemin de la Combatte 3, 2802 Develier, Schweiz<55053<1686628<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<55053<1686628<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Giacomettistrasse 114-116, 7000 Chur, Schweiz<61006<1850168<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<61006<1850168<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<19351<603869<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<19351<603869<<";
		String distancesCSV7 = "Calle Generación 21-9, 29196 Málaga,  Spain<Hochschulstrasse 6, 3012 Bern, Schweiz<62351<1928530<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Calle Generación 21-9, 29196 Málaga,  Spain<62351<1928530<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Burgerstrasse 13-15, 3600 Thun, Schweiz<63396<1962029<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Calle Generación 21-9, 29196 Málaga,  Spain<63396<1962029<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Via Bergamina 14-18, 20016 Pero MI, Italy<65305<1974905<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Calle Generación 21-9, 29196 Málaga,  Spain<65305<1974905<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Árpád u. 45-71, Budapest 1196, Hungary<96545<2928710<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Calle Generación 21-9, 29196 Málaga,  Spain<96545<2928710<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Grüner Weg 3-1, 50825 Köln, Germany<75319<2280527<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Calle Generación 21-9, 29196 Málaga,  Spain<75319<2280527<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Perjenerweg 2, 6500 Landeck, Austria<75080<2259687<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Calle Generación 21-9, 29196 Málaga,  Spain<75080<2259687<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<60568<1880817<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Calle Generación 21-9, 29196 Málaga,  Spain<60568<1880817<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Rütistrasse 28, 6032 Emmen, Schweiz<66196<2033631<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Calle Generación 21-9, 29196 Málaga,  Spain<66196<2033631<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Röhrliberg 32, 6330 Cham, Schweiz<66545<2053440<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Calle Generación 21-9, 29196 Málaga,  Spain<66545<2053440<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Via Vergiò 8-18, 6932 Lugano, Schweiz<68014<2045089<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Calle Generación 21-9, 29196 Málaga,  Spain<68014<2045089<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<63139<1951139<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Calle Generación 21-9, 29196 Málaga,  Spain<63139<1951139<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<60012<1865585<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Calle Generación 21-9, 29196 Málaga,  Spain<60012<1865585<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Chemin du Château 2-8, 1920 Martigny, Schweiz<61458<1906575<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Calle Generación 21-9, 29196 Málaga,  Spain<61458<1906575<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Hauptstrasse 37, 32 Aarau, Schweiz<65463<2016438<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Calle Generación 21-9, 29196 Málaga,  Spain<65463<2016438<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Neuhofstrasse 2, 6340 Baar, Schweiz<66812<2060145<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Calle Generación 21-9, 29196 Málaga,  Spain<66812<2060145<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Chemin de la Combatte 3, 2802 Develier, Schweiz<64785<2003658<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Calle Generación 21-9, 29196 Málaga,  Spain<64785<2003658<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Giacomettistrasse 114-116, 7000 Chur, Schweiz<70739<2167198<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Calle Generación 21-9, 29196 Málaga,  Spain<70739<2167198<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<22698<665736<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Calle Generación 21-9, 29196 Málaga,  Spain<22698<665736<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<15852<482248<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Calle Generación 21-9, 29196 Málaga,  Spain<15852<482248<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Hochschulstrasse 6, 3012 Bern, Schweiz<23490<676836<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<23490<676836<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Burgerstrasse 13-15, 3600 Thun, Schweiz<24274<702114<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<24274<702114<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Via Bergamina 14-18, 20016 Pero MI, Italy<32416<911457<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<32416<911457<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Árpád u. 45-71, Budapest 1196, Hungary<47295<1383311<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<47295<1383311<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Grüner Weg 3-1, 50825 Köln, Germany<7921<224353<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<7921<224353<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Perjenerweg 2, 6500 Landeck, Austria<30920<818047<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<30920<818047<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<25454<736082<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<25454<736082<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Rütistrasse 28, 6032 Emmen, Schweiz<23401<672040<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<23401<672040<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Röhrliberg 32, 6330 Cham, Schweiz<23465<682499<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<23465<682499<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Via Vergiò 8-18, 6932 Lugano, Schweiz<29575<839610<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<29575<839610<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<22851<659814<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<22851<659814<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<25260<633922<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<25260<633922<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Chemin du Château 2-8, 1920 Martigny, Schweiz<27741<802594<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<27741<802594<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Hauptstrasse 37, 32 Aarau, Schweiz<21939<632409<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<21939<632409<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Neuhofstrasse 2, 6340 Baar, Schweiz<23616<686230<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<23616<686230<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Chemin de la Combatte 3, 2802 Develier, Schweiz<20598<575320<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<20598<575320<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Giacomettistrasse 114-116, 7000 Chur, Schweiz<26964<781092<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<26964<781092<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<67549<2062934<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<67549<2062934<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<55464<1653732<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<55464<1653732<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Calle Generación 21-9, 29196 Málaga,  Spain<69538<2105994<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<69538<2105994<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Hochschulstrasse 6, 3012 Bern, Schweiz<51308<1366666<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<51308<1366666<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Burgerstrasse 13-15, 3600 Thun, Schweiz<51173<1333796<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<51173<1333796<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Via Bergamina 14-18, 20016 Pero MI, Italy<38567<1030896<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<38567<1030896<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Árpád u. 45-71, Budapest 1196, Hungary<23041<550237<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<23041<550237<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Grüner Weg 3-1, 50825 Köln, Germany<55626<1469211<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<55626<1469211<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Perjenerweg 2, 6500 Landeck, Austria<40901<1054588<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<40901<1054588<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<51940<1348643<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<51940<1348643<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Rütistrasse 28, 6032 Emmen, Schweiz<47593<1261189<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<47593<1261189<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Röhrliberg 32, 6330 Cham, Schweiz<47705<1264566<<"; 
		
		String distancesCSV9 = "Röhrliberg 32, 6330 Cham, Schweiz<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<47705<1264566<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Via Vergiò 8-18, 6932 Lugano, Schweiz<40997<1095702<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<40997<1095702<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<50669<1349644<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<50669<1349644<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<53485<1437292<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<53485<1437292<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Chemin du Château 2-8, 1920 Martigny, Schweiz<49665<1282486<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<49665<1282486<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Hauptstrasse 37, 32 Aarau, Schweiz<49698<1284739<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<49698<1284739<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Neuhofstrasse 2, 6340 Baar, Schweiz<47973<1271271<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<47973<1271271<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Chemin de la Combatte 3, 2802 Develier, Schweiz<51887<1362381<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<51887<1362381<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Giacomettistrasse 114-116, 7000 Chur, Schweiz<46101<1207820<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<46101<1207820<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<108597<3157098<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<108597<3157098<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<92767<2656815<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<92767<2656815<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Calle Generación 21-9, 29196 Málaga,  Spain<102377<2975761<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<102377<2975761<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<61904<1678662<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<61904<1678662<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Hochschulstrasse 6, 3012 Bern, Schweiz<47950<1436449<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Kaliskiego 25, 01-476 Warszawa,  Poland<47950<1436449<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Burgerstrasse 13-15, 3600 Thun, Schweiz<48734<1461727<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Kaliskiego 25, 01-476 Warszawa,  Poland<48734<1461727<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Via Bergamina 14-18, 20016 Pero MI, Italy<50962<1508131<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Kaliskiego 25, 01-476 Warszawa,  Poland<50962<1508131<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Árpád u. 45-71, Budapest 1196, Hungary<30860<908068<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Kaliskiego 25, 01-476 Warszawa,  Poland<30860<908068<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Grüner Weg 3-1, 50825 Köln, Germany<36642<1101239<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Kaliskiego 25, 01-476 Warszawa,  Poland<36642<1101239<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Perjenerweg 2, 6500 Landeck, Austria<42492<1231283<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Kaliskiego 25, 01-476 Warszawa,  Poland<42492<1231283<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<49914<1495696<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Kaliskiego 25, 01-476 Warszawa,  Poland<49914<1495696<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Rütistrasse 28, 6032 Emmen, Schweiz<45920<1385034<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Kaliskiego 25, 01-476 Warszawa,  Poland<45920<1385034<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Röhrliberg 32, 6330 Cham, Schweiz<45052<1364510<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Kaliskiego 25, 01-476 Warszawa,  Poland<45052<1364510<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Via Vergiò 8-18, 6932 Lugano, Schweiz<48121<1436284<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Kaliskiego 25, 01-476 Warszawa,  Poland<48121<1436284<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<47311<1419427<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Kaliskiego 25, 01-476 Warszawa,  Poland<47311<1419427<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<50619<1513270<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Kaliskiego 25, 01-476 Warszawa,  Poland<50619<1513270<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Chemin du Château 2-8, 1920 Martigny, Schweiz<52200<1562207<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Kaliskiego 25, 01-476 Warszawa,  Poland<52200<1562207<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Hauptstrasse 37, 32 Aarau, Schweiz<45364<1318733<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Kaliskiego 25, 01-476 Warszawa,  Poland<45364<1318733<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Neuhofstrasse 2, 6340 Baar, Schweiz<44898<1359472<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Kaliskiego 25, 01-476 Warszawa,  Poland<44898<1359472<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Chemin de la Combatte 3, 2802 Develier, Schweiz<47109<1384158<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Kaliskiego 25, 01-476 Warszawa,  Poland<47109<1384158<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Giacomettistrasse 114-116, 7000 Chur, Schweiz<42255<1294259<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Kaliskiego 25, 01-476 Warszawa,  Poland<42255<1294259<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<109004<3326335<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Kaliskiego 25, 01-476 Warszawa,  Poland<109004<3326335<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<96919<2917133<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Kaliskiego 25, 01-476 Warszawa,  Poland<96919<2917133<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Calle Generación 21-9, 29196 Málaga,  Spain<107758<3329198<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Kaliskiego 25, 01-476 Warszawa,  Poland<107758<3329198<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<42815<1287605<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Kaliskiego 25, 01-476 Warszawa,  Poland<42815<1287605<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<51676<1405194<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Kaliskiego 25, 01-476 Warszawa,  Poland<51676<1405194<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Hochschulstrasse 6, 3012 Bern, Schweiz<27115<757869<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Im Steinkampe 14-16, 38110 Braunschweig, Germany<27115<757869<<"; 
		String distancesCSV10 = "Im Steinkampe 14-16, 38110 Braunschweig, Germany<Burgerstrasse 13-15, 3600 Thun, Schweiz<27899<783147<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Im Steinkampe 14-16, 38110 Braunschweig, Germany<27899<783147<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Via Bergamina 14-18, 20016 Pero MI, Italy<34603<952827<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Im Steinkampe 14-16, 38110 Braunschweig, Germany<34603<952827<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Árpád u. 45-71, Budapest 1196, Hungary<34468<995258<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Im Steinkampe 14-16, 38110 Braunschweig, Germany<34468<995258<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Grüner Weg 3-1, 50825 Köln, Germany<12635<347196<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Im Steinkampe 14-16, 38110 Braunschweig, Germany<12635<347196<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Perjenerweg 2, 6500 Landeck, Austria<27089<736213<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Im Steinkampe 14-16, 38110 Braunschweig, Germany<27089<736213<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<29079<817115<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Im Steinkampe 14-16, 38110 Braunschweig, Germany<29079<817115<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Rütistrasse 28, 6032 Emmen, Schweiz<27026<753073<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Im Steinkampe 14-16, 38110 Braunschweig, Germany<27026<753073<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Röhrliberg 32, 6330 Cham, Schweiz<27090<763533<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Im Steinkampe 14-16, 38110 Braunschweig, Germany<27090<763533<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Via Vergiò 8-18, 6932 Lugano, Schweiz<31762<880980<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Im Steinkampe 14-16, 38110 Braunschweig, Germany<31762<880980<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<26476<740847<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Im Steinkampe 14-16, 38110 Braunschweig, Germany<26476<740847<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<29784<834690<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Im Steinkampe 14-16, 38110 Braunschweig, Germany<29784<834690<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Chemin du Château 2-8, 1920 Martigny, Schweiz<31365<883627<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Im Steinkampe 14-16, 38110 Braunschweig, Germany<31365<883627<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Hauptstrasse 37, 32 Aarau, Schweiz<25564<713442<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Im Steinkampe 14-16, 38110 Braunschweig, Germany<25564<713442<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Neuhofstrasse 2, 6340 Baar, Schweiz<27240<767263<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Im Steinkampe 14-16, 38110 Braunschweig, Germany<27240<767263<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Chemin de la Combatte 3, 2802 Develier, Schweiz<26274<705578<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Im Steinkampe 14-16, 38110 Braunschweig, Germany<26274<705578<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Giacomettistrasse 114-116, 7000 Chur, Schweiz<25896<738955<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Im Steinkampe 14-16, 38110 Braunschweig, Germany<25896<738955<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<84997<2572292<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Im Steinkampe 14-16, 38110 Braunschweig, Germany<84997<2572292<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<72912<2163090<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Im Steinkampe 14-16, 38110 Braunschweig, Germany<72912<2163090<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Calle Generación 21-9, 29196 Málaga,  Spain<86923<2650618<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Im Steinkampe 14-16, 38110 Braunschweig, Germany<86923<2650618<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<18809<533562<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Im Steinkampe 14-16, 38110 Braunschweig, Germany<18809<533562<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<55010<1483340<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Im Steinkampe 14-16, 38110 Braunschweig, Germany<55010<1483340<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Kaliskiego 25, 01-476 Warszawa,  Poland<24534<758159<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Im Steinkampe 14-16, 38110 Braunschweig, Germany<24534<758159<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Hochschulstrasse 6, 3012 Bern, Schweiz<99041<2699628<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Komsomolskaya pl. 1, Moskva, Russia 107140<99041<2699628<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Burgerstrasse 13-15, 3600 Thun, Schweiz<99825<2724906<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Komsomolskaya pl. 1, Moskva, Russia 107140<99825<2724906<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Via Bergamina 14-18, 20016 Pero MI, Italy<102053<2771310<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Komsomolskaya pl. 1, Moskva, Russia 107140<102053<2771310<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Árpád u. 45-71, Budapest 1196, Hungary<78752<1852008<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Komsomolskaya pl. 1, Moskva, Russia 107140<78752<1852008<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Grüner Weg 3-1, 50825 Köln, Germany<87733<2364418<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Komsomolskaya pl. 1, Moskva, Russia 107140<87733<2364418<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Perjenerweg 2, 6500 Landeck, Austria<93584<2494462<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Komsomolskaya pl. 1, Moskva, Russia 107140<93584<2494462<<";
		String distancesCSV11 = 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<101006<2758874<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Komsomolskaya pl. 1, Moskva, Russia 107140<101006<2758874<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Rütistrasse 28, 6032 Emmen, Schweiz<97012<2648213<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Komsomolskaya pl. 1, Moskva, Russia 107140<97012<2648213<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Röhrliberg 32, 6330 Cham, Schweiz<96144<2627689<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Komsomolskaya pl. 1, Moskva, Russia 107140<96144<2627689<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Via Vergiò 8-18, 6932 Lugano, Schweiz<99213<2699463<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Komsomolskaya pl. 1, Moskva, Russia 107140<99213<2699463<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<98403<2682606<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Komsomolskaya pl. 1, Moskva, Russia 107140<98403<2682606<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<101711<2776449<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Komsomolskaya pl. 1, Moskva, Russia 107140<101711<2776449<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Chemin du Château 2-8, 1920 Martigny, Schweiz<103292<2825386<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Komsomolskaya pl. 1, Moskva, Russia 107140<103292<2825386<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Hauptstrasse 37, 32 Aarau, Schweiz<96456<2581911<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Komsomolskaya pl. 1, Moskva, Russia 107140<96456<2581911<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Neuhofstrasse 2, 6340 Baar, Schweiz<95990<2622651<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Komsomolskaya pl. 1, Moskva, Russia 107140<95990<2622651<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Chemin de la Combatte 3, 2802 Develier, Schweiz<98200<2647337<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Komsomolskaya pl. 1, Moskva, Russia 107140<98200<2647337<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Giacomettistrasse 114-116, 7000 Chur, Schweiz<93346<2557438<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Komsomolskaya pl. 1, Moskva, Russia 107140<93346<2557438<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<160095<4589514<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Komsomolskaya pl. 1, Moskva, Russia 107140<160095<4589514<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<148010<4180312<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Komsomolskaya pl. 1, Moskva, Russia 107140<148010<4180312<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Calle Generación 21-9, 29196 Málaga,  Spain<158849<4592377<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Komsomolskaya pl. 1, Moskva, Russia 107140<158849<4592377<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<93907<2550784<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Komsomolskaya pl. 1, Moskva, Russia 107140<93907<2550784<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<100783<2381926<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Komsomolskaya pl. 1, Moskva, Russia 107140<100783<2381926<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Kaliskiego 25, 01-476 Warszawa,  Poland<51434<1265528<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Komsomolskaya pl. 1, Moskva, Russia 107140<51434<1265528<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Im Steinkampe 14-16, 38110 Braunschweig, Germany<75742<2019304<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Komsomolskaya pl. 1, Moskva, Russia 107140<75742<2019304<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Hochschulstrasse 6, 3012 Bern, Schweiz<72281<1940898<<" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Piata Dorobantilor 2,  Bucuresti, Romania<72281<1940898<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Burgerstrasse 13-15, 3600 Thun, Schweiz<73065<1966176<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Piata Dorobantilor 2,  Bucuresti, Romania<73065<1966176<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Via Bergamina 14-18, 20016 Pero MI, Italy<65014<1794249<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Piata Dorobantilor 2,  Bucuresti, Romania<65014<1794249<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Árpád u. 45-71, Budapest 1196, Hungary<32929<825535<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Piata Dorobantilor 2,  Bucuresti, Romania<32929<825535<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Grüner Weg 3-1, 50825 Köln, Germany<72914<1978064<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Piata Dorobantilor 2,  Bucuresti, Romania<72914<1978064<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Perjenerweg 2, 6500 Landeck, Austria<60623<1630826<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Piata Dorobantilor 2,  Bucuresti, Romania<60623<1630826<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<74245<2000144<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Piata Dorobantilor 2,  Bucuresti, Romania<74245<2000144<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Rütistrasse 28, 6032 Emmen, Schweiz<69488<1874833<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Piata Dorobantilor 2,  Bucuresti, Romania<69488<1874833<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Röhrliberg 32, 6330 Cham, Schweiz<68620<1854309<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Piata Dorobantilor 2,  Bucuresti, Romania<68620<1854309<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Via Vergiò 8-18, 6932 Lugano, Schweiz<67443<1859055<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Piata Dorobantilor 2,  Bucuresti, Romania<67443<1859055<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<71642<1923876<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Piata Dorobantilor 2,  Bucuresti, Romania<71642<1923876<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<74951<2017718<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Piata Dorobantilor 2,  Bucuresti, Romania<74951<2017718<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Chemin du Château 2-8, 1920 Martigny, Schweiz<76112<2045839<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Piata Dorobantilor 2,  Bucuresti, Romania<76112<2045839<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Hauptstrasse 37, 32 Aarau, Schweiz<69419<1860977<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Piata Dorobantilor 2,  Bucuresti, Romania<69419<1860977<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Neuhofstrasse 2, 6340 Baar, Schweiz<68466<1849271<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Piata Dorobantilor 2,  Bucuresti, Romania<68466<1849271<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Chemin de la Combatte 3, 2802 Develier, Schweiz<72860<1936613<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Piata Dorobantilor 2,  Bucuresti, Romania<72860<1936613<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Giacomettistrasse 114-116, 7000 Chur, Schweiz<65823<1784058<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Piata Dorobantilor 2,  Bucuresti, Romania<65823<1784058<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<135043<3920451<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Piata Dorobantilor 2,  Bucuresti, Romania<135043<3920451<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<119213<3420167<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Piata Dorobantilor 2,  Bucuresti, Romania<119213<3420167<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Calle Generación 21-9, 29196 Málaga,  Spain<128824<3739114<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Piata Dorobantilor 2,  Bucuresti, Romania<128824<3739114<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<79193<2187515<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Piata Dorobantilor 2,  Bucuresti, Romania<79193<2187515<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<46299<890082<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Piata Dorobantilor 2,  Bucuresti, Romania<46299<890082<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Kaliskiego 25, 01-476 Warszawa,  Poland<62517<1714103<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Piata Dorobantilor 2,  Bucuresti, Romania<62517<1714103<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Im Steinkampe 14-16, 38110 Braunschweig, Germany<66396<1801565<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Piata Dorobantilor 2,  Bucuresti, Romania<66396<1801565<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Komsomolskaya pl. 1, Moskva, Russia 107140<77675<1782626<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Piata Dorobantilor 2,  Bucuresti, Romania<77675<1782626<<" + 
		"Hibernate: select address0_.id as id1_0_, address0_.city as city2_0_, address0_.country as country3_0_, address0_.status as status4_0_, address0_.street as street5_0_ from address address0_" + 
		"Hochschulstrasse 6, 3012 Bern, Schweiz<Hochschulstrasse 6, 3012 Bern, Schweiz<0<0<<" + 
		"Burgerstrasse 13-15, 3600 Thun, Schweiz<Burgerstrasse 13-15, 3600 Thun, Schweiz<0<0<<" + 
		"Via Bergamina 14-18, 20016 Pero MI, Italy<Via Bergamina 14-18, 20016 Pero MI, Italy<0<0<<" + 
		"Árpád u. 45-71, Budapest 1196, Hungary<Árpád u. 45-71, Budapest 1196, Hungary<0<0<<" + 
		"Grüner Weg 3-1, 50825 Köln, Germany<Grüner Weg 3-1, 50825 Köln, Germany<0<0<<" + 
		"Perjenerweg 2, 6500 Landeck, Austria<Perjenerweg 2, 6500 Landeck, Austria<0<0<<" + 
		"Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<Vue-des-Alpes 19-1, 1627 Vaulruz, Schweiz<0<0<<" + 
		"Rütistrasse 28, 6032 Emmen, Schweiz<Rütistrasse 28, 6032 Emmen, Schweiz<0<0<<" + 
		"Röhrliberg 32, 6330 Cham, Schweiz<Röhrliberg 32, 6330 Cham, Schweiz<0<0<<" + 
		"Via Vergiò 8-18, 6932 Lugano, Schweiz<Via Vergiò 8-18, 6932 Lugano, Schweiz<0<0<<" + 
		"Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<Meiefeldstrasse 34, 3400 Burgdorf, Schweiz<0<0<<" + 
		"Les Chandelènes 2-4, 1436 Chamblon, Schweiz<Les Chandelènes 2-4, 1436 Chamblon, Schweiz<0<0<<" + 
		"Chemin du Château 2-8, 1920 Martigny, Schweiz<Chemin du Château 2-8, 1920 Martigny, Schweiz<0<0<<" + 
		"Hauptstrasse 37, 32 Aarau, Schweiz<Hauptstrasse 37, 32 Aarau, Schweiz<0<0<<" + 
		"Neuhofstrasse 2, 6340 Baar, Schweiz<Neuhofstrasse 2, 6340 Baar, Schweiz<0<0<<" + 
		"Chemin de la Combatte 3, 2802 Develier, Schweiz<Chemin de la Combatte 3, 2802 Develier, Schweiz<0<0<<" + 
		"Giacomettistrasse 114-116, 7000 Chur, Schweiz<Giacomettistrasse 114-116, 7000 Chur, Schweiz<0<0<<" + 
		"Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<Praceta Rosa 1,  2770-123 Paço de Arcos,  Portugal<0<0<<" + 
		"Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<Ronda de el Olivar de los Pozos 4 ,  45004 Toledo,  Spain<0<0<<" + 
		"Calle Generación 21-9, 29196 Málaga,  Spain<Calle Generación 21-9, 29196 Málaga,  Spain<0<0<<" + 
		"Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<Rue Alexandre Wouters 7-12,  1090 Jette,  Belgium<0<0<<" + 
		"Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<Partizanskog odreda Zvijezda 2-22, Vogošća, Bosnia and Herzegovina<0<0<<" + 
		"Kaliskiego 25, 01-476 Warszawa,  Poland<Kaliskiego 25, 01-476 Warszawa,  Poland<0<0<<" + 
		"Im Steinkampe 14-16, 38110 Braunschweig, Germany<Im Steinkampe 14-16, 38110 Braunschweig, Germany<0<0<<" + 
		"Komsomolskaya pl. 1, Moskva, Russia 107140<Komsomolskaya pl. 1, Moskva, Russia 107140<0<0<<" + 
		"Piata Dorobantilor 2,  Bucuresti, Romania<Piata Dorobantilor 2,  Bucuresti, Romania<0<0<<";	
}

