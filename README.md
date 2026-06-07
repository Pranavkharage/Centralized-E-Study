# Centralized E-Study Platform
### Spring Boot REST API | Upgraded from JDBC + Manual Mongo Driver
### 🏆 Government of India Copyright Holder

---

## Tech Stack
- **Java 17**
- **Spring Boot 3.2**
- **Spring Web** – REST API
- **Spring Data JPA + Hibernate** – MySQL user authentication
- **Spring Data MongoDB** – Study content storage (replaces manual mongodb-driver-sync)
- **MySQL** – User login/register
- **MongoDB** – Subject content (topics, theory, code, Q&A, diagrams, YouTube links)
- **Lombok** – Boilerplate removal
- **Maven** – pom.xml dependency management

---

## Project Structure
```
src/main/java/com/pranav/estudy/
├── EStudyApplication.java         ← @SpringBootApplication entry point
├── controller/
│   ├── AuthController.java        ← /api/auth (login/register via MySQL)
│   └── SubjectController.java     ← /api/subjects (content from MongoDB)
├── service/
│   ├── UserService.java           ← Auth business logic
│   └── SubjectService.java        ← MongoDB content logic
├── repository/
│   ├── UserRepository.java        ← JpaRepository → MySQL
│   └── SubjectRepository.java     ← MongoRepository → MongoDB
├── model/
│   ├── User.java                  ← @Entity (MySQL)
│   └── Subject.java               ← @Document (MongoDB)
└── dto/
    └── StudyDTO.java              ← Request/Response objects
```

---

## Setup

1. MySQL:
```sql
CREATE DATABASE estudy;
```

2. MongoDB: Make sure MongoDB is running on `localhost:27017`

3. Edit `application.properties` with your MySQL password

4. Run:
```bash
mvn spring-boot:run
```

---

## API Endpoints

### Auth (MySQL)
| Method | URL | Body |
|--------|-----|------|
| POST | /api/auth/register | `{"username":"pranav","password":"1234"}` |
| POST | /api/auth/login | `{"username":"pranav","password":"1234"}` |

### Study Content (MongoDB)
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/subjects/all | List all available subjects |
| GET | /api/subjects/{subjectName} | Full content for a subject (e.g. /api/subjects/OOP) |
| POST | /api/subjects/add | Add new subject with topics, Q&A, links |

---

## What changed from old version

| Old Code | New Spring Boot |
|---|---|
| `MongoClients.create("mongodb://localhost:27017")` | `spring.data.mongodb.uri` in application.properties |
| `db.getCollection("subjects")` | `@Document(collection="subjects")` on model class |
| `collection.find(new Document("subject", name)).first()` | `subjectRepository.findBySubject(name)` |
| Manual field access: `subject.getString("subject")` | Direct field access via `@Document` mapped class |
| MySQL JDBC PreparedStatement login | `UserRepository.findByUsernameAndPassword()` |
