# Software requirements specification  

Einführung in Software Engineering 2017  
Universität Bern  
 
**Team 6**  
Alvaro Lahiguera  
Nathalie Froidevaux  
Dominic Schweizer  
Mauro Kiener  

# 1. Introduction

## 1.1 Purpose
We want to have a software system that helps logisticians building routes. Routes consist of items that should be delivered to addresses. The drivers will see their routes on their mobile phones and will mark each item as delivered or not delivered. The system will also keep track of the time each route has taken.
   
## 1.2 Stakeholders
### 1.2.1 Logisticians 
**What do they need to do?**   
They prepare the routes for the truckers. They decide which truck is going to deliver which item. They give the Truckers a list with the items the truckers should deliver and with the address of the customers.
  
**What do they except from the system?**   
* They need to have an overview of the pending orders.
* They should be able to group items to routes with a few clicks.
* They need to prepare routes with a few clicks and give a time estimation.
* They need to see open deliveries and be able to schedule them.

### 1.2.2 Drivers
**What do they need to do?**  
They deliver articles/items to a customer by driving a truck on a route. 

**What do they except from the system?**
* They should see their scheduled routes for the next few days on a mobile device.
* They should be able to mark deliveries as accepted or rejected at a place.
* They should measure the amount of time it took them to finish a route.
* They should be able to start and end a route.
* They should see their current route on a map.

### 1.2.3 Customers
They receive the deliveries. 

### 1.2.4 Management
Finances the software. Want to manage the same workload with less logisticians.

## 1.3 Definitions
* OrderItem: A physical object which should be delivered to a customer. Consists of an Item, an order and a route. Has one of the following status: open, shedueld, delivered.
* Item: Object that can be theoretically delivered to a customer. Item has a space that it uses in the truck.
* Order: Contains items, a customer and an address.
* Route: Contains orderitems, which then form one or more deliveries. A Route gets started by the driver, and starts at the company. After delivering the last delivery, the route ends at the company and gets stopped. A route measures the time between start and stop.
* Truck: Is driven by a driver and makes the route. Contains several orderItems. A truck has a capacity.

## 1.4 System Overview
The System will be a client-server application, the most common browsers as chrome and firefox will act as clients. The application follows a MVC architekture. At least one part of the application will be a responsive webpage to be usable on a mobile device.

# 2. Overall description

## 2.1 Use cases

![](https://github.com/scg-unibe-ch/ese2017-team6/blob/docu/delivyZustaende.jpg?raw=true)

### **2.1.1 Create Sample Data**
* *Actors:*  
Logisticians and drivers 
* *Description:*  
A user creates sample data for the system. 
* *Precondition:*  
A user has acces to the internet and the system is started. 
* *Scenario:*
> 1. The user clicks on the button 'create sample data'.
> 2. The system creates instances of users, addresses, customers, items and trucks. It additionally creates sample orders and sample routes.  
* *Postcondition:*  
Instances of users, addresses, customers, items, trucks, orders and routes exist in the system.  

### **2.1.2 Login**
* *Actors:*  
Logisticians and Drivers  
* *Description:*  
A logistician or a driver logs himself into the system with his username and password.  
* *Precondition:*  
A user has an account in the system. He has internet connection and knows his credentials. No user is logged in at the moment.  
* *Scenario:*
> 1. The user types in his username.
> 2. The user types in his password.
> 3. The user clicks on the 'login' button.
> 4. He gets welcomed by the system and depending on which pages he has acces to, he can select further options.  
* *Alternative Scenario:*
> 1. The user types in his username.
> 2. The user types in his password.
> 3. The user clicks on the 'login' button.
> 4. The system doesn't find a user with these credentials and won't allow a login. The user remains on the same page and is requested to type in valid inputs.  

### **2.1.3 Cockpit**
* *Actors:*  
Logisticians  
* *Description:*  
A logistician wants to have an overview.  
* *Precondition:*  
A logistician is logged into the system and has internet connection.  
* *Scenario:*
> 1. The user clicks on the button 'Cockpit' anywhere in the system.
> 2. The user sees a page with the urgent orders, non-urgent order, available ressources *(drivers, trucks)* and scheduled routes.  
  
 
### **2.1.4 Create new address**
* *Actors:*  
Logisticians  
* *Description:*  
A logistician wants to create a new address for a customer.  
* *Precondition:*  
A logistician is logged into the system and has internet connection.
* *Scenario:*
> 1. The user clicks on the 'Administration' button in the header of the page and selects 'Addresses'.
> 2. The user is led to a page where he sees an overview of all addresses.
> 3. The user clicks on the button 'create new address'.
> 4. The user types in valid credentials for the new address.
> 5. The user clicks on the 'submit' button.
> 6. The system checks if the address can be found by GoogleMaps and if so, calculates a distance between the newly added address and the already existing addresses in the system. 

* *Alternative Scenario:*
> 1. The user clicks on the 'Administration' button in the header of the page and selects 'Addresses'.
> 2. The user is led to a page where he sees an overview of all addresses.
> 3. The user clicks on the button 'create new address'.
> 4. The user types in valid credentials for the new address.
> 5. The user clicks on the 'submit' button.
> 6. The system can't find the address in GoogleMaps or can't calculate a distance to any of the existing addresses.
> 7. The user sees his inputs an can now select if he saves the address anyway ('submit anyway'). If that's not the case, he can go back to the overwiev.  

* *Postcondition:*  
The address is saved in the system.  

### **2.1.5 Create new customer**
* *Actors:*  
Logisticians  
* *Description:*  
A logistician wants to create a new customer.  
* *Precondition:*  
A logistician is logged into the system and has internet connection.
* *Scenario:*
> 1. The user clicks on the 'Administration' button in the header of the page and selects 'Customer'.
> 2. The user is led to a page where he sees an overview of all customers.
> 3. The user clicks on the button 'create new customer'.
> 4. The user types in valid credentials for the new customer and selects a delivery and a domicil address.
> 5. The user clicks on the 'submit' button.

* *Alternative Scenario:*
> 1. The user clicks on the 'Administration' button in the header of the page and selects 'Customer'.
> 2. The user is led to a page where he sees an overview of all customers.
> 3. The user clicks on the button 'create new customer'.
> 4. The user types in valid credentials for the new customer, but realizes that there exists no address in the system.
> 5. The user clicks on the 'create new address' button and performs the task 'Create new Address'.
> 6. Continue with 1. in *Scenario*.

* *Postcondition:*  
The address is saved in the system.  

### **2.1.6 Create new order**
* *Actors:*  
Logisticians  
* *Description:*  
A logistician gets a call from a customer and wants to create a new order for this customer.  
* *Precondition:*  
A logistician is logged into the system and has internet connection. The customer and the items he ordered already exist in the system.  
* *Scenario:*
> 1. The user selects 'Create new order' in the header.
> 2. The user selects a customer and a delivery date for the order.
> 3. The user adds all the items that the customer ordered with the ordered number. 
> 4. The user submits the order.  
* *Postcondition:*  
The order and all the items (orderItems) it contains are saved into the system with the status 'OPEN'.  

### **2.1.7 Create new route**
* *Actors:*  
Logisticians  
* *Description:*  
A logistician wants to create a new route.  
* *Precondition:*  
A logistician is logged into the system and has internet connection.  
* *Scenario:*
> 1. The user selects 'Routing' in the header.  
> 2. The user selects a date for the route, when the route should start.
> 3. The user clicks on the button 'create new route'.  
> 4. The user can now choose a driver and a truck for the route. He can only select drivers and truck that are available at the chosen date. 
> 5. The user submits the route. 
> 6. The user is led to a page, where he can manage the orders for this route. He can choose the orders that he wants to add to this route. If he wants to remove orderItems he can also do so.  
> 7. By clicking on 'route' he sees all the information about the route. 
* *Alternative Scenario 1:*  
> 1. The user selects 'create new route' in the cockpit.
> 2. Continue with 4. in *Scenario*.  
* *Postcondition:*  
The route gets saved in the system with the status 'OPEN'.  
      
### **2.1.8 Add orders to route**
* *Actors:*  
Logisticians  
* *Description:*  
A logistician wants to add schedule an order an add the order to a route.  
* *Precondition:*  
A logistician is logged into the system and has internet connection. At least one orderitem in the order has status 'OPEN'.  
* *Scenario:*
> 1. The user is on the profile-page of the required order.
> 2. The user selects 'schedule order'.
> 3. The user sees all the routes which satisfy the needs*(capacity, date)* to add the required order. 
> 4. The user selects a route.  
* *Alternative Scenario 1:*  
> 1. The user is on the profile-page of the required order.
> 2. The user selects 'schedule order'.
> 3. The user gets informed that no current route satisfies the needs*(capacity, date)* to add the required order. 
> 4. The user selects to create a new route.  
> 5. The user creates a route which satisfies the needs of the order.  
* *Alternative Scenario 2:*  
> 1. The user is on the cockpit-page.
> 2. The user selects 'Add order to Route' in the header.
> 3. The user selects 'add to route' at the required order. 
> 4. Continue with 3. in *Scenario* or *Alternative Scenario 1*.  
* *Alternative Scenario 3:*  
> 1. The user is on the cockpit-page.
> 2. The user selects 'add to route' at the required order in the cockpit-page. 
> 3. Continue with 3. in *Scenario* or *Alternative Scenario 1*.  
* *Postcondition:*  
All the orderitems of the order get added to the selected route and their status is set to 'SCHEDULED'.  

### **2.1.9 Start route**
* *Actors:*  
Drivers 
* *Description:*  
A driver wants to start a route.  
* *Precondition:*  
A driver is logged into the system and is connected to the internet.  
* *Scenario:*
> 1. The driver is on the profile-page himself.
> 2. The driver sees all the scheduled routes for him.
> 3. The driver clicks on 'start route' at the route he wants to start.  
* *Postcondition:*  
The status of the route is now switched to 'ON ROUTE'.  

### **2.1.10 Accept/reject delivery**
* *Actors:*  
Drivers 
* *Description:*  
A driver wants to accept/reject a delivery at a certain address.  
* *Precondition:*  
A driver is logged into the system and is connected to the internet. He finds himself on route and at an address where he delivers items.  
* *Scenario:*
> 1. The driver sees the route he has to take from the previous delivery to the delivery he wants to accept/reject.  
> 2. The driver accepts/rejects the delivery by clicking on the respectively buttons.  
* *Postcondition:*  
The status of the orderItems at this route is set to 'FINISHED' *(accept)* or back to 'OPEN' *(reject)*.  

### **2.1.11 Stop route**
* *Actors:*  
Drivers 
* *Description:*  
A driver wants to stop a route.  
* *Precondition:*  
A driver is logged into the system and is connected to the internet. He finished all the deliveries.  
* *Scenario:*
> 1. The driver is on the page, that signalizes him to drive home.
> 2. After driving home, the driver clicks on 'finish route'.  
* *Postcondition:*  
The status of the route is now switched to 'FINISHED' and is no longer editable.  

### **2.1.12 Automatic route generation**
* *Actors:*  
Logistician
* *Description:*  
A logistician wants to create routes for all the orders at a given date.  
* *Precondition:*  
A logistician is logged into the system and is connected to the internet.  
* *Scenario:*
> 1. The user clicks on the button 'Create routes automatically'.  
> 2. The user selects a date on which he wants to create routes automatically. 
> 3. The user clicks 'start'.  
* *Postcondition:*  
Routes are automatically generated based on the best options *(trucks, orders, distances)* for the given orders. Orders that are added to the routes change their status to 'SCHEDULED'.  

 

## 2.2 Actor characteristics
  
### 2.2.1 Logistician
Works on a desktop computer, he has an overview of the pending orders, schedules deliveries and assembles routes. He sees the route statistics.
### 2.2.2 Driver
The driver accesses the application mostly via a mobile device. He executes a route and sees every delivery and its items. He finishes each delivery on the route by clicking "delivered" or not "delivered/declined" 

# 3. Specific requirements

## 3.1 Functional requirements
### 3.1.1 Items:
* The logisticians creates Orders(Task: Create new Order) which consist of a Client and a delivery address and several OrderItems. After an item is created it belongs to the items that should be delivered (has status “open”. OrderItems can only be deleted if they were not delivered yet (if they have status “not delivered”).
* OrderItems have the status „delivered“, „scheduled“, „open“. After creation the have the status „open“. Once they are assigned to a route they get the status „scheduled“. Depending on whether the trucker is able to deliver the change to „delivered“ or again to „open“.
   
### 3.1.2 Driver:
* A Driver is a user with role 'Driver'. He has only the authentication to acces the overview of all Drivers. On His Profile, he can start a route, and after finishing the route, he can stop it.
   
### 3.1.3 Creation and administration of Routes:
* A route is created by the logistician (Task: Create new route). The logistician first selects a Driver from the available drivers and a truck from the available Trucks. Afterwards he selects one or more Orders with the status „OPEN“. The orderitems from the orders which are assigned to a route get the status „SCHEDULED“.
* Routes have three status: „OPEN“, „ON ROUTE“, „FINISHED“. Only routes with the status „OPEN“ can be deleted or manipulated. If a route is deleted all OrderItems in that route will change the status from „scheduled“ to „OPEN“.
* The logistician can access a list with all routes (Task: Show routes) where he sees the start date, the end-date, the driver, the expected time and the route status. By clicking on a route he can see the items which are in the route. Only routes with the status „OPEN“ can be deleted or manipulated.
   
### 3.1.4 Execution of routes:
* A driver can select to view the route on the map(Task: Show route on map) with the status „OPEN“ and can there start the route. The starting time of the route is registered and the route gets the status „ON ROUTE“.
* The driver sees on the ongoing route the next delivery that he has to execute. He can press „accept delivery“ or „reject delivery“ for each delivery. Depending on his choice the status of the items change from „scheduled“ to "finished" or back to „open“. In the „open“ case the item shows up again when a logistician is going to create a new route.
* The Route changes it's status to finished, when the driver finished the route (even if he rejected deliveries).
   
![AddDriver](https://github.com/scg-unibe-ch/ese2017-team6/blob/master/UIAddDriver.jpg)
![UIManageDrivers](https://github.com/scg-unibe-ch/ese2017-team6/blob/master/UImanageTruckers.jpg)
![UIDriver](https://github.com/scg-unibe-ch/ese2017-team6/blob/master/UIdriver.jpg)
  
## 3.2 Nonfunctional requirements
### 3.2.1 Stability and Robustness
We used TRAVIS CI and Heroku to ensure stability and robustness, it's very useful to test new commits automatically with heroku, failed commits were shown in github and could be fixed easily. Testing the app on heroku shows us additional bugs which couldn't be found on a local instance due changements of the infrastructure, for example, the deployment on heroku was supported by PostGreSQL while the initial development was in MySQL.
### 3.2.2 Security
It's a application in the open web, a minimal authorization and authentication system should be in place. User have a secure login, all passwords in the database will be encrypted. BCrypt is used for the encryption of the passwords.
### 3.2.3 Plattforms
The application should run on a server an be accessible from the internet. Some stakeholders might access the application from a mobile device. The application is currently deployed on heroku. https://eseteam6.herokuapp.com
To ensure a responsive design we used bootstrap for the styling.
### 3.2.4 DataStorage
The Application is currently configured to use PostGreSQL for deployment and H2 for testing, other databases can be installed easy if they're supported by spring.
### 3.2.5 Maintainability
For better maintainability the front and the back are completely separated. We used hmtl and thymeleaf to create the front, java and spring to create the back. Due to the MVC configuration it is very easy to create new views or to extend the functionality.
### 3.2.6 Accessibility 
The application is designed as a REST webservice, the whole communication between the client and the server is made with http-requests.

# 4. Questions:
* Should it be possible to split an order into multiple deliveries? Is possible from our implementation since a route consists of OrderItems and the OrderItems can come from different orders. **Undelivered items will be left open. It's possible to assign the same order to two different routes with different items.**
* Should it be possible to group multiple orders into one delivery? Is possible from our implementation since a tour consists of OrderItems and the OrderItems can come from different orders. **Yes it's possible. Orders to the same address will be grouped together**
* How do you find out when a truck is full? Each truck has a capacity. They have small and large trucks. Each item has the same size in a first step. **Truck has weight and size capacity, the items have a weight and a size.**
* How do you load the truck, are the items stacked or free accessible? free accessible, we do not have to take this into account. **Loading order was not taken into account.**
* Do you need to know the order history of a customer? Do you need a customer relationship plugin? Maybe the customer could get an e-mail or SMS when a OrderItem is going to be delivered. **Not yet needed, not yet implemented.**
* Should the driver see the delivery address on his user interface? **Yes, it's implemented.**
* How do you create your orders? How does a order look like? Who creates them? They consist of large machines. They come in in different ways like Fax, e-mail, Phone. **We can assume the logistician translates them into the System.**
* How should the software be able to propose tours: Is it enough if we store the adresses in the first place? **Automatic route generation implemented as additional feature. Distance between addresses is calculated by googleMaps**
* Does the driver need a offline capability? **Not needed. Homepage is enough.**

