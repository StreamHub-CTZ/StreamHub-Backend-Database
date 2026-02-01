# Quick Start Guide - Spring Boot Backend Setup

## 🚀 30-Second Setup

### Step 1: Prepare MySQL (First Time Only)

```bash
# Login to MySQL
mysql -u root -p

# Create database and tables
source C:\Users\2464323\gencws\sessionwp\backstreamhub\src\main\resources\schema.sql
```

### Step 2: Configure Database Connection

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### Step 3: Run Application

```bash
# From project directory
cd C:\Users\2464323\gencws\sessionwp\backstreamhub

# Build and run
mvn clean install
mvn spring-boot:run
```

✅ **Server runs at:** `http://localhost:8080/api`

---

## 📋 What Was Created

### 1. **Content.java** (Entity)

- JPA entity mapped to `content` table
- Fields: title, description, contentType, contentUrl, duration, genre, rating, etc.
- Auto-manages timestamps (createdAt, updatedAt)
- Validates data with annotations

### 2. **application.properties** (Configuration)

- MySQL JDBC connection string
- Hibernate DDL settings (auto-create tables)
- Connection pooling (HikariCP)
- Logging configuration
- CORS settings for frontend

### 3. **ContentRepository.java** (Data Layer)

- JPA Repository for database operations
- CRUD operations: save, findById, delete, etc.
- Custom queries: searchByTitle, findTopRated, etc.
- No need to write SQL - JPA generates it!

### 4. **ContentService.java** (Business Logic)

- Service layer with business rules
- Operations: createContent, updateContent, getContentByType, searchContent
- Error handling and validation
- Analytics (getStatistics, incrementViewCount)

### 5. **ContentController.java** (REST API)

- REST endpoints for CRUD operations
- Response mapping to HTTP status codes
- Error handlers
- Base URL: `/api/content`

### 6. **schema.sql** (Database)

- Creates `streamhub_db` database
- Creates `content` table with proper indexes
- Includes sample data (movies, music, ebooks)
- Useful test queries for reference

---

## 🔌 REST API Endpoints

### Get All Content

```bash
GET /api/content
```

**Response:** Array of all content items

### Get Content by ID

```bash
GET /api/content/1
```

**Response:** Single content object

### Get Movies Only

```bash
GET /api/content/type/MOVIE
```

### Get Premium Content

```bash
GET /api/content/premium
```

### Search by Title

```bash
GET /api/content/search?keyword=Matrix
```

### Get Top Rated (limit 10)

```bash
GET /api/content/top-rated?limit=10
```

### Create New Content

```bash
POST /api/content
Content-Type: application/json

{
  "title": "Avatar",
  "description": "Epic sci-fi film",
  "contentType": "MOVIE",
  "contentUrl": "/videos/avatar.mp4",
  "durationMinutes": 162,
  "genre": "Sci-Fi",
  "rating": 8.0,
  "language": "English",
  "director": "James Cameron",
  "isPremium": true,
  "isAvailable": true
}
```

### Update Content

```bash
PUT /api/content/1
Content-Type: application/json

{
  "title": "Avatar 2",
  "rating": 8.5
  ...
}
```

### Delete Content

```bash
DELETE /api/content/1
```

### Get Statistics

```bash
GET /api/content/stats
```

**Response:**

```json
{
  "totalContent": 10,
  "availableContent": 9,
  "premiumContent": 7
}
```

---

## 🛠️ Architecture Overview

```
Frontend (React/Vite)
        ↓
        ↓ HTTP Requests
        ↓
ContentController (/api/content/...)
        ↓
ContentService (Business Logic)
        ↓
ContentRepository (JPA)
        ↓
Hibernate (ORM)
        ↓
MySQL JDBC Driver
        ↓
MySQL Database (streamhub_db)
        ↓
content table
```

---

## 🔐 Database Connection Flow

1. **Application starts** → Reads `application.properties`
2. **HikariCP initializes** → Creates connection pool (10 max, 5 min idle)
3. **Hibernate creates session** → Connects to MySQL
4. **application.properties has `ddl-auto=update`** → Auto-creates/updates `content` table
5. **Ready to receive requests** → Processes CRUD operations via REST API

**JDBC Connection String:**

```
jdbc:mysql://localhost:3306/streamhub_db?useSSL=false&serverTimezone=UTC
```

---

## 📊 Database Schema

**Table: content**

| Column           | Type         | Notes                             |
| ---------------- | ------------ | --------------------------------- |
| id               | BIGINT       | Auto-increment PK                 |
| title            | VARCHAR(255) | Unique, required                  |
| description      | LONGTEXT     | Optional                          |
| content_type     | ENUM         | MOVIE, MUSIC, EBOOK, SERIES, etc. |
| content_url      | VARCHAR(500) | File URL (required)               |
| duration_minutes | INT          | Optional                          |
| genre            | VARCHAR(100) | Optional                          |
| release_date     | DATETIME     | Optional                          |
| rating           | DECIMAL(3,1) | Out of 10                         |
| language         | VARCHAR(50)  | Optional                          |
| director         | VARCHAR(255) | Optional                          |
| cast             | LONGTEXT     | Optional                          |
| is_available     | BOOLEAN      | Default TRUE                      |
| is_premium       | BOOLEAN      | Default FALSE                     |
| view_count       | BIGINT       | Analytics                         |
| likes_count      | BIGINT       | Analytics                         |
| created_at       | DATETIME     | Auto-set                          |
| updated_at       | DATETIME     | Auto-updated                      |

---

## 🧪 Test the API in Terminal

```bash
# Get all content
curl http://localhost:8080/api/content

# Get content by ID
curl http://localhost:8080/api/content/1

# Get movies only
curl "http://localhost:8080/api/content/type/MOVIE"

# Search for content
curl "http://localhost:8080/api/content/search?keyword=Matrix"

# Get statistics
curl http://localhost:8080/api/content/stats

# Create content (POST)
curl -X POST http://localhost:8080/api/content \
  -H "Content-Type: application/json" \
  -d '{"title":"Dune","contentType":"MOVIE","contentUrl":"/videos/dune.mp4"}'
```

---

## 🔧 Common Configuration Changes

### Change Database Port

```properties
# Default is 3306
spring.datasource.url=jdbc:mysql://localhost:3307/streamhub_db
```

### Change Server Port

```properties
# Default is 8080
server.port=8081
```

### Disable Auto Table Creation

```properties
# Default is 'update' - options: none, validate, update, create-drop
spring.jpa.hibernate.ddl-auto=validate
```

### Add Database Logging

```properties
# Enable SQL query logging
logging.level.org.hibernate.SQL=DEBUG
# Enable parameter logging
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

---

## 📝 File Locations

```
C:\Users\2464323\gencws\sessionwp\backstreamhub\
├── pom.xml                                          ← Maven dependencies
├── README.md                                        ← Full documentation
├── QUICKSTART.md                                    ← This file
├── src/main/
│   ├── java/com/streamhub/
│   │   ├── StreamHubApplication.java               ← Main application
│   │   ├── controller/ContentController.java       ← REST endpoints
│   │   ├── entity/Content.java                     ← JPA entity
│   │   ├── repository/ContentRepository.java       ← Data access
│   │   └── service/ContentService.java             ← Business logic
│   └── resources/
│       ├── application.properties                  ← Configuration
│       └── schema.sql                              ← Database script
└── target/                                         ← Compiled JAR (after build)
```

---

## ✅ Verification Checklist

- [ ] Java 17+ installed: `java -version`
- [ ] Maven 3.6+ installed: `mvn -version`
- [ ] MySQL 8.0+ running: `mysql -u root -p`
- [ ] Database created: `SHOW DATABASES;` shows `streamhub_db`
- [ ] Tables created: `USE streamhub_db; SHOW TABLES;`
- [ ] application.properties updated with correct credentials
- [ ] Application builds: `mvn clean install`
- [ ] Application starts: `mvn spring-boot:run`
- [ ] API responds: `curl http://localhost:8080/api/content`

---

## 🚨 Troubleshooting

| Issue                | Solution                                                |
| -------------------- | ------------------------------------------------------- |
| Cannot find database | Run `source schema.sql` in MySQL                        |
| Connection refused   | Check MySQL is running: `mysql -u root -p`              |
| Table doesn't exist  | Check `ddl-auto=update` in application.properties       |
| Build fails          | Verify Java 17: `java -version`                         |
| Port 8080 in use     | Change `server.port=8081` in properties or kill process |
| CORS errors          | Check `@CrossOrigin` annotation in controller           |

---

## 🎯 Next Steps

1. ✅ Start backend: `mvn spring-boot:run`
2. ✅ Test API endpoints with curl or Postman
3. ✅ Connect frontend to API (`http://localhost:8080/api`)
4. ✅ Add authentication (JWT)
5. ✅ Add more entities (User, Subscription, etc.)
6. ✅ Deploy to production

---

**Need help?** Refer to the full README.md for detailed documentation.
