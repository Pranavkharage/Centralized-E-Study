# 📚 Centralized E-Study Platform

A terminal-based Java application that gives students a single place to access study material for their subjects. After logging in, the student picks a subject and the app fetches all content (theory, code examples, diagrams, Q&A, YouTube links) from a database and displays it in a neatly formatted browser page — automatically.

> 🏆 **Government of India Copyright Holder** — This project is officially copyrighted.

---

## 🎯 What This Project Does

Most students waste time searching for study material across multiple websites. This platform solves that by centralizing everything in one place:

- You **register / log in** via the terminal
- You **choose a subject** (OOP, DBMS, SPOS, CNS, Java)
- The app **fetches all study content** from MongoDB
- It **auto-opens a browser tab** with theory, code examples, diagrams, Q&A, and YouTube links — all nicely formatted

---

## 🖥️ How It Looks (Flow)

```
----- Centralized E-Study Platform -----
1. Login
2. Register
Choose option: 1

Enter username: pranav
Enter password: 1234

✅ Login successful

Subjects available:
1. OOP
2. DBMS
3. SPOS
4. CNS
5. JAVA

Enter subject number: 2
```
👉 A browser tab opens automatically with full DBMS study content.

---

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────┐
│           Terminal (User Interface)          │
└────────────────────┬────────────────────────┘
                     │
         ┌───────────▼───────────┐
         │    Centralized.java   │
         │     (Main Logic)      │
         └──────┬────────────────┘
                │
     ┌──────────┴──────────┐
     │                     │
┌────▼─────┐         ┌─────▼──────┐
│  MySQL   │         │  MongoDB   │
│          │         │            │
│ Users    │         │ subjects   │
│ table    │         │ collection │
│          │         │            │
│ Login &  │         │ Study      │
│ Register │         │ Content    │
└──────────┘         └────────────┘
                            │
                     ┌──────▼──────┐
                     │   Browser   │
                     │  (HTML tab) │
                     └─────────────┘
```

**Why two databases?**
- **MySQL** → structured data (user accounts, login) — best for relational, fixed-schema data
- **MongoDB** → unstructured content (theory, code, links, Q&A) — best for flexible, document-style data

This is called a **Hybrid Database Architecture** and is a real-world design pattern used in production systems.

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | JDK 8+ | Core application language |
| MySQL | 8.x | User authentication & storage |
| MongoDB | 4.x | Subject content storage |
| JDBC | — | Java ↔ MySQL connection |
| MongoDB Java Driver | 4.11.1 | Java ↔ MongoDB connection |
| HTML + Java Desktop API | — | Auto-open browser with formatted content |

---

## 📁 Project Structure

```
Centralized-E-Study/
│
├── Centralized.java        ← Main source code (all logic here)
├── authentication.sql      ← MySQL database setup script
├── code.txt                ← Run commands reference
│
└── lib/                    ← Dependency JARs (place all JARs here)
    ├── mysql-connector-j-9.5.0.jar
    ├── mongodb-driver-sync-4.11.1.jar
    ├── mongodb-driver-core-4.11.1.jar
    ├── bson-4.11.1.jar
    └── slf4j-simple-2.0.9.jar
```

---

## ⚙️ Setup & Installation

### Prerequisites
Make sure you have these installed on your system:
- [Java JDK 8 or above](https://www.oracle.com/java/technologies/downloads/)
- [MySQL Server](https://dev.mysql.com/downloads/mysql/)
- [MongoDB Community Server](https://www.mongodb.com/try/download/community)

---

### Step 1 — Set up MySQL Database

Open MySQL and run the `authentication.sql` file:

```sql
CREATE DATABASE estudy;
USE estudy;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
);
```

Or simply run the provided file:
```bash
mysql -u root -p < authentication.sql
```

---

### Step 2 — Set up MongoDB

Start your MongoDB server locally. The app connects to:
```
mongodb://localhost:27017
Database: estudy
Collection: subjects
```

Each subject document in MongoDB should follow this structure:

```json
{
  "subject": "OOP",
  "topics": [
    {
      "topicName": "Classes and Objects",
      "theory": "A class is a blueprint for objects...",
      "example": "Car is a class, my car is an object",
      "code": "class Car { String brand; }",
      "diagrams": ["https://link-to-diagram.com"]
    }
  ],
  "questions": [
    {
      "question": "What is encapsulation?",
      "answer": "Wrapping data and methods together in a class."
    }
  ],
  "youtubeLinks": ["https://youtube.com/watch?v=example"]
}
```

Insert a document like this for each subject: OOP, DBMS, SPOS, CNS, JAVA.

---

### Step 3 — Update Database Credentials

Open `Centralized.java` and update these lines with your own MySQL credentials:

```java
static final String MYSQL_USER = "root";        // your MySQL username
static final String MYSQL_PASS = "yourpassword"; // your MySQL password
```

---

### Step 4 — Compile & Run

Open terminal/command prompt in the project folder and run:

**Compile:**
```bash
javac -cp ".;*" Centralized.java
```

**Run:**
```bash
java -cp ".;*" Centralized
```

> On **Mac/Linux**, replace `;` with `:` in the classpath:
> ```bash
> javac -cp ".:*" Centralized.java
> java -cp ".:*" Centralized
> ```

---

## ✨ Features

- ✅ User Registration with duplicate username check
- ✅ Secure Login with PreparedStatement (SQL injection safe)
- ✅ Subject selection menu (OOP, DBMS, SPOS, CNS, Java)
- ✅ Fetches study content dynamically from MongoDB
- ✅ Auto-generates a formatted HTML page
- ✅ Opens the page directly in Chrome browser
- ✅ Content includes: Theory, Code Examples, Diagrams, Q&A, YouTube Links
- ✅ Hybrid database design (MySQL + MongoDB)

---

## 🔑 Default Test Credentials

You can use these accounts to test the app (already inserted by `authentication.sql`):

| Username | Password |
|---|---|
| user1 | password1 |
| user2 | password2 |
| pranav | 1234 |

---

## 🚀 Future Improvements

- [ ] Add password hashing (BCrypt) for secure storage
- [ ] Add admin panel to insert/update subject content
- [ ] Add more subjects
- [ ] Cross-platform browser opening (currently Chrome on Windows only)
- [ ] GUI version using Java Swing or JavaFX

---

## 👨‍💻 Author

**Pranav Kharage**
- GitHub: [@Pranavkharage](https://github.com/Pranavkharage)
- LinkedIn: [pranav-kharage](https://www.linkedin.com/in/pranav-kharage-824354258/)

---

## 📄 License & Copyright

This project is protected under **Government of India Copyright**.
© Pranav Kharage. All rights reserved.
