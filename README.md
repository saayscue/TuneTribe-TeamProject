# TuneTribe

TuneTribe is a Spring Boot web application that serves as a music community platform. It provides user registration and authentication, user management, artist management, and administrative features for managing the community.

## Features

- **User Management**: Register and login
- **Artist Management**: Create and manage artist profiles
- **Admin Dashboard**: Administrative tools for managing users, artists, and moderators
- **Community Guidelines**: Managing community standards
- **Copyright Management**: Copyright claim and policy management system
- **Moderation System**:Manage community content and requests
- **Role-Based Access Control**: Different views and permissions for regular users, artists, moderators, and administrators

## Tech Stack

- **Framework**: Spring Boot 3.2.4
- **Java Version**: Java 21
- **Build Tool**: Maven
- **Database**: MySQL
- **ORM**: JPA Hibernate
- **Template Engine**: Thymeleaf
- **Security**: Spring Security with BCrypt password encryption
- **Development Tools**: Spring Boot DevTools, Lombok

## Prerequisites

Before running the application, ensure you have the following installed:

- Java 21 or higher
- MySQL Server
- Maven (or use the included Maven wrapper)

## Setup Instructions

### 1. Database Setup (MySQL)

1. Start your MySQL server.
2. Create the database:

   ```sql
   CREATE DATABASE csc340_project;
   ```

3. (Optional) Create a dedicated MySQL user (recommended):

   ```sql
   CREATE USER 'tunetribe'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON csc340_project.* TO 'tunetribe'@'localhost';
   FLUSH PRIVILEGES;
   ```

4. Hibernate will auto-create tables on first run (`spring.jpa.hibernate.ddl-auto=update`).

### 2. Configure Database Connection

Edit [src/main/resources/application.properties](src/main/resources/application.properties) to match your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/csc340_project?useSSL=false
spring.datasource.username=root
spring.datasource.password=
```

### 3. Build and Run the Application

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

#### Maven (if installed globally)

```bash
mvn clean install
mvn spring-boot:run
```

### 4. Access the Application

Open your browser at:

```
http://localhost:8080
```

You will be redirected to the login page. New users can register using the registration page.

### 5. Create an Admin (Optional)

To make a user an admin:

1. Register a normal account in the app.
2. In MySQL, update the user’s `role` to `Admin` in the `user` table:

```sql
UPDATE `user`
SET role = 'Admin'
WHERE user_name = 'yourUsername';
```

## Admin Setup

To make a user an admin:

1. Register a normal account in the app.
2. In MySQL, update the user’s `role` to `Admin` in the `user` table.

### MySQL Connection Issues

- Ensure MySQL is running
- Verify the database `csc340_project` exists
- Check the username and password in application.properties

### Build Failures

```bash
./mvnw clean install -U  # Update dependencies
```

## Development

To run with Spring Boot DevTools (auto-reload on file changes):

```bash
./mvnw spring-boot:run
```
