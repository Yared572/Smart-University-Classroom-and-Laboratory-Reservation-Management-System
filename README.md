# DBU Smart University Reservation

JavaFX desktop project for Debre Berhan University. Requirements: JDK 21+, Maven, and MySQL 8+.

## Setup

Run all Maven commands below from the project root (the directory containing the top-level `pom.xml`). The project Maven settings store downloaded and installed artifacts in a `.m2` directory relative to the current directory; running Maven from a module folder uses a different local repository and may make sibling modules appear missing.

1. Execute `database/university_reservation.sql` in MySQL Workbench. It creates the schema and seeds the starter departments and rooms needed for registration and room search.
2. The local development connection defaults to MySQL `root` with password `1615` and database `dbu_reservations`. Override the password in the terminal for any other setup; do not commit real credentials:

```powershell
$env:DBU_RESERVATION_DB_PASSWORD = "your-MySQL-password"
```

The default connection is `jdbc:mysql://localhost:3306/dbu_reservations` with username `root`. Override `DBU_RESERVATION_DB_URL` or `DBU_RESERVATION_DB_USER` if your MySQL settings differ.
To check the configured connection from the project root, first compile the infrastructure module, then run the check:

```cmd
mvn -s .mvn\settings.xml -pl reservation-infrastructure -am compile
java -cp "reservation-infrastructure\target\classes;.m2\repository\com\mysql\mysql-connector-j\9.2.0\mysql-connector-j-9.2.0.jar" com.dbu.reservation.infrastructure.database.DatabaseConnectionCheck
```

A successful check prints the connected database name. Run `database/university_reservation.sql` to add a sample room named `DB Connection Test Room`; create an account in the app and search for that room to verify app access. You can run `check-db.cmd` to compile and perform the connection check in one step.
3. After changing source code, install all modules from the project root. The presentation module uses the installed artifacts for its sibling modules when run on its own:

```cmd
mvn -s .mvn\settings.xml -pl reservation-presentation -am install -DskipTests
```

Run this command from the project root. If your prompt is already in `reservation-presentation>`, run `cd ..` first.

4. Run the presentation module from the project root, using the same Maven settings and local repository:

```cmd
mvn -s .mvn\settings.xml -pl reservation-presentation javafx:run
```

Do not run plain `mvn javafx:run` from the project root: that runs against the parent POM, which has no JavaFX `mainClass`. You can also run `run.cmd` from the project root.

The app opens on the sign-in page. New students and lecturers can create accounts using a department ID from the `departments` table, then sign in with their university ID and password. Accounts are associated with their own reservations. Admin accounts must be provisioned by the database administrator; self-registration cannot create an admin account.

## Modules

- `reservation-domain`: domain models, repository interfaces, and overlap rule.
- `reservation-application`: authentication, availability, reservation, and approval use cases.
- `reservation-infrastructure`: JDBC/MySQL adapters and PBKDF2 password hashing.
- `reservation-presentation`: JavaFX entry point, UI, controllers, FXML, and CSS.

Availability excludes overlapping approved bookings. Reservation submission and approval also lock the room row and recheck conflicts in a transaction, preventing two simultaneous decisions from double-booking a room. Room CRUD, reports, notification inbox, cancellations, and admin approval screens are follow-on features; the current guest UI covers room search and reservation submission/history.
