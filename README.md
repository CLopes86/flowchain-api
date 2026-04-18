# FlowChain API

> Internal operations management system for multi-unit restaurant chains.

FlowChain replaces manual WhatsApp and Excel-based workflows with a structured, traceable, and role-based REST API — covering weekly supply orders, warehouse stock control, distribution planning, daily chicken stock tracking, urgent inter-unit transfers, and supplier payment registration.

---

## 🚀 Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 (LTS) | Programming language |
| Spring Boot | 3.5.13 | Main framework |
| Spring Security + JWT | Latest | Authentication & authorization |
| Spring Data JPA + Hibernate | 3.x | Database ORM |
| PostgreSQL | 16 | Relational database |
| Docker + Docker Compose | Latest | Containerization |
| Maven | 3.x | Build & dependency management |
| JUnit 5 + Mockito | Latest | Unit & integration testing |
| Swagger / OpenAPI | 2.8.8 | API documentation |
| Lombok | Latest | Boilerplate reduction |

---

## 🏗️ Architecture

The system follows a **Layered Architecture** with clear separation of concerns:

```
Client (Mobile App / Browser)
        ↓ HTTP Request + JWT
Spring Security Filter
        ↓ Authorized
Controller Layer     ← Receives HTTP requests, validates DTOs
        ↓
Service Layer        ← Business logic, validations, aggregations
        ↓
Repository Layer     ← Spring Data JPA, database communication
        ↓
PostgreSQL Database
```

---

## 👥 System Roles

| Role | Description |
|---|---|
| **ADMIN** | Full access — consolidated lists, all units, user management |
| **WAREHOUSE** | Stock management, distributions, delivery confirmation |
| **UNIT** | Weekly orders, daily chicken stock, payment registration |
| **COURIER** | Urgent transfers between units |

---

## 🏪 Operational Units

The system manages 7 operational units:

| Unit | Type | Special Role |
|---|---|---|
| **Alpha** | Restaurant | Portioning center — bacalhau & entrecosto |
| **Beta** | Restaurant + Take Away | Operations hub — soups, rice, prepared dishes + courier base |
| **Gamma** | Restaurant + Take Away | — |
| **Delta** | Restaurant + Take Away | — |
| **Epsilon** | Take Away | — |
| **Zeta** | Take Away | — |
| **Eta** | Take Away | — |

---

## 📅 Weekly Operational Calendar

| Day | Operation | Responsible |
|---|---|---|
| Monday | Charcoal distribution | Warehouse |
| Tuesday | Warehouse products distribution | Warehouse |
| Thursday | External supplier purchases + distribution | Admin + Warehouse |
| Friday | Meats & prepared dishes distribution | Warehouse |
| Daily | Chicken stock control | Each Unit |
| Any time | Urgent transfers between units | Courier |

---

## 🗺️ Project Roadmap

### ✅ Phase 1 — Setup & Foundations *(Completed)*
- [x] Requirements analysis
- [x] Data model design (12 entities)
- [x] System architecture diagram
- [x] Spring Boot 3.x project setup
- [x] GitHub repository initialization
- [x] Docker Compose — PostgreSQL + pgAdmin
- [x] Project folder structure
- [x] Unit entity with JPA mappings
- [x] UnitRepository, UnitService, UnitController
- [x] First REST endpoint — GET /api/units
- [x] Swagger UI integration

### 🔄 Phase 2 — Authentication *(In Progress)*
- [ ] User registration & login
- [ ] JWT token generation and validation
- [ ] Role-based access control (ADMIN, WAREHOUSE, UNIT, COURIER)
- [ ] Security filter configuration

### ⏳ Phase 3 — Order Management
- [ ] Weekly order submission per unit (Supplier, Warehouse, Beira Sumos)
- [ ] Prepared dishes order (soup, rice, bacalhau, etc.)
- [ ] Automatic consolidated order list per supplier
- [ ] Order status tracking (PENDING → CONFIRMED → DELIVERED)

### ⏳ Phase 4 — Warehouse & Stock Management
- [ ] Warehouse stock entries and exits
- [ ] Weekly distribution planning per unit
- [ ] Portioning registration (Alpha & Beta units)
- [ ] Delivery confirmation per unit

### ⏳ Phase 5 — Daily Operations
- [ ] Daily chicken stock control (per unit)
- [ ] Urgent product transfers between units
- [ ] Supplier payment registration with invoice photo
- [ ] Operations history and audit trail

### ⏳ Phase 6 — Testing & Documentation
- [ ] Unit tests with JUnit 5 + Mockito
- [ ] Integration tests
- [ ] Full Swagger / OpenAPI documentation
- [ ] API usage examples

### ⏳ Phase 7 — Security & Quality
- [ ] Global exception handling
- [ ] Input validation on all endpoints
- [ ] System logging
- [ ] Security review

---

## 📁 Project Structure

```
flowchain-api/
├── docs/                          # Project documentation (PDF)
│   ├── FlowChain_Analise_Requisitos.pdf
│   ├── FlowChain_Modelo_Dados.pdf
│   ├── FlowChain_Arquitetura.pdf
│   └── FlowChain_UseCases.html
├── src/
│   ├── main/
│   │   ├── java/com/flowchain/api/
│   │   │   ├── controller/        # REST Controllers
│   │   │   ├── service/           # Business logic
│   │   │   ├── repository/        # Spring Data JPA repositories
│   │   │   ├── entity/            # JPA entities (database tables)
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   ├── security/          # JWT & Spring Security config
│   │   │   └── exception/         # Global exception handling
│   │   └── resources/
│   │       └── application.yml    # App configuration
│   └── test/                      # Unit & integration tests
├── docker-compose.yml             # PostgreSQL + pgAdmin containers
├── pom.xml                        # Maven dependencies
└── README.md
```

---

## 🐳 Running Locally

### Prerequisites
- Java 21
- Docker + Docker Compose
- Maven 3.x

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/CLopes86/flowchain-api.git
cd flowchain-api
```

**2. Start the database**
```bash
docker compose up -d
```

**3. Run the application**
```bash
./mvnw spring-boot:run
```

**4. Access Swagger UI**
```
http://localhost:8080/swagger-ui/index.html
```

---

## 📋 Main API Endpoints *(Work in Progress)*

| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/api/units` | All | List all units |
| GET | `/api/units/{id}` | All | Get unit by ID |
| POST | `/api/units` | ADMIN | Create new unit |
| DELETE | `/api/units/{id}` | ADMIN | Delete unit |
| POST | `/api/auth/login` | All | User login |
| POST | `/api/orders` | UNIT | Submit weekly order |
| GET | `/api/orders/consolidated` | ADMIN, WAREHOUSE | Consolidated order list |
| POST | `/api/warehouse/stock` | WAREHOUSE | Register stock movement |
| POST | `/api/distributions` | WAREHOUSE | Create distribution |
| PUT | `/api/distributions/{id}/confirm` | WAREHOUSE | Confirm delivery |
| POST | `/api/chicken-stock` | UNIT | Daily chicken stock |
| POST | `/api/transfers` | COURIER | Register urgent transfer |
| POST | `/api/payments` | UNIT | Register supplier payment |

---

## 📄 Documentation

Full project documentation is available in the `/docs` folder:

- 📋 [Requirements Analysis](docs/FlowChain_Analise_Requisitos.pdf)
- 🗄️ [Data Model](docs/FlowChain_Modelo_Dados.pdf)
- 🏗️ [Architecture Diagram](docs/FlowChain_Arquitetura.pdf)
- 📊 [Use Case Diagram](docs/FlowChain_UseCases.html)

---

## 👨‍💻 Author

<img src="https://github.com/CLopes86.png" alt="Cesaltino Lopes" width="100" style="border-radius: 50%"/>

**Cesaltino Lopes**
Computer Science Student @ IPCB — Castelo Branco, Portugal

[![GitHub](https://img.shields.io/badge/GitHub-CLopes86-181717?style=flat&logo=github)](https://github.com/CLopes86)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Cesaltino%20Lopes-0077B5?style=flat&logo=linkedin)](https://www.linkedin.com/in/cesaltino-lopes-55274b176/)
[![Email](https://img.shields.io/badge/Email-c.lopes46cv@gmail.com-D14836?style=flat&logo=gmail)](mailto:c.lopes46cv@gmail.com)

---

*This project is under active development. New features are added progressively following the roadmap above.*