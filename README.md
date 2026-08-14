# 📚 Centralized E-Study Platform
### A Full-Stack Web Application with Hybrid MySQL + MongoDB Architecture

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen?style=flat-square&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![MongoDB](https://img.shields.io/badge/MongoDB-8.0-green?style=flat-square&logo=mongodb)
![Maven](https://img.shields.io/badge/Maven-3.9-red?style=flat-square&logo=apachemaven)
![Postman](https://img.shields.io/badge/Tested%20on-Postman-orange?style=flat-square&logo=postman)
![Copyright](https://img.shields.io/badge/Government%20of%20India-Copyright%20Holder-gold?style=flat-square)

---

## 📌 About the Project

**Centralized E-Study Platform** is a full-stack web application that gives students a single place to access complete interview preparation material — theory, code examples, Q&A, and YouTube OneShot videos — organized by subject and accessible through a clean browser interface.

### The Problem It Solves
Students preparing for technical interviews have to browse multiple websites, YouTube videos, and PDFs to find study material. This platform centralizes everything — OOP, DSA, OS, CN, DBMS — in one place with structured content accessible after a simple login.

### Hybrid Database Architecture
- **MySQL** — Secure user registration and login (structured relational data — user IDs, credentials)
- **MongoDB** — Scalable storage of all study content (unstructured nested data — topics, theory, code, Q&A, YouTube links)

> Each database is used where it fits best. User data is relational with fixed fields and foreign keys — MySQL is the right choice. Study content is deeply nested and schema-less — MongoDB's document model fits perfectly.

### 🏆 Government of India Copyright
This project holds an official **Government of India Copyright** as an original student-focused academic preparation portal.

---

## ✨ Features

- 🔐 **Student Login/Register** — secure authentication via MySQL
- 🚪 **Protected Routes** — subjects only accessible after login
- 📖 **5 Core Subjects** — OOP, DSA, OS, CN, DBMS
- 🧠 **50+ Topics** — theory, real code examples, diagrams
- ❓ **Interview Q&A** — most asked interview questions with detailed answers
- ▶️ **YouTube OneShot Videos** — best one-shot video for each subject
- 🌐 **Browser UI** — clean interface, no Postman needed
- 🌱 **Auto Data Seeder** — study content loads into MongoDB automatically on first run
- 🧱 **Layered Architecture** — Controller → Service → Repository → Database

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.2.0 |
| Web Layer | Spring Web (REST API + Static HTML) |
| Auth DB | Spring Data JPA + Hibernate + MySQL |
| Content DB | Spring Data MongoDB |
| Databases | MySQL 8.0 + MongoDB 8.0 |
| Frontend | HTML + CSS + JavaScript (served by Spring Boot) |
| Build Tool | Maven (pom.xml) |
| API Testing | Postman |

---

## 📁 Project Structure

```
centralized-estudy/
├── pom.xml
├── .gitignore
└── src/main/
    ├── java/com/pranav/estudy/
    │   ├── EStudyApplication.java            ← Entry point (@SpringBootApplication)
    │   ├── seeder/
    │   │   └── DataSeeder.java               ← Auto-loads all subject data into MongoDB
    │   ├── controller/
    │   │   ├── HomeController.java           ← Redirects / to login page
    │   │   ├── AuthController.java           ← /api/auth (register, login → MySQL)
    │   │   └── SubjectController.java        ← /api/subjects (content → MongoDB)
    │   ├── service/
    │   │   ├── UserService.java              ← Auth business logic
    │   │   └── SubjectService.java           ← Subject content logic
    │   ├── repository/
    │   │   ├── UserRepository.java           ← JpaRepository → MySQL
    │   │   └── SubjectRepository.java        ← MongoRepository → MongoDB
    │   ├── model/
    │   │   ├── User.java                     ← @Entity (MySQL users table)
    │   │   └── Subject.java                  ← @Document (MongoDB subjects collection)
    │   └── dto/
    │       └── StudyDTO.java                 ← Request / Response classes
    └── resources/
        ├── application.properties            ← MySQL + MongoDB config
        └── static/
            ├── login.html                    ← Register / Login page
            ├── dashboard.html                ← Subject selection page
            └── subject.html                  ← Topics + Q&A + YouTube page
```

---

## ⚙️ Setup & Run Instructions

### 🌐 Live Deployment

🔗 **Live URL:**  https://centralized-e-study.onrender.com

This project is deployed using:
- **Backend hosting:** Render (free tier)
- **MySQL database:** Aiven (free tier)
- **MongoDB database:** MongoDB Atlas (free tier)

### Run Locally (Optional)

If you want to run this project on your own machine instead of using the live deployment:

#### Prerequisites
- Java 21
- Maven
- A MySQL database (local or cloud)
- A MongoDB database (local or cloud)

#### Step 1 — Set Environment Variables

This project reads database credentials from environment variables instead of hardcoding them.

On Windows PowerShell:
```powershell
$env:SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/estudy"
$env:SPRING_DATASOURCE_USERNAME="root"
$env:SPRING_DATASOURCE_PASSWORD="your_password"
$env:SPRING_DATA_MONGODB_URI="mongodb://localhost:27017/estudy"
```

#### Step 2 — Create MySQL Database

```bash
mysql -u root -p
```
```sql
CREATE DATABASE estudy;
exit;
```

> The `users` table is created automatically by Hibernate on first run.

#### Step 3 — Make Sure MongoDB is Running

```bash
sc query MongoDB
```
Should show `STATE: 4 RUNNING`. If not:
```bash
net start MongoDB
```

#### Step 4 — Run

**IntelliJ:** Open `EStudyApplication.java` → click green ▶ button

**Terminal:**
```bash
mvn spring-boot:run
```

You will see in the console:

### Step 5 — Open in Browser

```
http://localhost:8080
```

---

## 🌐 Pages

| Page | URL | Description |
|---|---|---|
| Login / Register | `http://localhost:8080` | Student authentication |
| Dashboard | `http://localhost:8080/dashboard.html` | Subject selection |
| OOP | `http://localhost:8080/subject.html?name=OOP` | OOP topics + Q&A |
| DSA | `http://localhost:8080/subject.html?name=DSA` | DSA topics + Q&A |
| OS | `http://localhost:8080/subject.html?name=OS` | OS topics + Q&A |
| CN | `http://localhost:8080/subject.html?name=CN` | CN topics + Q&A |
| DBMS | `http://localhost:8080/subject.html?name=DBMS` | DBMS topics + Q&A |

---

## 📖 Subjects and Topics Covered

### ☕ OOP (13 Topics)
What is OOP, Encapsulation, Inheritance, Polymorphism, Abstraction, Method Overloading, Method Overriding, Interface, Abstract Class vs Interface, Constructor, this and super keyword, Static keyword, final keyword

### 🧩 DSA (11 Topics)
Arrays, Linked List, Stack, Queue, Binary Search Tree, Bubble Sort, Merge Sort, Quick Sort, Binary Search, Time & Space Complexity, Graph BFS/DFS

### 💻 OS (8 Topics)
What is OS, Process vs Thread, CPU Scheduling Algorithms, Deadlock, Memory Management & Paging, Semaphore & Mutex, Virtual Memory & Page Replacement, File System

### 🌐 CN (8 Topics)
OSI Model, TCP/IP Model, TCP vs UDP, IP Addressing & Subnetting, HTTP & HTTPS, DNS, Router vs Switch vs Hub, Protocols & Ports

### 🗄️ DBMS (8 Topics)
What is DBMS, ACID Properties, Normalization (1NF 2NF 3NF BCNF), SQL Commands (DDL DML DCL TCL), SQL Joins, Keys in DBMS, Indexing, Transactions & Isolation Levels

---

## 🔗 API Endpoints

Base URL: `http://localhost:8080`

### Auth (MySQL)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new student |
| POST | `/api/auth/login` | Login |

```json
POST /api/auth/register
{ "username": "pranav", "password": "1234" }
```

### Study Content (MongoDB)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/subjects/all` | List all subject names |
| GET | `/api/subjects/{name}` | Full content for a subject |
| POST | `/api/subjects/add` | Add new subject content |

```
GET /api/subjects/OOP
GET /api/subjects/DSA
GET /api/subjects/OS
GET /api/subjects/CN
GET /api/subjects/DBMS
```

---

## 🗄️ Database Design

### Why Two Databases?

| | MySQL | MongoDB |
|---|---|---|
| Stores | User credentials | Study content |
| Data type | Structured, relational | Nested, unstructured |
| Spring module | Spring Data JPA | Spring Data MongoDB |
| Model annotation | `@Entity` | `@Document` |
| Repository | `JpaRepository` | `MongoRepository` |

### MySQL Schema (auto-created by Hibernate)
```sql
CREATE TABLE users (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);
```

### MongoDB Document Structure
```json
{
  "_id": "ObjectId(...)",
  "subject": "OOP",
  "topics": [
    {
      "topicName": "Inheritance",
      "theory": "Detailed theory explanation...",
      "example": "class Dog extends Animal {}",
      "code": "class Animal { void eat(){} }\nclass Dog extends Animal { void bark(){} }",
      "diagrams": []
    }
  ],
  "questions": [
    {
      "question": "What are the 4 pillars of OOP?",
      "answer": "Encapsulation, Inheritance, Polymorphism, Abstraction"
    }
  ],
  "youtubeLinks": ["https://youtube.com/watch?v=..."]
}
```

---

## 🔑 Key Spring Annotations Used

| Annotation | Purpose |
|---|---|
| `@SpringBootApplication` | Entry point, auto-configuration |
| `@RestController` | REST controller, returns JSON |
| `@Controller` | MVC controller for page redirects |
| `@GetMapping` / `@PostMapping` | HTTP method mapping |
| `@PathVariable` | Reads `{name}` from URL |
| `@RequestBody` | Converts JSON to Java object |
| `@Service` | Business logic layer bean |
| `@Repository` | Data layer bean |
| `@Autowired` | Spring dependency injection |
| `@Entity` | Hibernate maps class to MySQL table |
| `@Document` | Spring Data maps class to MongoDB collection |
| `@Component` + `CommandLineRunner` | DataSeeder runs on app startup |

---

## 🚀 Future Improvements

- [ ] JWT Authentication with Spring Security
- [ ] BCrypt password hashing
- [ ] Search topics by keyword
- [ ] Admin panel to add/edit subjects
- [ ] Progress tracking per student
- [ ] Dark/Light mode toggle

---

## 👤 Author

**Pranav Kharage**
- GitHub: [@Pranavkharage](https://github.com/Pranavkharage)
- LinkedIn: [pranav-kharage](https://www.linkedin.com/in/pranav-kharage-824354258/)
- Email: pranavkharage21@gmail.com

---

> 🏆 This project holds a **Government of India Copyright** as an original student-focused academic preparation platform.
