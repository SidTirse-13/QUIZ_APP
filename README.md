# 🎯 Quiz App (Java 17 + MySQL + JDBC)

A console-based **Online Quiz Application** built with plain **Java (17)**, **MySQL**, and **JDBC**.  
This repo provides user registration/login (passwords hashed with BCrypt), admin functionality to create/manage quizzes, and user flows to attempt quizzes and store results.


## Overview
This application demonstrates a simple, modular approach to building a quiz system using JDBC for database interactions. The code is organized as services (`UserService`, `QuizService`, `AdminService`) and a console UI (`Main.java`). All persistent data is stored in a MySQL database.


## Features
- ✅ User registration and login (BCrypt hashed passwords)  
- ✅ Role-based behavior: `ADMIN` and `USER`  
- ✅ Admin can create quizzes and add questions  
- ✅ Users can list quizzes and attempt them  
- ✅ Results are stored in the database for later review


## Prerequisites
- Java 17 (JDK 17) installed and `JAVA_HOME` configured  
- Maven 3.6+  
- MySQL 8+ running locally (or reachable remotely)  
- Terminal or IDE (IntelliJ/Eclipse/VSCode)


## Project structure
quiz-app/
│── src/main/java/com/quizapp/
│   ├── DBConnection.java   # Database connection
│   ├── UserService.java    # Handles user login & registration
│   ├── QuizService.java    # Handles quiz logic
│   ├── AdminService.java   # Handles admin actions
│   ├── Main.java           # Entry point (menu-driven application)
│
│── pom.xml                 # Maven dependencies & build


## Database schema & sample data

Run these SQL commands in your MySQL client to create the database and tables used by the application.

#SQL
1) Create database
CREATE DATABASE quizdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE quizdb;

2) Users
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,     -- BCrypt hash (~60 chars)
    role ENUM('ADMIN','USER') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

3) Quizzes
CREATE TABLE quizzes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

4) Questions (multiple choice stored in columns for simplicity)
CREATE TABLE questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    quiz_id INT NOT NULL,
    question_text TEXT NOT NULL,
    option_a VARCHAR(1024),
    option_b VARCHAR(1024),
    option_c VARCHAR(1024),
    option_d VARCHAR(1024),
    correct_option CHAR(1),  -- 'A', 'B', 'C', 'D'
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

5) Results (attempts)
CREATE TABLE results (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    quiz_id INT,
    score INT,
    total INT,
    correct INT,
    time_taken_seconds INT,
    taken_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE SET NULL
);


## 🛠 Tech Stack
- **Java 17**
- **MySQL 8+**
- **JDBC (Java Database Connectivity)**
- **Maven** (dependency management & build tool)
- **BCrypt** for password hashing


## ⚙️ Installation & Setup

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-username/quiz-app.git
cd quiz-app
