-- ============================================
-- StreamHub Database Schema
-- MySQL Script to create the Content Table
-- ============================================

-- Create Database (if not exists)
CREATE DATABASE IF NOT EXISTS streamhub_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use the database
USE streamhub_db;

-- ============================================
-- Content Table
-- ============================================
-- This table stores all streaming content (movies, music, ebooks, etc.)

CREATE TABLE IF NOT EXISTS content (
    -- Primary Key
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    -- Content Metadata
    title VARCHAR(255) NOT NULL UNIQUE KEY,
    description LONGTEXT,
    content_type ENUM('MOVIE', 'MUSIC', 'EBOOK', 'SERIES', 'PODCAST', 'DOCUMENTARY', 'STAND_UP') NOT NULL,
    
    -- Content URL and Media
    content_url VARCHAR(500) NOT NULL,
    duration_minutes INT,
    thumbnail_url VARCHAR(500),
    
    -- Content Details
    genre VARCHAR(100),
    release_date DATETIME,
    rating DECIMAL(3, 1) COMMENT 'Rating out of 10',
    language VARCHAR(50),
    director VARCHAR(255),
    cast LONGTEXT COMMENT 'Cast members (JSON or comma-separated)',
    
    -- -- Status and Availability
    -- is_available BOOLEAN NOT NULL DEFAULT TRUE,
    -- is_premium BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- -- Analytics
    -- view_count BIGINT DEFAULT 0,
    -- likes_count BIGINT DEFAULT 0,
    
    -- Timestamps
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    
    -- Indexes for faster queries
    KEY idx_title (title),
    KEY idx_content_type (content_type),
    KEY idx_created_at (created_at),
    KEY idx_genre (genre),
    KEY idx_is_available (is_available),
    KEY idx_is_premium (is_premium)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Sample Data (Optional - for testing)
-- ============================================

-- Insert sample movies
INSERT INTO content (title, description, content_type, content_url, duration_minutes, thumbnail_url, genre, release_date, rating, language, director, is_available, is_premium, created_at) VALUES
('The Matrix', 'A computer hacker learns about the true nature of his reality.', 'MOVIE', '/videos/matrix.mp4', 136, '/thumbs/matrix.jpg', 'Action', '1999-03-31 00:00:00', 8.7, 'English', 'Lana Wachowski', TRUE, TRUE, NOW()),
('Inception', 'A skilled thief who steals corporate secrets through dream-sharing technology.', 'MOVIE', '/videos/inception.mp4', 148, '/thumbs/inception.jpg', 'Sci-Fi', '2010-07-16 00:00:00', 8.8, 'English', 'Christopher Nolan', TRUE, TRUE, NOW()),
('Interstellar', 'A team of explorers travel through a wormhole in space to ensure humanity\'s survival.', 'MOVIE', '/videos/interstellar.mp4', 169, '/thumbs/interstellar.jpg', 'Sci-Fi', '2014-11-07 00:00:00', 8.6, 'English', 'Christopher Nolan', TRUE, TRUE, NOW()),
('The Dark Knight', 'Batman faces the Joker, a criminal mastermind who wants to plunge Gotham into anarchy.', 'MOVIE', '/videos/darkknight.mp4', 152, '/thumbs/darkknight.jpg', 'Action', '2008-07-18 00:00:00', 9.0, 'English', 'Christopher Nolan', TRUE, TRUE, NOW());

-- Insert sample TV series
INSERT INTO content (title, description, content_type, content_url, thumbnail_url, genre, release_date, rating, language, is_available, is_premium, created_at) VALUES
('Breaking Bad', 'A high school chemistry teacher turns to cooking methamphetamine.', 'SERIES', '/videos/breakingbad.mp4', '/thumbs/breakingbad.jpg', 'Drama', '2008-01-20 00:00:00', 9.5, 'English', TRUE, TRUE, NOW()),
('Game of Thrones', 'A battle for power amid feudal political intrigue and supernatural forces.', 'SERIES', '/videos/gameofthrones.mp4', '/thumbs/gameofthrones.jpg', 'Fantasy', '2011-04-17 00:00:00', 9.2, 'English', TRUE, TRUE, NOW());

-- Insert sample music
INSERT INTO content (title, description, content_type, content_url, duration_minutes, thumbnail_url, genre, release_date, rating, language, is_available, is_premium, created_at) VALUES
('Bohemian Rhapsody', 'Epic rock song by Queen', 'MUSIC', '/songs/bohemianrhapsody.mp3', 6, '/thumbs/queen.jpg', 'Rock', '1975-10-31 00:00:00', 9.0, 'English', TRUE, FALSE, NOW()),
('Imagine', 'Iconic peace anthem by John Lennon', 'MUSIC', '/songs/imagine.mp3', 3, '/thumbs/lennon.jpg', 'Pop', '1971-09-09 00:00:00', 8.8, 'English', TRUE, FALSE, NOW());

-- Insert sample ebooks
INSERT INTO content (title, description, content_type, content_url, thumbnail_url, genre, release_date, rating, language, is_available, is_premium, created_at) VALUES
('The Great Gatsby', 'A tragic love story set in the Jazz Age', 'EBOOK', '/ebooks/gatsby.pdf', '/thumbs/gatsby.jpg', 'Romance', '1925-04-10 00:00:00', 8.5, 'English', TRUE, FALSE, NOW()),
('1984', 'A dystopian social science fiction novel by George Orwell', 'EBOOK', '/ebooks/1984.pdf', '/thumbs/1984.jpg', 'Sci-Fi', '1949-06-08 00:00:00', 8.9, 'English', TRUE, TRUE, NOW());

-- ============================================
-- Database Statistics Query
-- ============================================
-- SELECT 
--     COUNT(*) as total_content,
--     SUM(CASE WHEN is_available = TRUE THEN 1 ELSE 0 END) as available_content,
--     SUM(CASE WHEN is_premium = TRUE THEN 1 ELSE 0 END) as premium_content,
--     COUNT(DISTINCT content_type) as unique_types,
--     AVG(rating) as avg_rating
-- FROM content;

-- ============================================
-- Useful Queries for Testing
-- ============================================

-- Get all movies sorted by rating
-- SELECT * FROM content WHERE content_type = 'MOVIE' AND is_available = TRUE ORDER BY rating DESC;

-- Search for movies by title
-- SELECT * FROM content WHERE title LIKE '%Matrix%';

-- Get premium content
-- SELECT * FROM content WHERE is_premium = TRUE;

-- Get content added in last 7 days
-- SELECT * FROM content WHERE created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY);

-- Get most viewed content
-- SELECT * FROM content ORDER BY view_count DESC LIMIT 10;

-- Get content by genre
-- SELECT * FROM content WHERE genre = 'Action' AND is_available = TRUE;
