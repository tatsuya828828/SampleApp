INSERT INTO user(id, password, name)
VALUES('satou@gmail.com', '$2a$10$iMXSP71uLPvUgBEKaI3Kp.peKaVefaSad0os8ZV4vAsV9cVr9443O', 'さとう');

INSERT INTO user(id, password, name)
VALUES('a@a', '$2a$10$dAoAeR1J9bSBbqM5RzQOme1J9DstuGlqWbgPxG7km8FH6pknl0zQy', 'a');
-- bookのデータ
INSERT INTO book VALUES('アヒルと鴨のコインロッカー', null, '面白い', '伊坂幸太郎', 'satou@gmail.com', 0);
INSERT INTO book VALUES('スッキリわかるJava入門', null, 'わかりやすい', '中山清喬', 'a@a', 0);
INSERT INTO book VALUES('スッキリわかるJava入門 実践編', null, 'わかりやすい', '中山清喬', 'a@a', 0);
INSERT INTO book VALUES('スッキリわかるSQL入門', null, 'わかりやすい', '中山清喬', 'a@a', 0);

INSERT INTO evaluation
VALUES(5, 'a@a', 'アヒルと鴨のコインロッカー');