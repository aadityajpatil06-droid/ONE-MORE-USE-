-- ========================================================
-- One More Use - Database Schema (MySQL)
-- Database Name: one_more_use
-- ========================================================

CREATE DATABASE IF NOT EXISTS one_more_use CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE one_more_use;

-- Drop tables in reverse order of foreign key dependency if re-creating
DROP TABLE IF EXISTS item_requests;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

-- 1. Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    location VARCHAR(100) DEFAULT 'Pune',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Items Table
CREATE TABLE items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    category VARCHAR(100) NOT NULL,
    item_condition VARCHAR(50) NOT NULL,
    approx_size VARCHAR(50) DEFAULT 'Medium (fits in a bag)',
    location VARCHAR(100) NOT NULL,
    image_url VARCHAR(255),
    emoji VARCHAR(20) DEFAULT '📦',
    exchange_type VARCHAR(50) NOT NULL, -- 'FREE' or 'EXCHANGE'
    preferred_exchange_item VARCHAR(200),
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_item_owner FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Item Requests Table
CREATE TABLE item_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    owner_id BIGINT NOT NULL,
    message TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING', -- 'PENDING', 'ACCEPTED', 'REJECTED'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_req_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    CONSTRAINT fk_req_requester FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_req_owner FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
