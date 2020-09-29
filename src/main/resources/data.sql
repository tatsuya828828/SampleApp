INSERT INTO user(created_at, last_login, self_id, password, name)
VALUES(CURRENT_DATE, LOCALTIME, 'satou@gmail.com', '$2a$10$iMXSP71uLPvUgBEKaI3Kp.peKaVefaSad0os8ZV4vAsV9cVr9443O', 'さとう');

INSERT INTO user(created_at, last_login, self_id, password, name)
VALUES(CURRENT_DATE, LOCALTIME, 'a@a', '$2a$10$dAoAeR1J9bSBbqM5RzQOme1J9DstuGlqWbgPxG7km8FH6pknl0zQy', 'a');
-- bookのデータ
INSERT INTO book VALUES(NULL, CURRENT_DATE, 'アヒルと鴨のコインロッカー', '面白い', '伊坂幸太郎', '小説', 1, 0, '/images/ahiru.jpg');
INSERT INTO book VALUES(NULL, CURRENT_DATE, 'スッキリわかるJava入門', 'わかりやすい', '中山清喬', '学習本', 2, 0, '/images/NOIMAGE.png');
INSERT INTO book VALUES(NULL, CURRENT_DATE, 'スッキリわかるJava入門 実践編', 'わかりやすい', '中山清喬', '学習本', 2, 0, '/images/NOIMAGE.png');
INSERT INTO book VALUES(NULL, CURRENT_DATE, 'スッキリわかるSQL入門', 'わかりやすい', '中山清喬', '学習本', 2, 0, '/images/NOIMAGE.png');