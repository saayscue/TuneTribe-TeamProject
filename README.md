# TuneTribe

TuneTribe is a Spring Boot web app that is a music community platform.

## Features

- **User**: Register and login
- **Artist**: Create and manage artist profiles
- **Admin Dashboard**: Admins to manage users, artists, and moderators
- **Community Guidelines**: Managing community standards
- **Copyright**: Copyright and policy
- **Moderation System**: Manage community content and requests
- **Role-Based Access Control**: Different views and permissions for regular users, artists, moderators, and admins

## Tech Stack

- **Framework**: Spring Boot 3.2.4
- **Java Version**: Java 21
- **Build Tool**: Maven
- **Database**: MySQL
- **ORM**: JPA Hibernate
- **Template Engine**: Thymeleaf
- **Security**: Spring Security with BCrypt password encryption
- **Development Tools**: Spring Boot DevTools, Lombok

## Before running the app, install:

- Java 21 or higher
- MySQL Server
- Maven (or use the included Maven wrapper)

## Setup

### 1. Database Setup (MySQL)

1. Start your MySQL server.
2. Create the database (I used phpMyAdmin to create it):

   ```sql
   CREATE DATABASE csc340_project;
   ```

3. (Optional) Create a dedicated MySQL user OR leave the default root username and no password:

   ```sql
   CREATE USER 'tunetribe'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON csc340_project.* TO 'tunetribe'@'localhost';
   FLUSH PRIVILEGES;
   ```

4. Hibernate will auto-create tables on first run (`spring.jpa.hibernate.ddl-auto=update`).

### 2. Database Connection

Edit (src/main/resources/application.properties) to match your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/csc340_project?useSSL=false
spring.datasource.username=root
spring.datasource.password=
```

Since root now has no password, your config should be:

spring.datasource.username=root
spring.datasource.password= (empty)

### 3. Build and Run the App

#### macOS/Linux

```bash
./mvnw clean install
./mvnw spring-boot:run
```

#### Windows

```bash
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

### 4. Access the App

Open your browser at:

```
http://localhost:8080
```

### 5. Create an Admin (Optional)

## Admin Setup

To make a user an admin:

1. Register a normal account in the app.
2. In MySQL, update the user’s `role` to `Admin` in the `user` table.
