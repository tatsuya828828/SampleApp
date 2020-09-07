CREATE TABLE IF NOT EXISTS user(
	id VARCHAR(50) PRIMARY KEY, password VARCHAR(100), name VARCHAR(50));

CREATE TABLE IF NOT EXISTS book(
	title VARCHAR(50) PRIMARY KEY, new_title VARCHAR(50),
	body VARCHAR(100), author VARCHAR(50), user_id VARCHAR(50) REFERENCES user(id), evaluation INT);
CREATE TABLE IF NOT EXISTS comment(
	user_id VARCHAR(50) REFERENCES user(id), book_id VARCHAR(50) REFERENCES book(title)
	, comment VARCHAR(50));
CREATE TABLE IF NOT EXISTS evaluation(
	evaluation INT, user_id VARCHAR(50) REFERENCES user(id), book_id VARCHAR(50) REFERENCES book(title))