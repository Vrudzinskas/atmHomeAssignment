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
- Open in your IDE of choice able to run Java (preferably IntellijIDEA), navigate to src/main/java/com.assignment.home.assignment/Application.java to find the main method to run. Project is set to run on port 8089. Language is set to Java 8
- Application was developed with MySQL database which I later replaced with H2 in-memory database. Old application.properties file for MySQL is included as application(mysql).properties, but it is probably easier to use H2 database for demo purposes.
- After you run the project one way or another, open your browser and URL http://localhost:8080/h2console.
- Login to H2 database with these details:
  
- You should then be able to execute queries on tables created in H2 in-memory database as you go along.
  

## API
- As per your requirements, I have created 2 endpoints:
1. Endpoint to get balance, overdraft, available withdrawal funds and available ATM cash to dispense: GET "http://localhost:8080/api/v1/balance" with JSON body as example:
   
2. Endpoint accepting cash withdrawal requests: POST "http://localhost:8080/api/v1/withdraw" with JSON body as example:
   


# REST Client
- I used Postman for testing.


