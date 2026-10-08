# Event & Ticket Booking System

Advance Java practical project: **JSP + HTML + CSS**, **Java Servlets + JDBC**, **MySQL**, **JavaBeans**.

| Layer | Technology |
|---|---|
| Frontend | JSP, HTML, CSS |
| Backend | Java Servlets, JDBC, JavaBeans |
| Database | MySQL (`event_ticket_db`) |
| IDE | NetBeans |
| Server | GlassFish |

---

## 1. Create the database

1. Start **MySQL**.
2. Open MySQL Workbench or command line.
3. Run `database/event_ticket.sql`.
4. Open `src/main/java/com/eventticket/util/DBConnection.java` and set your MySQL username and password (default is `root` / `root`).

Demo logins after import:

- User: `demo@eventhub.com` / `demo123`
- Admin: `admin@eventhub.com` / `admin123`

---

## 2. Open in NetBeans (recommended)

### Option A — Maven (File → Open Project)

1. NetBeans → **File → Open Project** → select this folder (`EventTicketBooking`).
2. Right-click project → **Properties** → **Run**:
   - Server: **GlassFish**
   - Context Path: `/EventTicketBooking`
3. Right-click **Libraries** if needed and confirm MySQL connector is present (`mysql-connector-java` from Maven).
4. Right-click project → **Run**.

Browser URL:

`http://localhost:8080/EventTicketBooking/`

### Option B — Classic Java Web Application (copy files)

1. **File → New Project → Java Web → Web Application**.
2. Name: `EventTicketBooking`.
3. Server: **GlassFish Server**. Java EE 7.
4. Copy `src/main/java/com` into **Source Packages**.
5. Copy everything inside `src/main/webapp` into **Web Pages** (`WEB-INF`, `css`, `*.jsp`, `admin`).
6. Add **MySQL JDBC driver**:
   - Download `mysql-connector-j-8.0.33.jar`
   - Right-click **Libraries → Add JAR/Folder**
7. Run the project.

---

## 3. GlassFish notes

- Add GlassFish in NetBeans: **Tools → Servers → Add Server → GlassFish Server**.
- If the MySQL driver is missing at runtime, copy the same JAR into `GlassFish/glassfish/lib` or keep it in project Libraries (`WEB-INF/lib`).
- Default HTTP port is usually **8080**.

---

## 4. What the project covers (viva)

**Frontend**

- Home, event list/search, event details, register/login
- Book tickets, my bookings, cancel booking
- Admin dashboard, event form, booking list

**Backend (CRUD)**

| Module | Create | Read | Update | Delete |
|---|---|---|---|---|
| Users | Register | Login / profile in session | — | — |
| Events (Admin) | Add event | List / details | Edit event | Delete event |
| Bookings | Book tickets | My bookings / admin list | Cancel (status + restore seats) | Cancel |

**Extra (as per announcement)**

- JavaBeans: `User`, `Event`, `Booking`
- JDBC with `PreparedStatement`
- Transaction for booking and cancel (seats + booking row)
- Servlet Filter for login/admin protection
- SHA-256 passwords

---

## 5. Project structure

```
EventTicketBooking
├── database/event_ticket.sql
├── pom.xml
└── src/main
    ├── java/com/eventticket
    │   ├── bean        JavaBeans
    │   ├── dao         JDBC
    │   ├── servlet     Controllers
    │   ├── filter      Auth
    │   └── util        DB + hash
    └── webapp
        ├── css/style.css
        ├── *.jsp
        └── WEB-INF
```

---

## 6. Screens to show in practical

1. Home + event cards  
2. Register / Login  
3. Search events and book tickets  
4. My Bookings + cancel  
5. Admin: add / edit / delete event  
6. Admin: view all bookings  
