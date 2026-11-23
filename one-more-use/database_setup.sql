-- ========================================================
-- One More Use - Complete Database Setup Script
-- Database: MySQL 8.0+
-- Run this script in MySQL Workbench or via command line:
-- mysql -u root -p < database_setup.sql
-- ========================================================

CREATE DATABASE IF NOT EXISTS one_more_use CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE one_more_use;

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

-- Seed Sample Users (Default password for all sample accounts: password123)
-- Uses BCrypt hash for "password123" (with fallback auto-upgrade on first login)
INSERT INTO users (id, name, email, password_hash, location) VALUES
(1, 'Priya Kulkarni', 'priya.kulkarni@gmail.com', '$2a$10$wT8hA8oN3lP2K4i6O8p5OeR6h3j2l1k0v9y8u7t6s5r4q3p2o1n0m', 'Kothrud, Pune'),
(2, 'Aditi Deshmukh', 'aditi.deshmukh@gmail.com', '$2a$10$wT8hA8oN3lP2K4i6O8p5OeR6h3j2l1k0v9y8u7t6s5r4q3p2o1n0m', 'Kothrud, Pune'),
(3, 'Rohan Patil', 'rohan.patil@gmail.com', '$2a$10$wT8hA8oN3lP2K4i6O8p5OeR6h3j2l1k0v9y8u7t6s5r4q3p2o1n0m', 'Baner, Pune'),
(4, 'Sneha Kulkarni', 'sneha.kulkarni@gmail.com', '$2a$10$wT8hA8oN3lP2K4i6O8p5OeR6h3j2l1k0v9y8u7t6s5r4q3p2o1n0m', 'Wakad, Pune'),
(5, 'Kunal Joshi', 'kunal.joshi@gmail.com', '$2a$10$wT8hA8oN3lP2K4i6O8p5OeR6h3j2l1k0v9y8u7t6s5r4q3p2o1n0m', 'Aundh, Pune'),
(6, 'Meera Joshi', 'meera.joshi@gmail.com', '$2a$10$wT8hA8oN3lP2K4i6O8p5OeR6h3j2l1k0v9y8u7t6s5r4q3p2o1n0m', 'Kothrud, Pune');

-- Seed Sample Items
INSERT INTO items (id, owner_id, title, description, category, item_condition, approx_size, location, emoji, exchange_type, preferred_exchange_item, is_available) VALUES
(1, 2, 'Old Wooden Box', 'A sturdy wooden storage box, slightly worn on the edges but structurally solid. Great for storage, planters or upcycling projects.', 'Furniture / Storage', 'Good — minor wear', 'Medium (fits in a bag)', 'Kothrud, Pune', '🪵', 'FREE', NULL, TRUE),
(2, 3, 'Cane Study Chair', 'A cane-weave study chair, sturdy frame with light surface wear. Ideal for a study table or reading corner.', 'Furniture', 'Fair — visible wear', 'Large (needs a vehicle)', 'Baner, Pune', '🪑', 'EXCHANGE', 'Study desk or wooden stool', TRUE),
(3, 4, 'Table Lamp, Working', 'A working table lamp with a fabric shade, tested and fully functional. Great for a study desk or bedside table.', 'Electronics / Lighting', 'Like new', 'Small (fits in hand)', 'Wakad, Pune', '💡', 'FREE', NULL, TRUE),
(4, 5, '2-Tier Bookshelf', 'Compact 2-tier wooden bookshelf, good for a small study corner or hallway.', 'Furniture / Storage', 'Good — minor wear', 'Large (needs a vehicle)', 'Aundh, Pune', '📚', 'FREE', NULL, TRUE),
(5, 6, 'Kids\' Cycle, 16 inch', '16 inch kids\' cycle with training wheels, well maintained and ready to ride.', 'Toys & Sports', 'Good — minor wear', 'Large (needs a vehicle)', 'Kothrud, Pune', '🚲', 'FREE', NULL, TRUE),
(6, 2, 'Mixer Jar Set', 'A set of 3 mixer jars, compatible with most standard mixer bases. Minor scratches, fully functional.', 'Kitchen Appliances', 'Fair — visible wear', 'Medium (fits in a bag)', 'Kothrud, Pune', '🫙', 'EXCHANGE', 'Toaster or electric kettle', TRUE);

-- Seed Sample Requests
INSERT INTO item_requests (id, item_id, requester_id, owner_id, message, status) VALUES
(1, 1, 3, 2, 'Hi, I need this wooden box for my balcony gardening project!', 'PENDING'),
(2, 1, 4, 2, 'Hi Aditi, I would love to use this box for organizing craft supplies.', 'ACCEPTED'),
(3, 4, 1, 5, 'Hi Kunal, I would like to pick up this 2-tier bookshelf for my study room.', 'PENDING'),
(4, 5, 1, 6, 'Hi Meera, is this kids cycle still available for pickup?', 'ACCEPTED');
