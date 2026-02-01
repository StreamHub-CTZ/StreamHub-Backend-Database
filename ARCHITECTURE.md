# Spring Boot Backend - Architecture & Code Flow

## 🏗️ Complete Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND (React/Vite)                      │
│              http://localhost:5173                            │
└────────────────────┬────────────────────────────────────────┘
                     │ HTTP Requests (JSON)
                     ↓
┌─────────────────────────────────────────────────────────────┐
│         SPRING BOOT REST API LAYER                            │
│         http://localhost:8080/api                            │
├─────────────────────────────────────────────────────────────┤
│  ContentController (@RestController)                         │
│  ├── GET    /content              → getAllContent()          │
│  ├── GET    /content/{id}         → getContentById()         │
│  ├── GET    /content/available    → getAvailableContent()    │
│  ├── GET    /content/premium      → getPremiumContent()      │
│  ├── GET    /content/type/{type}  → getContentByType()       │
│  ├── GET    /content/genre/{genre}→ getContentByGenre()      │
│  ├── GET    /content/search       → searchContent()          │
│  ├── GET    /content/top-rated    → getTopRatedContent()     │
│  ├── GET    /content/stats        → getStatistics()          │
│  ├── POST   /content              → createContent()          │
│  ├── PUT    /content/{id}         → updateContent()          │
│  ├── DELETE /content/{id}         → deleteContent()          │
│  └── POST   /content/{id}/view    → incrementViewCount()     │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ↓
┌─────────────────────────────────────────────────────────────┐
│          BUSINESS LOGIC / SERVICE LAYER                       │
├─────────────────────────────────────────────────────────────┤
│  ContentService (@Service)                                   │
│  ├── createContent(Content)                                  │
│  ├── getContentById(Long)                                    │
│  ├── getAllContent()                                         │
│  ├── getAvailableContent()                                   │
│  ├── getContentByType(ContentType)                           │
│  ├── getContentByGenre(String)                               │
│  ├── getPremiumContent()                                     │
│  ├── searchContent(String keyword)                           │
│  ├── getTopRatedContent(int limit)                           │
│  ├── updateContent(Long id, Content)                         │
│  ├── deleteContent(Long id)                                  │
│  ├── incrementViewCount(Long id)                             │
│  └── getStatistics()                                         │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ↓
┌─────────────────────────────────────────────────────────────┐
│          DATA ACCESS / REPOSITORY LAYER                       │
├─────────────────────────────────────────────────────────────┤
│  ContentRepository (@Repository)                             │
│  Extends JpaRepository<Content, Long>                        │
│                                                               │
│  Built-in Methods:                                           │
│  ├── save(Content)                                           │
│  ├── findById(Long)                                          │
│  ├── findAll()                                               │
│  ├── deleteById(Long)                                        │
│                                                               │
│  Custom Finder Methods:                                      │
│  ├── findByTitle(String)                                     │
│  ├── findByContentType(ContentType)                          │
│  ├── findByIsAvailableTrue()                                 │
│  ├── findByIsPremiumTrue()                                   │
│  ├── findByGenre(String)                                     │
│  ├── findByCreatedAtAfter(LocalDateTime)                     │
│  ├── searchByTitle(String keyword)                           │
│  ├── findTopRatedContent(Boolean, int)                       │
│  ├── findByMultipleCriteria(...)                             │
│  ├── countByIsAvailableTrue()                                │
│  └── existsByTitle(String)                                   │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ↓
┌─────────────────────────────────────────────────────────────┐
│              ORM / PERSISTENCE LAYER                          │
├─────────────────────────────────────────────────────────────┤
│  Hibernate (JPA Implementation)                              │
│  ├── Entity Mapping: Content.java ↔ content table            │
│  ├── Auto-generates SQL queries from repository methods      │
│  ├── Session Management                                      │
│  ├── Transaction Management (@Transactional)                │
│  └── Lazy/Eager Loading Configuration                       │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ↓
┌─────────────────────────────────────────────────────────────┐
│              JDBC / DRIVER LAYER                              │
├─────────────────────────────────────────────────────────────┤
│  MySQL JDBC Driver (mysql-connector-java 8.0.33)            │
│  ├── Connection Management                                   │
│  ├── SQL Execution                                           │
│  ├── Result Set Mapping                                      │
│  └── Connection Pooling (HikariCP - 10 max, 5 min)         │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ↓
┌─────────────────────────────────────────────────────────────┐
│                  MYSQL DATABASE                               │
│               localhost:3306                                 │
├─────────────────────────────────────────────────────────────┤
│  streamhub_db (Database)                                     │
│  └── content (Table)                                         │
│      ├── id (BIGINT PK, AUTO_INCREMENT)                      │
│      ├── title (VARCHAR 255, UNIQUE)                         │
│      ├── description (LONGTEXT)                              │
│      ├── content_type (ENUM)                                 │
│      ├── content_url (VARCHAR 500)                           │
│      ├── duration_minutes (INT)                              │
│      ├── genre (VARCHAR 100)                                 │
│      ├── release_date (DATETIME)                             │
│      ├── rating (DECIMAL 3,1)                                │
│      ├── thumbnail_url (VARCHAR 500)                         │
│      ├── language (VARCHAR 50)                               │
│      ├── director (VARCHAR 255)                              │
│      ├── cast (LONGTEXT)                                     │
│      ├── is_available (BOOLEAN)                              │
│      ├── is_premium (BOOLEAN)                                │
│      ├── view_count (BIGINT)                                 │
│      ├── likes_count (BIGINT)                                │
│      ├── created_at (DATETIME, NOT NULL)                     │
│      ├── updated_at (DATETIME)                               │
│      └── INDEXES: title, content_type, created_at, genre     │
│                                                               │
│  Indexes (for faster queries):                               │
│  ├── idx_title                                               │
│  ├── idx_content_type                                        │
│  ├── idx_created_at                                          │
│  └── idx_genre                                               │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔄 Request/Response Flow Example

### Scenario: Get All Content

```
1. Frontend Request:
   GET http://localhost:8080/api/content

2. Spring Receives Request:
   ContentController.getAllContent()

3. Calls Service Layer:
   contentService.getAllContent()

4. Service Calls Repository:
   contentRepository.findAll()

5. Hibernate generates SQL:
   SELECT * FROM content

6. JDBC executes query:
   Connects to MySQL via jdbc:mysql://localhost:3306/streamhub_db

7. MySQL returns rows:
   [
     {id: 1, title: "Matrix", ...},
     {id: 2, title: "Inception", ...},
     ...
   ]

8. Hibernate maps to Objects:
   List<Content> objects

9. Spring returns JSON Response:
   HTTP 200 OK
   [
     {"id": 1, "title": "Matrix", ...},
     {"id": 2, "title": "Inception", ...}
   ]

10. Frontend receives:
    React component renders content list
```

---

## 📝 Entity Class Structure

```java
@Entity                    // Marks as JPA entity
@Table(name = "content")   // Maps to database table
public class Content {

    @Id                                // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;              // Column constraints

    @Column(columnDefinition = "TEXT")
    private String description;        // Large text column

    @Enumerated(EnumType.STRING)
    private ContentType contentType;   // Enum field

    @PrePersist                        // Auto-set on insert
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate                         // Auto-set on update
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

---

## 🛠️ Dependency Injection Flow

```
Spring Container
├── Detects @SpringBootApplication
├── Scans @Entity classes
├── Creates @Repository beans (ContentRepository)
├── Creates @Service beans (ContentService)
│   └── Injects ContentRepository via @RequiredArgsConstructor
├── Creates @Controller beans (ContentController)
│   └── Injects ContentService via @RequiredArgsConstructor
└── Initializes DataSource
    └── HikariCP Connection Pool
        └── JDBC Connection String: jdbc:mysql://...
```

---

## 🔌 Configuration Chain

```
application.properties
│
├── spring.datasource.url
│   └── JDBC Connection String
│       └── HikariCP creates connection pool
│           └── Connects to MySQL
│
├── spring.datasource.username/password
│   └── Authentication credentials
│
├── spring.jpa.hibernate.ddl-auto
│   └── Controls schema generation
│       ├── validate: Check schema
│       ├── update: Modify existing schema
│       ├── create: Drop & recreate
│       └── create-drop: Drop on shutdown
│
├── spring.jpa.show-sql
│   └── Logs SQL queries to console
│
└── spring.jpa.properties.hibernate.format_sql
    └── Formats SQL for readability
```

---

## 📊 Data Flow in CRUD Operations

### CREATE (POST)

```
Frontend POST /api/content
  ↓
Controller receives JSON
  ↓
Hibernate deserializes to Content object
  ↓
Service.createContent() validates
  ↓
Repository.save(content)
  ↓
Hibernate generates INSERT SQL
  ↓
JDBC executes: INSERT INTO content (title, description, ...) VALUES (...)
  ↓
MySQL returns auto-generated ID
  ↓
Hibernate returns saved entity with ID
  ↓
Controller returns HTTP 201 + JSON response
```

### READ (GET)

```
Frontend GET /api/content/{id}
  ↓
Controller.getContentById(id)
  ↓
Service calls Repository
  ↓
Repository.findById(id)
  ↓
Hibernate generates SELECT query
  ↓
JDBC executes: SELECT * FROM content WHERE id = ?
  ↓
MySQL returns matching row
  ↓
Hibernate maps to Content object
  ↓
Controller returns HTTP 200 + JSON
```

### UPDATE (PUT)

```
Frontend PUT /api/content/{id}
  ↓
Controller receives JSON + ID
  ↓
Service fetches existing entity
  ↓
Updates entity fields
  ↓
Repository.save(updatedEntity)
  ↓
Hibernate generates UPDATE SQL
  ↓
JDBC executes: UPDATE content SET ... WHERE id = ?
  ↓
MySQL updates row
  ↓
Hibernate updates @PreUpdate timestamp
  ↓
Controller returns HTTP 200 + updated JSON
```

### DELETE (DELETE)

```
Frontend DELETE /api/content/{id}
  ↓
Controller.deleteContent(id)
  ↓
Service validates entity exists
  ↓
Repository.deleteById(id)
  ↓
Hibernate generates DELETE SQL
  ↓
JDBC executes: DELETE FROM content WHERE id = ?
  ↓
MySQL removes row
  ↓
Controller returns HTTP 200 + success message
```

---

## 🔍 Query Execution Example

```
Frontend calls: GET /api/content/search?keyword=Matrix

Java Code:
contentService.searchContent("Matrix")
  ↓
contentRepository.searchByTitle("Matrix")
  ↓
@Query("SELECT c FROM Content c WHERE LOWER(c.title)
        LIKE LOWER(CONCAT('%', :keyword, '%'))")
  ↓
Hibernate translates JPQL to SQL:
  ↓
SELECT * FROM content
WHERE LOWER(title) LIKE LOWER(CONCAT('%', 'Matrix', '%'))
  ↓
MySQL executes (case-insensitive search):
  ↓
Returns rows where title contains "Matrix"
  ↓
Hibernate maps results to List<Content>
  ↓
Spring serializes to JSON array
  ↓
Frontend receives: [{"id": 1, "title": "The Matrix", ...}]
```

---

## 📚 Annotation Guide

| Annotation                 | Layer      | Purpose                        |
| -------------------------- | ---------- | ------------------------------ |
| `@SpringBootApplication`   | Main       | Enables auto-config            |
| `@Entity`                  | Entity     | JPA entity class               |
| `@Table`                   | Entity     | Maps to database table         |
| `@Id`                      | Entity     | Primary key                    |
| `@GeneratedValue`          | Entity     | Auto-generate ID               |
| `@Column`                  | Entity     | Column constraints             |
| `@Enumerated`              | Entity     | Enum to string/ordinal         |
| `@PrePersist`              | Entity     | Auto-execute before insert     |
| `@PreUpdate`               | Entity     | Auto-execute before update     |
| `@Repository`              | Repository | Spring repository bean         |
| `@Service`                 | Service    | Spring service bean            |
| `@RestController`          | Controller | REST API controller            |
| `@GetMapping`              | Controller | GET endpoint                   |
| `@PostMapping`             | Controller | POST endpoint                  |
| `@PutMapping`              | Controller | PUT endpoint                   |
| `@DeleteMapping`           | Controller | DELETE endpoint                |
| `@RequestParam`            | Controller | URL query parameter            |
| `@PathVariable`            | Controller | URL path variable              |
| `@RequestBody`             | Controller | JSON request body              |
| `@CrossOrigin`             | Controller | Allow CORS requests            |
| `@Transactional`           | Service    | Transaction management         |
| `@Query`                   | Repository | Custom JPQL query              |
| `@Param`                   | Repository | Named query parameter          |
| `@RequiredArgsConstructor` | Any        | Constructor injection (Lombok) |

---

## 🚀 Startup Sequence

```
1. JVM starts
2. Spring Boot main() method executes
3. SpringApplication.run() initializes
4. ComponentScan finds @SpringBootApplication
5. Auto-configuration loads
   └── Detects MySQL driver on classpath
   └── Creates DataSource with HikariCP
   └── Creates JPA/Hibernate EntityManager
6. ComponentScan finds beans:
   └── @Entity (Content.java)
   └── @Repository (ContentRepository)
   └── @Service (ContentService)
   └── @RestController (ContentController)
7. Dependency injection:
   └── ContentController ← ContentService
   └── ContentService ← ContentRepository
8. EntityManager initializes Hibernate
9. Hibernate connects to MySQL (via JDBC)
10. DDL-auto executes:
    └── Checks if content table exists
    └── Creates/updates schema if needed
11. Spring Boot server starts on port 8080
12. Application ready to receive requests!
```

---

This architecture provides:

- ✅ **Separation of Concerns** - Clear layers
- ✅ **Reusability** - Service layer can be used by multiple controllers
- ✅ **Testability** - Each layer can be tested independently
- ✅ **Maintainability** - Easy to find and modify code
- ✅ **Scalability** - Can add caching, queues, etc. between layers
