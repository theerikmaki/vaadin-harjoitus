# Vaadin Chat Application

This is a simple chat application built using:
- Java 23
- Spring Boot
- Vaadin Flow
- MySQL 8
- Docker & Docker Compose

## Features

- User authentication with hashed passwords.
- Admin-only access to user, message, and chat room management.
- Real-time message broadcasting using Vaadin Push.
- Responsive frontend using Vaadin components.
- Database persistence for users, chat rooms, and messages.
- Full Docker support for application and database.

## Default Admin Credentials

| Username | Password            |
|:---------|:--------------------|
| Admin    | SuperSecretPassword  |

**Admin** account is automatically created on first application startup if it does not already exist.

## Changing the Default Admin Password

To change the default password:

1. Open the project file:

```
src/main/java/online/robodoc/base/config/DatabaseInitializer.java
```

2. Locate the section that creates the Admin user.

3. Modify the password field:

```
admin.setPassword(passwordEncoder.encode("NewSecurePasswordHere"));
```

4. Save the file and rebuild the application:

```bash
docker-compose down
docker-compose up --build
```

>Note: Changing the password later requires manual update of the database or creating a user interface for password management.

## Requirements

- Java 23+
- Maven
- Docker
- Docker Compose

## Running the Application

1. Build and run the containers:

```bash
docker-compose up --build
```

This will:
- Start MySQL database on port 3306
- Build and start the Vaadin chat app on port 8080

2. Access the application

Open your browser and navigate to:
```
http://localhost:8080
```

## Notes

- Database schema is updated automatically on startup (`spring.jpa.hibernate.ddl-auto=update`).

- All frontend styles are located under `src/main/frontend/themes/default/styles.css`.

- Real-time updates use Vaadin Push and a custom broadcaster.

## Development Commands

To rebuild only:
```bash
docker-compose up --build app
```

To tear down:
```bash
docker-compose down
```