# TuneTribe

TuneTribe is a Spring Boot music app where users and artists share posts, follow each other, and manage with role-based dashboards.

## Spotify API
Artists can add a track to a post by entering a track title and artist name. It calls Spotify’s Search API (type=track, query: track:{title} artist:{artist}) and shows the Spotify track URL on the post.

## Features

- **Register and login**
- **User**: Post and follow
- **Artist**: Add a track to a post
- **Admin Dashboard**: Manage users, artists, and moderators
- **Mod Dashboard**: Manage users, artists
- **Community Guidelines**
- **Copyright**
- **Role-Based**: Different views and permissions for regular users, artists, moderators, and admins

## Tech Stack

- **Framework**: Spring Boot
- **Language**: Java
- **Build Tool**: Maven
- **Database**: MySQL
- **ORM**: JPA Hibernate
- **Thymeleaf**
- **Spring Security with BCrypt password encryption**
- **Dev Tools**: Spring Boot DevTools, Lombok

## Install:

- Java 21 or higher
- MySQL Server (I used XAMPP which includes phpMyAdmin)

## Setup

### 1. Database Setup (MySQL)

1. Start MySQL server
2. Create the database:

   ```sql
   CREATE DATABASE csc340_project;
   ```

3. Edit (src/main/resources/application.properties) to match your MySQL credentials (if needed):

```properties
spring.datasource.username=root
spring.datasource.password=
```

### 2.1 Spotify API

1. Create a Spotify app and get a Client ID and Client Secret:
   https://developer.spotify.com/dashboard
2. Add credentials to (src/main/resources/application.properties):

```properties
spotify.client-id=YOUR_SPOTIFY_CLIENT_ID
spotify.client-secret=YOUR_SPOTIFY_CLIENT_SECRET
```

### 3. Build and Run

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

### 4. Open browser at:

```
http://localhost:8080
```

### 5. Create an Admin

1. Register a normal account
2. In MySQL, update the user’s `role` to `Admin` in the `user` table.
