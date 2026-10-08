CREATE DATABASE IF NOT EXISTS event_ticket_db;
USE event_ticket_db;

DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    role VARCHAR(10) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE events (
    event_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    category VARCHAR(40) NOT NULL,
    description TEXT,
    venue VARCHAR(150) NOT NULL,
    city VARCHAR(80) NOT NULL,
    event_date DATE NOT NULL,
    event_time TIME NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    total_seats INT NOT NULL,
    available_seats INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    tickets INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id)
);

-- Passwords are SHA-256 of admin123 and demo123
INSERT INTO users (full_name, email, password, phone, role) VALUES
('System Admin', 'admin@eventhub.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', '9999999999', 'ADMIN'),
('Demo User', 'demo@eventhub.com', 'd3ad9315b7be5dd53b31a273b3b3aba5defe700808305aa16a3062b76658a791', '8888888888', 'USER');

INSERT INTO events (title, category, description, venue, city, event_date, event_time, price, total_seats, available_seats) VALUES
('Arijit Singh Live Concert', 'Concert', 'An unforgettable night of live music with Arijit Singh. Enjoy chart-topping romantic and Bollywood hits with a full live orchestra.', 'NSCI Dome', 'Mumbai', '2026-11-15', '19:00:00', 2499.00, 500, 500),
('India vs Australia T20', 'Sports', 'International T20 cricket match. Cheer for Team India in a high-energy evening at the stadium.', 'Wankhede Stadium', 'Mumbai', '2026-10-20', '18:30:00', 1800.00, 800, 800),
('Shakespeare in the Park', 'Theatre', 'Open-air performance of A Midsummer Night''s Dream with a modern Indian twist.', 'Rangsharda Auditorium', 'Mumbai', '2026-10-05', '20:00:00', 799.00, 200, 200),
('TechNext 2026 Conference', 'Conference', 'Keynotes, workshops and networking for students and professionals in AI, cloud and full-stack development.', 'Bombay Exhibition Centre', 'Mumbai', '2026-12-02', '09:30:00', 1299.00, 350, 350),
('Navratri Garba Night', 'Festival', 'Traditional Garba and Dandiya with live dhol and celebrity singers. Traditional or festive wear recommended.', 'GMDC Ground', 'Ahmedabad', '2026-10-12', '20:30:00', 499.00, 1000, 1000),
('Sunburn Arena', 'Concert', 'EDM arena night featuring international DJs, LED stage production and food stalls.', 'Mahalaxmi Race Course', 'Mumbai', '2026-11-28', '16:00:00', 3499.00, 600, 600),
('Stand-up Comedy Night', 'Comedy', 'A lineup of India''s top stand-up comics. 18+ show with dinner and drinks available at the venue.', 'The Habitat', 'Pune', '2026-10-18', '21:00:00', 699.00, 150, 150),
('Classical Music Evening', 'Concert', 'Sitar, tabla and vocal recitals celebrating Indian classical ragas.', 'NCPA Tata Theatre', 'Mumbai', '2026-11-08', '18:00:00', 999.00, 250, 250);
