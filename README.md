# ATM Home Assignment 

This project is a simple ATM simulation. 
## Used technologies:
- Spring Boot
- Spring Data JPA
- H2 Database
- Lombok Annotation
- JUnit 5

## Requirements
For building and running the application you need:
- JDK 
- Lombok
- Maven

## Setup
- Open in your IDE of choice able to run Java (preferably IntellijIDEA), navigate to src/main/java/com.assignment.home.assignment/Application.java to find the main method to run. Project is set to run on port 8089. Language is set to Java 11
- Application was developed with MySQL database which I later replaced with H2 in-memory database. Old application.properties file for MySQL is included as application(mysql).properties, but it is probably easier to use H2 database for demo purposes.
- After you run the project one way or another, open your browser and URL "http://localhost:8089/h2console".
- Login to H2 database with these details, login "root", password "root", set in application.properties:
![Screenshot1](https://user-images.githubusercontent.com/11228131/193507351-44dee3fb-99a6-4225-9c6c-9982f3fa8da6.png)
- You should then be able to execute queries on tables created in H2 in-memory database as you go along to see it being modified after succesful withdraw queries:
![Screenshot2](https://user-images.githubusercontent.com/11228131/193508047-c2766a30-14a6-4d3b-bae7-5bd8184a79b4.png)
![Screenshot3](https://user-images.githubusercontent.com/11228131/193508101-b7db13e9-a9cd-4d4e-a04f-62a7ea4a56d9.png)

  

## API
- As per your requirements, I have created 2 endpoints:
1. Endpoint to get balance, overdraft, available withdrawal funds and available ATM cash to dispense: GET "http://localhost:8080/api/v1/balance" with JSON body as example:

![Screenshot4](https://user-images.githubusercontent.com/11228131/193508803-e305226b-7653-4dc6-883c-da4aec9a46e6.png)
I chose to send bank account and pin in the GET body, cause it is a little safer than sending them as URL parameters.

2. Endpoint accepting cash withdrawal requests: POST "http://localhost:8080/api/v1/withdraw" with JSON body as example:

![Screenshot5](https://user-images.githubusercontent.com/11228131/193508838-1a809c67-bf4a-48bc-8c8d-3d241d2e70d2.png)

## Features
- JPA with H2 database
- Custom exceptions and Exception Controller
- Basic Spring Boot validation implemented:

![Screenshot6](https://user-images.githubusercontent.com/11228131/193510139-fe1f856a-aabb-43ec-9a48-e350d5804dee.png)![Screenshot7](https://user-images.githubusercontent.com/11228131/193510155-06adb751-ef10-4af5-b8de-60036191585b.png)



