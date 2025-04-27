# Vaadin Chat Application

This is a simple chat application built using:
- Java 23
- Spring Boot
- Vaadin Flow
- MySQL 8
- Docker & Docker Compose

---

## Features

- User authentication with hashed passwords.
- Admin-only access to user, message, and chat room management.
- Real-time message broadcasting using Vaadin Push.
- Responsive frontend using Vaadin components.
- Database persistence for users, chat rooms, and messages.
- Full Docker support for application and database.

---

## Default Admin Credentials

| Username | Password            |
|:---------|:--------------------|
| Admin    | SuperSecretPassword  |

**Note:**  
The Admin account is automatically created at first startup if not already present in the database.

---

## Changing the Default Admin Password

To change the default password:

1. Open:
```
src/main/java/online/robodoc/base/config/DataInitializer.java
```

2. Edit the password setting:

```java
admin.setPassword("NewSecurePasswordHere");
```

3. Save and rebuild:

```bash
docker-compose down
docker-compose up --build
```

**Important:**  
Changing an already-created Admin password requires manual database update or a future admin UI feature.

---

## Admin Panel Access

Once logged in as Admin, you can:

| Path            | Functionality                       |
|:----------------|:-------------------------------------|
| `/users`        | View, add, edit, delete users.        |
| `/messages`     | View and delete all messages.         |
| `/chatrooms`    | View, create, edit, delete chat rooms.|

---

## Requirements

- Java 23+
- Maven
- Docker
- Docker Compose

---

## Running the Application

1. Build and run the containers:

```bash
docker-compose up --build
```

This will:
- Start MySQL database on port 3306
- Build and start the Vaadin chat app on port 8080

2. Open your browser at:

```
http://localhost:8080
```

---

## Development Commands

- Rebuild application container only:

```bash
docker-compose up --build app
```

- Stop and remove containers:

```bash
docker-compose down
```

---

## Notes

- Database schema auto-updates on startup (`spring.jpa.hibernate.ddl-auto=update`).
- Frontend styles are in `src/main/frontend/themes/default/styles.css`.
- Real-time updates handled with Vaadin Push and a custom broadcaster.
- Database connection waits on startup for MySQL readiness.

---

# License

This project is distributed under the MIT License.