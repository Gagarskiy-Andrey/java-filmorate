delete
from MPA;
delete
from GENRES;
INSERT INTO GENRES (GENRE_NAME) VALUES
('Комедия'),
('Драма'),
('Мультфильм'),
('Триллер'),
('Документальный'),
('Боевик');
MERGE INTO MPA (MPA_ID, MPA_NAME) VALUES
(1,'G'),
(2,'PG'),
(3,'PG-13'),
(4,'R'),
(5,'NC-17');