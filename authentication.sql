CREATE DATABASE estudy;
USE estudy;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
);
INSERT INTO users (username, password) VALUES
('user1', 'password1'),
('user2', 'password2'),
('pranav', '1234');
SELECT * FROM users;





