INSERT INTO user_table(user_id, password, name)
VALUES('satou@gmail.com', '$2a$10$iMXSP71uLPvUgBEKaI3Kp.peKaVefaSad0os8ZV4vAsV9cVr9443O', 'さとう');

INSERT INTO book_table(title, body, user_id)
VALUES('アヒルと鴨のコインロッカー', '面白い', 'satou@gmail.com')