# Job Portal Backend Application

This is a backend Job Portal application developed using **Spring Boot** with a **layered architecture**.  
The application provides secure REST APIs for user authentication and job portal operations and is fully Dockerized for easy setup.

---

## 🚀 Tech Stack

- **Java 21**
- **Spring Boot**
- **Spring Security + JWT**
- **Spring Data JPA (Hibernate)**
- **PostgreSQL**
- **Docker & Docker Compose**
- **Postman**

---

## 🧱 Architecture

The application follows a **layered architecture**:

- **Controller Layer** – REST API endpoints
- **Service Layer** – Business logic
- **Repository Layer** – Database access (JPA)
- **Security Layer** – JWT authentication & authorization
- **Exception Handling** – Centralized error handling

---

## 🔐 Authentication & Authorization

- JWT-based authentication
- Role-based authorization (e.g. CANDIDATE, RECRUITER)
- Secure APIs using Spring Security
- Token generated on login and required for protected endpoints

---

## 🐳 Running the Application (Recommended: Docker)

### Prerequisites
- Docker
- Docker Compose

---

### Steps to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/ukm3423/Job-Portal.git
   cd Job-Portal
