CREATE TABLE blog_post
    (id INT PRIMARY KEY AUTO_INCREMENT,
     title VARCHAR(50) NOT NULL,
     author VARCHAR(50) NOT NULL,
     created_at TIMESTAMP DEFAULT NOW(),
     content TEXT NOT NULL);

CREATE TABLE comments
    (id INT PRIMARY KEY AUTO_INCREMENT,
     content TEXT NOT NULL,
     author VARCHAR(50) NOT NULL,
     created_at TIMESTAMP DEFAULT NOW(),
     blogpost_id INT NOT NULL,
     FOREIGN KEY(blogpost_id) REFERENCES blog_post(id) ON DELETE CASCADE);

CREATE TABLE likes
    (id INT PRIMARY KEY AUTO_INCREMENT,
     blogpost_id INT NOT NULL,
     FOREIGN KEY(blogpost_id) REFERENCES blog_post(id) ON DELETE CASCADE);
 
CREATE TABLE users
    (username VARCHAR(50) PRIMARY KEY,
     password VARCHAR(68) NOT NULL,
     enabled TINYINT DEFAULT 1);

CREATE TABLE authorities
    (username VARCHAR(50) NOT NULL,
     authority VARCHAR(50) NOT NULL,
     FOREIGN KEY(username) REFERENCES users(username) ON DELETE CASCADE,
     CONSTRAINT unique_user_authority UNIQUE(username,authority));

INSERT INTO users (username,password)
    VALUES ("anurag","{bcrypt}$2a$10$wRtVUSClULEOEpKsVt1.OOaB.oIqarPnqqZS.whaojAUqLrQr0U7.");

insert into authorities (username, authority)
    VALUES ("anurag","ROLE_ADMIN");
 