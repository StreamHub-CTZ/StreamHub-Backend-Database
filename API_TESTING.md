# API Testing Guide - cURL Commands

This file contains all cURL commands to test the Spring Boot REST API.

## ✅ Prerequisites

- Spring Boot application running: `mvn spring-boot:run`
- Base URL: `http://localhost:8080/api/content`
- MySQL database running with sample data

---

## 📋 GET Requests (Read Operations)

### 1. Get All Content

```bash
curl http://localhost:8080/api/content
```

**Expected Response (200 OK):**

```json
[
  {
    "id": 1,
    "title": "The Matrix",
    "description": "A computer hacker learns about the true nature of his reality.",
    "contentType": "MOVIE",
    "contentUrl": "/videos/matrix.mp4",
    "durationMinutes": 136,
    "thumbnailUrl": "/thumbs/matrix.jpg",
    "genre": "Action",
    "rating": 8.7,
    "language": "English",
    "director": "Lana Wachowski",
    "isPremium": true,
    "isAvailable": true,
    "viewCount": 0,
    "likesCount": 0,
    "createdAt": "2024-01-22T10:30:00",
    "updatedAt": "2024-01-22T10:30:00"
  },
  ...
]
```

---

### 2. Get Content by ID

```bash
curl http://localhost:8080/api/content/1
```

**Expected Response (200 OK):**

```json
{
  "id": 1,
  "title": "The Matrix",
  "description": "A computer hacker learns about the true nature of his reality.",
  "contentType": "MOVIE",
  ...
}
```

---

### 3. Get Only Available Content

```bash
curl http://localhost:8080/api/content/available
```

**Expected Response (200 OK):**

```json
[
  {
    "id": 1,
    "title": "The Matrix",
    ...
  },
  {
    "id": 2,
    "title": "Inception",
    ...
  }
]
```

---

### 4. Get Only Premium Content

```bash
curl http://localhost:8080/api/content/premium
```

**Expected Response (200 OK):**

```json
[
  {
    "id": 1,
    "title": "The Matrix",
    "isPremium": true,
    ...
  }
]
```

---

### 5. Get Content by Type (MOVIE, MUSIC, EBOOK, etc.)

**Get All Movies:**

```bash
curl http://localhost:8080/api/content/type/MOVIE
```

**Get All Music:**

```bash
curl http://localhost:8080/api/content/type/MUSIC
```

**Get All Ebooks:**

```bash
curl http://localhost:8080/api/content/type/EBOOK
```

**Get All Series:**

```bash
curl http://localhost:8080/api/content/type/SERIES
```

---

### 6. Get Content by Genre

```bash
curl http://localhost:8080/api/content/genre/Action
```

**Other Genres:**

- `Sci-Fi`
- `Drama`
- `Romance`
- `Fantasy`
- `Rock`
- `Pop`

---

### 7. Search Content by Keyword

```bash
curl "http://localhost:8080/api/content/search?keyword=Matrix"
```

**Other Examples:**

```bash
# Search for "Inception"
curl "http://localhost:8080/api/content/search?keyword=Inception"

# Search for "Game"
curl "http://localhost:8080/api/content/search?keyword=Game"

# Search is case-insensitive
curl "http://localhost:8080/api/content/search?keyword=matrix"
```

**Expected Response:**

```json
[
  {
    "id": 1,
    "title": "The Matrix",
    ...
  }
]
```

---

### 8. Get Top Rated Content

```bash
# Get top 10 (default)
curl http://localhost:8080/api/content/top-rated

# Get top 5
curl "http://localhost:8080/api/content/top-rated?limit=5"

# Get top 20
curl "http://localhost:8080/api/content/top-rated?limit=20"
```

---

### 9. Get Newly Added Content

```bash
# Get content added in last 7 days (default)
curl http://localhost:8080/api/content/new

# Get content added in last 30 days
curl "http://localhost:8080/api/content/new?days=30"

# Get content added in last 1 day
curl "http://localhost:8080/api/content/new?days=1"
```

---

### 10. Get Content Statistics

```bash
curl http://localhost:8080/api/content/stats
```

**Expected Response:**

```json
{
  "totalContent": 10,
  "availableContent": 9,
  "premiumContent": 7
}
```

---

## ✏️ POST Requests (Create Operations)

### 11. Create New Content (Movie)

```bash
curl -X POST http://localhost:8080/api/content \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Dune",
    "description": "Epic sci-fi film about desert politics",
    "contentType": "MOVIE",
    "contentUrl": "/videos/dune.mp4",
    "durationMinutes": 166,
    "genre": "Sci-Fi",
    "releaseDate": "2021-09-28T00:00:00",
    "rating": 8.0,
    "thumbnailUrl": "/thumbs/dune.jpg",
    "language": "English",
    "director": "Denis Villeneuve",
    "cast": "Timothée Chalamet, Zendaya, Oscar Isaac",
    "isPremium": true,
    "isAvailable": true
  }'
```

**Expected Response (201 CREATED):**

```json
{
  "id": 11,
  "title": "Dune",
  "description": "Epic sci-fi film about desert politics",
  "contentType": "MOVIE",
  ...
  "createdAt": "2024-01-22T11:45:30",
  "updatedAt": "2024-01-22T11:45:30"
}
```

---

### 12. Create New Content (Music)

```bash
curl -X POST http://localhost:8080/api/content \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Blinding Lights",
    "description": "Hit song by The Weeknd",
    "contentType": "MUSIC",
    "contentUrl": "/songs/blindinglights.mp3",
    "durationMinutes": 3,
    "genre": "Synthwave",
    "releaseDate": "2019-11-29T00:00:00",
    "rating": 8.5,
    "thumbnailUrl": "/thumbs/theweeknd.jpg",
    "language": "English",
    "isPremium": false,
    "isAvailable": true
  }'
```

---

### 13. Create New Content (Ebook)

```bash
curl -X POST http://localhost:8080/api/content \
  -H "Content-Type: application/json" \
  -d '{
    "title": "The Hobbit",
    "description": "Fantasy adventure by J.R.R. Tolkien",
    "contentType": "EBOOK",
    "contentUrl": "/ebooks/thehobbit.pdf",
    "genre": "Fantasy",
    "releaseDate": "1937-09-21T00:00:00",
    "rating": 8.9,
    "thumbnailUrl": "/thumbs/hobbit.jpg",
    "language": "English",
    "isPremium": false,
    "isAvailable": true
  }'
```

---

### 14. Create New Content (Series)

```bash
curl -X POST http://localhost:8080/api/content \
  -H "Content-Type: application/json" \
  -d '{
    "title": "The Mandalorian",
    "description": "Star Wars series with Din Djarin",
    "contentType": "SERIES",
    "contentUrl": "/videos/mandalorian.mp4",
    "genre": "Sci-Fi",
    "releaseDate": "2019-11-12T00:00:00",
    "rating": 8.7,
    "thumbnailUrl": "/thumbs/mandalorian.jpg",
    "language": "English",
    "isPremium": true,
    "isAvailable": true
  }'
```

---

### 15. Create Content with Minimal Fields

```bash
curl -X POST http://localhost:8080/api/content \
  -H "Content-Type: application/json" \
  -d '{
    "title": "My New Movie",
    "contentType": "MOVIE",
    "contentUrl": "/videos/mynewmovie.mp4"
  }'
```

**Note:** Only `title`, `contentType`, and `contentUrl` are required. Other fields are optional.

---

## 📝 PUT Requests (Update Operations)

### 16. Update Content - Change Rating

```bash
curl -X PUT http://localhost:8080/api/content/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "The Matrix",
    "description": "A computer hacker learns about the true nature of his reality.",
    "contentType": "MOVIE",
    "contentUrl": "/videos/matrix.mp4",
    "durationMinutes": 136,
    "genre": "Action",
    "rating": 9.0,
    "language": "English",
    "director": "Lana Wachowski",
    "isPremium": true,
    "isAvailable": true
  }'
```

**Expected Response (200 OK):**

```json
{
  "id": 1,
  "title": "The Matrix",
  "rating": 9.0,
  "updatedAt": "2024-01-22T12:00:00",
  ...
}
```

---

### 17. Update Content - Make Unavailable

```bash
curl -X PUT http://localhost:8080/api/content/2 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Inception",
    "description": "A skilled thief who steals corporate secrets through dream-sharing technology.",
    "contentType": "MOVIE",
    "contentUrl": "/videos/inception.mp4",
    "durationMinutes": 148,
    "genre": "Sci-Fi",
    "rating": 8.8,
    "language": "English",
    "director": "Christopher Nolan",
    "isPremium": true,
    "isAvailable": false
  }'
```

---

### 18. Update Content - Change Premium Status

```bash
curl -X PUT http://localhost:8080/api/content/5 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Bohemian Rhapsody",
    "description": "Epic rock song by Queen",
    "contentType": "MUSIC",
    "contentUrl": "/songs/bohemianrhapsody.mp3",
    "durationMinutes": 6,
    "genre": "Rock",
    "rating": 9.0,
    "language": "English",
    "isPremium": true,
    "isAvailable": true
  }'
```

---

## 🗑️ DELETE Requests (Delete Operations)

### 19. Delete Content by ID

```bash
# Delete content with ID 11
curl -X DELETE http://localhost:8080/api/content/11
```

**Expected Response (200 OK):**

```json
{
  "message": "Content deleted successfully"
}
```

---

### 20. Attempt to Delete Non-existent Content

```bash
curl -X DELETE http://localhost:8080/api/content/999
```

**Expected Response (404 NOT FOUND):**

```json
{}
```

---

## 🔗 POST Requests (Special Operations)

### 21. Increment View Count

```bash
curl -X POST http://localhost:8080/api/content/1/view
```

**Expected Response (200 OK):**

```json
{
  "message": "View count incremented"
}
```

---

## 🧪 Test Scenarios

### Scenario 1: Find All Action Movies

```bash
curl http://localhost:8080/api/content/type/MOVIE
# Filter results by genre "Action" manually
```

---

### Scenario 2: Search and Get Details

```bash
# 1. Search for "Breaking Bad"
curl "http://localhost:8080/api/content/search?keyword=Breaking"

# 2. Get the ID from results (e.g., ID 6)
# 3. Get full details
curl http://localhost:8080/api/content/6
```

---

### Scenario 3: Create, Update, then Delete

```bash
# 1. Create content
RESPONSE=$(curl -X POST http://localhost:8080/api/content \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Movie",
    "contentType": "MOVIE",
    "contentUrl": "/videos/test.mp4"
  }')

# 2. Extract ID from response
CONTENT_ID=$(echo $RESPONSE | grep -o '"id":[0-9]*' | cut -d: -f2)

# 3. Update it
curl -X PUT http://localhost:8080/api/content/$CONTENT_ID \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated Test Movie",
    "contentType": "MOVIE",
    "contentUrl": "/videos/test.mp4"
  }'

# 4. Delete it
curl -X DELETE http://localhost:8080/api/content/$CONTENT_ID
```

---

## 📊 Using Postman Instead of cURL

**Easy Alternative to cURL:**

1. Download [Postman](https://www.postman.com/downloads/)
2. Import this collection:
   - Create new request
   - Set method (GET, POST, PUT, DELETE)
   - Set URL (e.g., `http://localhost:8080/api/content`)
   - For POST/PUT: Set Body → Raw → JSON
   - Click Send

**Or Import cURL:**

1. In Postman, click "Import"
2. Select "Raw text"
3. Paste any cURL command from this guide

---

## ✅ Common Status Codes

| Code | Meaning                            | Example                    |
| ---- | ---------------------------------- | -------------------------- |
| 200  | OK - Success                       | GET request successful     |
| 201  | Created - Resource created         | POST request successful    |
| 400  | Bad Request - Invalid data         | Missing required field     |
| 404  | Not Found - Resource doesn't exist | GET with invalid ID        |
| 500  | Server Error                       | Database connection failed |

---

## 🔍 Debugging Tips

### View Response Headers

```bash
curl -i http://localhost:8080/api/content
# Shows status code, headers, and body
```

### View Only Response Headers

```bash
curl -I http://localhost:8080/api/content
```

### Pretty Print JSON Response

```bash
curl http://localhost:8080/api/content | jq
# Requires 'jq' to be installed
```

### Save Response to File

```bash
curl http://localhost:8080/api/content > response.json
```

### Verbose Output (shows request/response details)

```bash
curl -v http://localhost:8080/api/content
```

---

## 🚀 Batch Testing

### Run Multiple Requests

```bash
#!/bin/bash

echo "Testing StreamHub API..."

echo "1. Get all content"
curl http://localhost:8080/api/content

echo -e "\n2. Get statistics"
curl http://localhost:8080/api/content/stats

echo -e "\n3. Search for movies"
curl "http://localhost:8080/api/content/type/MOVIE"

echo -e "\n4. Get top rated"
curl "http://localhost:8080/api/content/top-rated?limit=5"

echo -e "\nAll tests completed!"
```

Save as `test_api.sh` and run:

```bash
bash test_api.sh
```

---

## 💡 Tips

- ✅ Always use `-H "Content-Type: application/json"` for POST/PUT
- ✅ Use single quotes `'` for JSON to preserve special characters
- ✅ Test GET requests first (no body needed)
- ✅ Check `/api/content/stats` to see current data
- ✅ Use `?keyword=term` for searches (case-insensitive)
- ✅ Content IDs are auto-generated starting from 1

---

**Happy Testing! 🎉**
