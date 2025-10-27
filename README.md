# 🏥 HealthFinance Plus - Enterprise Microservices Platform

## 📊 Project Overview

A complete, production-ready healthcare management platform built with Spring Boot microservices architecture.

### **Architecture Highlights**
- **11 Microservices** (Eureka, API Gateway, 9 business services)
- **Service Discovery** with Netflix Eureka
- **API Gateway** with JWT authentication
- **9 PostgreSQL Databases** (one per service)
- **Redis Cache** for performance
- **RabbitMQ** for async messaging
- **OpenFeign** for inter-service communication
- **Circuit Breakers** with Resilience4j

---

## 🚀 Services

| Service | Port | Description |
|---------|------|-------------|
| Eureka Server | 8761 | Service Discovery |
| API Gateway | 8080 | Single Entry Point |
| User Service | 8081 | Authentication & Users |
| Appointment Service | 8082 | Scheduling & Bookings |
| Medical Records | 8083 | Patient Health Records |
| Prescription Service | 8084 | Prescriptions & Medications |
| Insurance Service | 8085 | Policies & Claims |
| Billing Service | 8086 | Invoices & Payments |
| Notification Service | 8087 | Email & SMS |
| Laboratory Service | 8088 | Lab Tests & Results |
| Pharmacy Service | 8089 | Medication Inventory |

---

## 🛠️ Technology Stack

- **Backend:** Spring Boot 3.2.0, Java 21
- **Service Discovery:** Netflix Eureka
- **API Gateway:** Spring Cloud Gateway
- **Database:** PostgreSQL 17
- **Cache:** Redis 7
- **Message Queue:** RabbitMQ 3
- **Security:** JWT, Spring Security
- **API Documentation:** Swagger/OpenAPI
- **Build Tool:** Maven

---

## 📋 Prerequisites

- Java 21
- Maven 3.9+
- Docker & Docker Compose
- PostgreSQL 17

---

## 🚀 Quick Start

### 1. Start Databases
```bash
docker-compose up -d
```

### 2. Start Eureka Server
```bash
cd eureka-server
mvn spring-boot:run
```

### 3. Start API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```

### 4. Start All Services
Start each service in separate terminals:
```bash
cd user-service && mvn spring-boot:run
cd appointment-service && mvn spring-boot:run
cd medical-records-service && mvn spring-boot:run
cd prescription-service && mvn spring-boot:run
cd insurance-service && mvn spring-boot:run
cd billing-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
cd laboratory-service && mvn spring-boot:run
cd pharmacy-service && mvn spring-boot:run
```

---

## 📚 API Documentation

- **Eureka Dashboard:** http://localhost:8761
- **API Gateway:** http://localhost:8080
- **Swagger UI (each service):** http://localhost:808X/swagger-ui.html

---

## 🔐 Authentication

All requests (except login/register) require JWT token:
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password"}'

# Use token in subsequent requests
curl -X GET http://localhost:8080/api/appointments \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 🎯 Key Features

### Inter-Service Communication
- Appointment → Notification (automatic reminders)
- Billing → Insurance (auto-claim submission)
- Prescription → Pharmacy (auto-dispensing)

### Performance Optimization
- Redis caching on frequently accessed data
- Async processing with @Async
- Message queuing with RabbitMQ

### Resilience
- Circuit breakers on all Feign clients
- Fallback mechanisms
- Retry logic

---

## 📊 Project Statistics

- **Total Services:** 11
- **Total Endpoints:** 140+
- **Lines of Code:** 15,000+
- **Databases:** 9 PostgreSQL + Redis
- **Message Broker:** RabbitMQ

---

## 👨‍💻 Developer

Built by [Your Name]

---

## 📝 License

MIT License
