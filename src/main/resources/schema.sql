CREATE TABLE IF NOT EXISTS user(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, created_at DATE, last_login DATETIME, self_id VARCHAR(50) UNIQUE
	, password VARCHAR(100), name VARCHAR(50), image VARCHAR(50));

CREATE TABLE IF NOT EXISTS book(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, created_at DATE, title VARCHAR(50),
	body VARCHAR(100), author VARCHAR(50), genre VARCHAR(50)
	, user_id INT REFERENCES user(id), evaluation INT, image VARCHAR(50));
CREATE TABLE IF NOT EXISTS comment(
	created_at DATE, user_id INT REFERENCES user(id), book_id INT REFERENCES book(id)
	, comment VARCHAR(50), evaluation INT, PRIMARY KEY(user_id, book_id));