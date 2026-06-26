# 💗 HeartSync — Date Planning Application

HeartSync is a web-based date planning application for couples. Users can create and manage date plans, build itineraries from a curated venue catalog, explore locations on an interactive map, and share plans with a partner. The application also lets users save venues to a wishlist, suggest new venues, and reflect on completed dates with personal notes and a star rating.

Built as a monolithic MVC application using **Java Spring Boot** on the backend and **Thymeleaf + Bootstrap** on the frontend.

---

## Features

- **User authentication** — Register and log in with role-based access (USER, ADMIN)
- **Date plans** — Create, manage, and delete date plans with a title, date, and description
- **Itinerary builder** — Add ordered stops with time slots, either from the venue catalog or as a custom named location
- **Partner invites** — Send an invite to another registered user, who can accept or reject it. Both users share access to the plan
- **Venue map** — Interactive Leaflet.js map with pre-loaded venues displayed as colored category pins. Click any pin to view details, save to wishlist, or add to a plan
- **Category filters** — Filter map pins by category (Park, Restaurant, Cafe, Cinema, etc.)
- **Wishlist** — Save venues from the map and add them to plans later
- **Venue suggestions** — Users can submit new venue suggestions with name, description, coordinates, and a photo. Admin reviews, edits, and approves or rejects each suggestion
- **Post-date reflections** — After marking a plan as completed, write a note and give it a star rating (1–5)
- **Admin panel** — Manage users, venues, and review pending venue suggestions

---

## Tech Stack

### Backend
- Java
- Spring Boot
- Spring MVC
- Spring Security (session-based authentication, BCrypt password hashing)
- Spring Data JPA / Hibernate
- Maven

### Database
- PostgreSQL — production database
- H2 — in-memory database for testing

### Frontend
- Thymeleaf — server-side templating
- Bootstrap 5 — responsive layout and components
- Leaflet.js — interactive venue map
- Vanilla JavaScript — map interactions, CSRF handling, dynamic modal

### Tools
- IntelliJ IDEA
- GitHub — version control
- Postman — API testing

---

## Prerequisites

Make sure you have the following installed:

- Java 17+
- Maven 3.8+
- PostgreSQL 14+

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/zeynalli-snani/HeartSync.git
cd heartsync
```

### 2. Set up the database

Open pgAdmin or psql and create the database:

```sql
CREATE DATABASE heartsync;
```

### 3. Configure the application

Copy the example properties file and fill in your credentials:

```bash
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

Open `application.properties` and update:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/heartsync
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
```

### 4. Run the application

```bash
mvn spring-boot:run
```

The app will start at `http://localhost:8080`.

Hibernate will auto-generate all tables on first run.

---

## Creating an Admin Account

After the app starts, run the following SQL to insert an admin user.

First generate a BCrypt hash of your chosen password by temporarily adding this to `HeartsyncApplication.java`:

```java
@Bean
public CommandLineRunner generateHash(PasswordEncoder passwordEncoder) {
    return args -> System.out.println("HASH: " + passwordEncoder.encode("yourpassword"));
}
```

Copy the printed hash, then run:

```sql
INSERT INTO users (username, email, full_name, password, role, is_active)
VALUES ('admin', 'admin@heartsync.com', 'Admin', '<paste_hash_here>', 'ADMIN', true);
```

Remove the `CommandLineRunner` bean afterwards.

---

## Project Structure

```
src/main/java/com/heartsync/
│
├── config/          # Spring Security config, Web MVC config
├── controller/      # HTTP request handlers (Auth, Plan, Venue, Admin)
├── exception/       # Custom exception classes
├── model/           # JPA entities (User, Plan, Venue, Stop, etc.)
├── repository/      # Spring Data JPA repositories
└── service/         # Business logic services

src/main/resources/
├── static/
│   ├── css/         # main.css
│   ├── js/          # map-explore.js, add-to-plan.js
│   └── uploads/     # User-uploaded venue photos
└── templates/
    ├── layout/      # Base layout (base.html)
    ├── auth/        # Login, register
    ├── plans/       # Plan list, detail, new, invites
    ├── venues/      # Map, wishlist, suggest
    └── admin/       # Dashboard, users, venues, suggestions
```

---

## Data Model

| Entity | Description |
|---|---|
| `User` | All users with role (USER, ADMIN) |
| `Plan` | A date plan owned by a user, optionally shared with a partner |
| `Stop` | An ordered itinerary entry linking a plan to a venue or custom name |
| `Venue` | A pre-loaded location with category, coordinates, and photo |
| `Wishlist` | A saved link between a user and a venue |
| `Reflection` | A post-date note and rating attached to a completed plan |
| `PlanInvite` | A partner invite with PENDING / ACCEPTED / REJECTED status |
| `VenueSuggestion` | A user-submitted venue awaiting admin review |

---

## Venue Categories

Each venue belongs to one of the following categories, each displayed with a distinct color on the map:

| Category | Color |
|---|---|
| Park | Green |
| Restaurant | Orange |
| Fast Food | Yellow |
| Cafe | Purple |
| Activity | Pink |
| Cinema | Blue |
| Museum | Teal |
| Rooftop | Rose |
| Bar | Violet |
| Other | Gray |

---

## File Uploads

Uploaded venue photos are stored in `src/main/resources/static/uploads/` and served at `/uploads/filename`. The upload directory is created automatically if it does not exist.

Supported formats: `.jpg`, `.jpeg`, `.png`, `.webp`, `.gif`

Maximum file size: 5MB (configurable in `application.properties`)

---

## Environment Variables

All sensitive configuration lives in `application.properties` which is excluded from version control via `.gitignore`. Use `application-example.properties` as the template.

| Property | Description |
|---|---|
| `spring.datasource.url` | PostgreSQL connection URL |
| `spring.datasource.username` | Database username |
| `spring.datasource.password` | Database password |
| `app.upload.dir` | Directory for uploaded images |

---

## Academic Context

This project was built as a final diploma project for the **Java Full Stack Development** program at **Qwasar Silicon Valley — Peerstack Campus**.