/*
================================================================================
PROJECT: StreamHub - Digital Content Distribution & Subscription Management
VERSION: 1.1.0
MODULES: IAM, Content, Subscription, Billing, Access Control, Analytics
STANDARDS: MySQL 8.0+, InnoDB, utf8mb4, Snake_Case, Audit Columns, RBAC
================================================================================

TABLE PURPOSES:
1. role: Defines RBAC permissions (Admin, Subscriber, etc.).
2. app_user: Central identity store for all system users.
3. user_role: Many-to-many mapping for users and their permissions.
4. content: Central catalog for videos, audio, and ebooks with JSON metadata.
5. subscription_plan: Product catalog defining prices and durations.
6. subscription: Tracks active user contracts and expiration dates.
7. payment_transaction: Detailed billing history and payment gateway responses.
8. access_control_log: High-performance log for DRM and content access attempts.
9. revenue_report: Aggregated financial metrics for management dashboards.
10. system_audit_log: Low-level tracking of data changes for compliance.
*/

CREATE DATABASE IF NOT EXISTS streamhub_db;
USE streamhub_db;

SET foreign_key_checks = 0;

-- -----------------------------------------------------------------------------
-- SECTION 1: IDENTITY & ACCESS MANAGEMENT (IAM)
-- -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS role (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    version_id INT UNSIGNED DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS app_user (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    status ENUM('PENDING', 'ACTIVE', 'SUSPENDED', 'DELETED') DEFAULT 'ACTIVE',
    version_id INT UNSIGNED DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    INDEX idx_user_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS user_role (
    user_id BIGINT UNSIGNED NOT NULL,
    role_id BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- SECTION 2: CONTENT & METADATA MANAGEMENT
-- -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS content (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    content_type ENUM('VIDEO', 'AUDIO', 'EBOOK') NOT NULL,
    genre VARCHAR(100),
    language VARCHAR(50),
    metadata JSON,
    rating VARCHAR(50),
    thumbnail_url LONGTEXT,
    duration INT,
    status ENUM('ACTIVE', 'ARCHIVED', 'DRAFT') DEFAULT 'ACTIVE',
    version_id INT UNSIGNED DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FULLTEXT INDEX ft_content_search (title, genre),
    INDEX idx_content_type (content_type),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- For VIDEO type content
CREATE TABLE IF NOT EXISTS video_metadata (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    content_id BIGINT UNSIGNED NOT NULL UNIQUE,
    duration INT,
    stream_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_video_content FOREIGN KEY (content_id) REFERENCES content(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- SECTION 3: SUBSCRIPTION & BILLING MANAGEMENT
-- -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS subscription_plan (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    plan_name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    duration_days INT NOT NULL,
    features JSON,
    is_active BOOLEAN DEFAULT TRUE,
    version_id INT UNSIGNED DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS subscription (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL,
    plan_id BIGINT UNSIGNED NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status ENUM('ACTIVE', 'EXPIRED', 'CANCELLED', 'PAST_DUE') DEFAULT 'ACTIVE',
    version_id INT UNSIGNED DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    CONSTRAINT fk_sub_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    CONSTRAINT fk_sub_plan FOREIGN KEY (plan_id) REFERENCES subscription_plan(id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Payment/Billing History Table
CREATE TABLE IF NOT EXISTS payment_transaction (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    subscription_id BIGINT UNSIGNED NOT NULL,
    user_id BIGINT UNSIGNED NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    payment_method ENUM('CREDIT_CARD', 'PAYPAL', 'STRIPE', 'APPLE_PAY') NOT NULL,
    transaction_status ENUM('SUCCESS', 'FAILED', 'REFUNDED', 'PENDING') NOT NULL,
    gateway_reference VARCHAR(255),
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    CONSTRAINT fk_pay_sub FOREIGN KEY (subscription_id) REFERENCES subscription(id),
    CONSTRAINT fk_pay_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    INDEX idx_pay_user (user_id),
    INDEX idx_pay_status (transaction_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- SECTION 4: ACCESS CONTROL & DRM
-- -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS access_control_log (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    content_id BIGINT UNSIGNED NOT NULL,
    user_id BIGINT UNSIGNED NOT NULL,
    access_status ENUM('GRANTED', 'DENIED_NO_SUBSCRIPTION', 'DENIED_DRM_VIOLATION') NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    content_title_snapshot VARCHAR(255), 
    CONSTRAINT fk_acl_content FOREIGN KEY (content_id) REFERENCES content(id) ON DELETE RESTRICT,
    CONSTRAINT fk_acl_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE RESTRICT,
    INDEX idx_access_content (content_id),
    INDEX idx_access_user_time (user_id, timestamp)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- SECTION 5: ANALYTICS & AUDIT
-- -----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS revenue_report (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    report_period_start DATE NOT NULL,
    report_period_end DATE NOT NULL,
    total_revenue DECIMAL(15, 2),
    active_users_count INT,
    metrics JSON, 
    generated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    INDEX idx_report_dates (report_period_start, report_period_end)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS system_audit_log (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    table_name VARCHAR(100) NOT NULL,
    action ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    record_id BIGINT UNSIGNED NOT NULL,
    old_value JSON,
    new_value JSON,
    performed_by VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_audit_table (table_name),
    INDEX idx_audit_timestamp (timestamp)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- SEED DATA
-- -----------------------------------------------------------------------------

INSERT INTO role (role_name, description, created_by) VALUES 
('ADMIN', 'Full system access', 'SYSTEM'),
('SUBSCRIBER', 'End user with streaming access', 'SYSTEM');

INSERT INTO app_user (username, email, password_hash, created_by) VALUES 
('admin_user', 'admin@streamhub.com', '$2y$12$6K8J3...', 'SYSTEM'),
('jdoe_99', 'john.doe@gmail.com', '$2y$12$R7n2P...', 'SYSTEM');

INSERT INTO user_role (user_id, role_id) VALUES (1, 1), (2, 2);

INSERT INTO content (title, content_type, genre, language, created_by) VALUES 
('The Great Voyage', 'VIDEO', 'Documentary', 'English', 'admin_user');

INSERT INTO subscription_plan (plan_name, price, duration_days, created_by) VALUES 
('Basic Monthly', 9.99, 30, 'SYSTEM');

INSERT INTO subscription (user_id, plan_id, start_date, end_date, created_by) VALUES 
(2, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'SYSTEM');

INSERT INTO payment_transaction (subscription_id, user_id, amount, payment_method, transaction_status, gateway_reference, created_by) VALUES 
(1, 2, 9.99, 'CREDIT_CARD', 'SUCCESS', 'TXN_12345ABC', 'SYSTEM');

SET foreign_key_checks = 1;

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
