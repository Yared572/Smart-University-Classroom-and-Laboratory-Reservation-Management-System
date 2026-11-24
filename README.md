# Smart University Classroom and Laboratory Reservation Management System

**DBU Smart University Reservation**

A JavaFX desktop application developed for Debre Berhan University to manage classroom and laboratory reservations.

## 1. Requirements

Before running the application, install:

- JDK 21 or later
- Apache Maven
- MySQL 8 or later
- JavaFX (managed through Maven dependencies)
- MySQL Workbench (recommended)

## 2. Project Setup

Run all Maven commands from the project root directory (the directory containing the top-level `pom.xml`).

### Step 1: Configure the Database

1. Open MySQL Workbench.
2. Execute the SQL file:

   `database/university_reservation.sql`

3. This script creates the `dbu_reservations` database and inserts the starter departments and rooms required for registration and room searches.

### Step 2: Configure Database Connection

The default local development connection is:

- Database: `dbu_reservations`
- Username: `root`
- Password: `1615`
- Host: `localhost`
- Port: `3306`

For a different MySQL password, configure the environment variable in PowerShell:

```powershell
$env:DBU_RESERVATION_DB_PASSWORD = "your-MySQL-password"
```

The default JDBC connection URL is:

```text
jdbc:mysql://localhost:3306/dbu_reservations
```

You can override the following environment variables if your MySQL configuration differs:

- `DBU_RESERVATION_DB_URL`
- `DBU_RESERVATION_DB_USER`
- `DBU_RESERVATION_DB_PASSWORD`

**Security note:** Do not commit real database passwords or credentials to GitHub.

### Step 3: Check Database Connection

From the project root, compile the infrastructure module:

```cmd
mvn -s .mvn\settings.xml -pl reservation-infrastructure -am compile
```

Then check the database connection:

```cmd
java -cp "reservation-infrastructure\target\classes;.m2\repository\com\mysql\mysql-connector-j\9.2.0\mysql-connector-j-9.2.0.jar" com.dbu.reservation.infrastructure.database.DatabaseConnectionCheck
```

A successful check prints the connected database name.

You can also execute `check-db.cmd` from the project root to compile and perform the database connection check in one step.

### Step 4: Build the Project

After changing source code, install all modules from the project root:

```cmd
mvn -s .mvn\settings.xml -pl reservation-presentation -am install -DskipTests
```

This builds and installs the required project modules.

### Step 5: Run the Application

Run the JavaFX presentation module from the project root:

```cmd
mvn -s .mvn\settings.xml -pl reservation-presentation javafx:run
```

Alternatively, execute:

```cmd
run.cmd
```

**Important:** Run Maven commands from the project root. Running Maven from an individual module folder can cause sibling modules or dependencies to appear missing.

Do not run plain `mvn javafx:run` from the project root because the parent POM does not contain the JavaFX main class.

## 3. Application Login and Registration

The application opens on the sign-in page.

- Students can create accounts using their university ID and department ID.
- Lecturers can create accounts using their university ID and department ID.
- Users can sign in using their university ID and password.
- Each account is associated with its own reservations.
- Admin accounts must be created by the database administrator.
- Self-registration cannot create administrator accounts.

## 4. Project Architecture and Modules

The application uses a modular architecture:

### 4.1 reservation-domain

Contains:
- Domain models
- Repository interfaces
- Reservation overlap rules

### 4.2 reservation-application

Contains:
- Authentication services
- Availability checking
- Reservation management
- Approval use cases

### 4.3 reservation-infrastructure

Contains:
- JDBC database connectivity
- MySQL database adapters
- PBKDF2 password hashing

### 4.4 reservation-presentation

Contains:
- JavaFX application entry point
- User interface
- Controllers
- FXML files
- CSS styling

## 5. Reservation Management and Database Safety

The system checks room availability and prevents overlapping approved reservations.

Reservation submission and approval operations lock the room database row and recheck conflicts inside a transaction. This helps prevent simultaneous booking decisions from double-booking the same room.

## 6. Current Development Status

The current application includes:

- User registration
- User sign-in
- Room searching
- Reservation submission
- Reservation history
- Availability checking

The following features are planned as follow-on development:

- Classroom and laboratory management (CRUD)
- Administrative approval screens
- Reports and statistics
- Notification inbox
- Reservation cancellation

## 7. Project Goal

The main goal of this project is to provide a computerized reservation management system for Debre Berhan University.

It aims to reduce manual booking processes, improve classroom and laboratory utilization, prevent scheduling conflicts, and make reservation management easier for students, lecturers, and administrators.