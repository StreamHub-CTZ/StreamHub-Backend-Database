<<<<<<< HEAD
# StreamHub Backend - Spring Boot Application

A comprehensive REST API backend for the StreamHub content management system built with Spring Boot, MySQL, and JPA/Hibernate.

## Project Structure

```
streamhub-backend/
├── src/
│   ├── main/
│   │   ├── java/com/streamhub/
│   │   │   ├── StreamHubApplication.java        # Main application entry point
│   │   │   ├── controller/
│   │   │   │   └── ContentController.java       # REST API endpoints
│   │   │   ├── entity/
│   │   │   │   └── Content.java                 # JPA Entity (database mapping)
│   │   │   ├── repository/
│   │   │   │   └── ContentRepository.java       # JPA Repository (database operations)
│   │   │   └── service/
│   │   │       └── ContentService.java          # Business logic layer
│   │   └── resources/
│   │       ├── application.properties           # Spring Boot configuration
│   │       └── schema.sql                       # Database schema script
│   └── test/
│       └── java/                                # Unit tests
└── pom.xml                                      # Maven dependencies and build config
```

## Features

- ✅ **Complete CRUD Operations** - Create, Read, Update, Delete content
- ✅ **Content Filtering** - Filter by type, genre, availability, premium status
- ✅ **Search Functionality** - Search content by title/keyword
- ✅ **Sorting & Pagination** - Top rated, newly added content
- ✅ **Analytics** - View count tracking, statistics
- ✅ **MySQL Integration** - JDBC connectivity with connection pooling
- ✅ **JPA/Hibernate** - Automatic table generation and ORM mapping
- ✅ **REST API** - Complete API endpoints with proper HTTP methods
- ✅ **Error Handling** - Comprehensive exception handling
- ✅ **Logging** - SLF4J with detailed log messages
- ✅ **CORS Support** - Cross-origin requests for frontend communication

## Prerequisites

### System Requirements

- **Java 17+** - JDK installed and configured
- **Maven 3.6+** - Build tool for compiling and running tests
- **MySQL 8.0+** - Database server
- **Git** - Version control (optional)

### Install Java 17

**Windows:**

```
# Using Windows Package Manager (if available)
winget install Oracle.JDK.17

# Or download from: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
```

**Verify Installation:**

```
java -version
javac -version
```

### Install Maven

**Windows:**

```
# Using Windows Package Manager
winget install Apache.Maven

# Or download from: https://maven.apache.org/download.cgi
# Add Maven bin folder to PATH environment variable
```

**Verify Installation:**

```
mvn -version
```

### Install MySQL

**Windows:**

```
# Using Windows Package Manager
winget install Oracle.MySQL

# Or download from: https://dev.mysql.com/downloads/mysql/
```

**Verify Installation:**

```
mysql --version
mysql -u root -p
```

## Setup Instructions

### 1. Create MySQL Database

```sql
-- Login to MySQL
mysql -u root -p

-- Run the schema script
source src/main/resources/schema.sql;

-- Verify tables created
USE streamhub_db;
SHOW TABLES;
DESCRIBE content;
```

**Or execute the SQL file directly:**

```bash
mysql -u root -p streamhub_db < src/main/resources/schema.sql
```

### 2. Configure Database Connection

Edit `src/main/resources/application.properties`:

```properties
# Update MySQL credentials (if different from defaults)
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

# Database URL (change if MySQL port is different)
spring.datasource.url=jdbc:mysql://localhost:3306/streamhub_db?useSSL=false&serverTimezone=UTC
```

### 3. Build the Project

```bash
# Navigate to project directory
cd backstreamhub

# Build using Maven
mvn clean install

# Or skip tests if you want faster build
mvn clean install -DskipTests
```

### 4. Run the Application

```bash
# Using Maven
mvn spring-boot:run

# Or using Java directly (after build)
java -jar target/streamhub-backend-1.0.0.jar
```

**Expected Console Output:**

```
=====================================
StreamHub Backend Started Successfully
API Base URL: http://localhost:8080/api
=====================================
```

## API Endpoints

### Base URL

```
http://localhost:8080/api/content
```

### Content Operations

| Method     | Endpoint                       | Description                             |
| ---------- | ------------------------------ | --------------------------------------- |
| **GET**    | `/content`                     | Get all content                         |
| **GET**    | `/content/{id}`                | Get content by ID                       |
| **GET**    | `/content/available`           | Get only available content              |
| **GET**    | `/content/premium`             | Get premium content                     |
| **GET**    | `/content/type/{contentType}`  | Get by type (MOVIE, MUSIC, EBOOK, etc.) |
| **GET**    | `/content/genre/{genre}`       | Get by genre                            |
| **GET**    | `/content/search?keyword=term` | Search by title/keyword                 |
| **GET**    | `/content/top-rated?limit=10`  | Get top rated content                   |
| **GET**    | `/content/new?days=7`          | Get newly added content                 |
| **GET**    | `/content/stats`               | Get statistics                          |
| **POST**   | `/content`                     | Create new content                      |
| **PUT**    | `/content/{id}`                | Update content                          |
| **DELETE** | `/content/{id}`                | Delete content                          |
| **POST**   | `/content/{id}/view`           | Increment view count                    |

### Example API Calls

#### Get All Content

```bash
curl http://localhost:8080/api/content
```

#### Get Content by ID

```bash
curl http://localhost:8080/api/content/1
```

#### Create New Content

```bash
curl -X POST http://localhost:8080/api/content \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Dune",
    "description": "Epic sci-fi film",
    "contentType": "MOVIE",
    "contentUrl": "/videos/dune.mp4",
    "genre": "Sci-Fi",
    "durationMinutes": 166,
    "rating": 8.0,
    "isPremium": true,
    "isAvailable": true
  }'
```

#### Search Content

```bash
curl "http://localhost:8080/api/content/search?keyword=Matrix"
```

#### Get Statistics

```bash
curl http://localhost:8080/api/content/stats
```

## Database Schema

### Content Table Columns

| Column             | Type         | Description                                                       |
| ------------------ | ------------ | ----------------------------------------------------------------- |
| `id`               | BIGINT (PK)  | Auto-incremented unique identifier                                |
| `title`            | VARCHAR(255) | Content title (unique, required)                                  |
| `description`      | LONGTEXT     | Detailed description                                              |
| `content_type`     | ENUM         | Type: MOVIE, MUSIC, EBOOK, SERIES, PODCAST, DOCUMENTARY, STAND_UP |
| `content_url`      | VARCHAR(500) | URL to content file (required)                                    |
| `duration_minutes` | INT          | Duration in minutes                                               |
| `genre`            | VARCHAR(100) | Genre classification                                              |
| `release_date`     | DATETIME     | Original release date                                             |
| `rating`           | DECIMAL(3,1) | Rating out of 10                                                  |
| `thumbnail_url`    | VARCHAR(500) | Poster/thumbnail image URL                                        |
| `language`         | VARCHAR(50)  | Language of content                                               |
| `director`         | VARCHAR(255) | Director name (for movies)                                        |
| `cast`             | LONGTEXT     | Cast members (JSON or comma-separated)                            |
| `is_available`     | BOOLEAN      | Whether content is active                                         |
| `is_premium`       | BOOLEAN      | Whether premium subscription required                             |
| `view_count`       | BIGINT       | Total views                                                       |
| `likes_count`      | BIGINT       | Total likes/favorites                                             |
| `created_at`       | DATETIME     | Creation timestamp (auto-set)                                     |
| `updated_at`       | DATETIME     | Last update timestamp (auto-updated)                              |

## Configuration Details

### application.properties Highlights

```properties
# JDBC Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/streamhub_db
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate/JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update  # Auto-create/update tables
spring.jpa.show-sql=true              # Show SQL in logs

# Connection Pool (HikariCP)
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5

# Logging
logging.level.com.streamhub=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

## Troubleshooting

### Issue: "Cannot connect to database"

**Solution:**

```bash
# Check MySQL is running
mysql -u root -p

# Verify database exists
SHOW DATABASES;

# Verify application.properties has correct credentials
```

### Issue: "Table doesn't exist"

**Solution:**

```bash
# Check if schema was created
USE streamhub_db;
SHOW TABLES;

# If not, manually run schema
mysql -u root -p streamhub_db < src/main/resources/schema.sql
```

### Issue: "Port 8080 already in use"

**Solution:**

```bash
# Change port in application.properties
server.port=8081

# Or kill the process using port 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Issue: "Build fails with Java version error"

**Solution:**

```bash
# Verify Java version
java -version

# Install Java 17 if needed
# Or change java.version in pom.xml to your installed version
```

## Development Tips

### Enable Hot Reload

Add spring-boot-devtools for automatic restart on code changes:

```bash
mvn spring-boot:run
```

### View Database

```bash
# MySQL CLI
mysql -u root -p streamhub_db

# Or use MySQL Workbench GUI
```

### View API Documentation

If you add Springdoc OpenAPI dependency:

```
http://localhost:8080/api/swagger-ui.html
http://localhost:8080/api/v3/api-docs
```

## Next Steps

1. ✅ Set up frontend to call this API from `http://localhost:8080/api/content`
2. ✅ Add authentication (JWT, Spring Security)
3. ✅ Add pagination and filtering
4. ✅ Add file upload functionality
5. ✅ Add caching (Redis)
6. ✅ Deploy to cloud (AWS, Heroku, etc.)

## Technology Stack

| Technology      | Version | Purpose            |
| --------------- | ------- | ------------------ |
| Spring Boot     | 3.2.0   | Framework          |
| Spring Data JPA | 3.2.0   | ORM                |
| Hibernate       | 6.x     | JPA Implementation |
| MySQL Connector | 8.0.33  | JDBC Driver        |
| Lombok          | Latest  | Reduce boilerplate |
| Maven           | 3.6+    | Build tool         |
| Java            | 17+     | Language           |

## License

This project is part of StreamHub and is proprietary.

## Support

For issues or questions:

1. Check the troubleshooting section above
2. Review application logs in console
3. Check MySQL error logs
4. Verify database configuration
=======
# StreamHub-Backend-Database
>>>>>>> 30f6ee7c5c217cd31b3e9c28cd65c3a67f446fc2
