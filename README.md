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

### 1. Database Setup

1. Start your MySQL server
2. Create a new database for the project:

   ```sql
   CREATE DATABASE csc340_project;
   ```

3. The application will automatically create the necessary tables through Hibernate (configured with `spring.jpa.hibernate.ddl-auto=update`)

### 2. Configure Database Connection (Optional)

If your MySQL setup differs from the default, update [src/main/resources/application.properties](src/main/resources/application.properties):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/csc340_project?useSSL=false
spring.datasource.username=root
spring.datasource.password=
```

### 3. Build and Run the Application

#### Using Maven Wrapper (macOS/Linux):

```bash
./mvnw clean install
./mvnw spring-boot:run
```

#### Using Maven Wrapper (Windows):

```bash
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

#### Using Maven (if installed globally):

```bash
mvn clean install
mvn spring-boot:run
```

### 4. Access the Application

Once the application is running, open your web browser and navigate to:

```
http://localhost:8080
```

You will be redirected to the login page. New users can register through the registration page.

## Default Credentials

After initial setup, you may need to create an admin user through the database or application startup configuration. Refer to the User model and AdminService for details on user creation.

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
