# java-filmorate
Template repository for Filmorate project.

Схема базы данных:
![Схема БД](/assets/images/db_cheme.jpg)

Основные запросы:
1. Получить всех польщователей

SELECT * 
FROM users

2. Получить всех  друзей для пользователя c {id}

SELECT * FROM users
WHERE id in(
SELECT  friend_id 
FROM users_friends where user_id = {id})

3. Получить общих друзей для пользователей c {id1}, {id2}

SELECT * FROM users
WHERE users.id in 
(SELECT u.id
FROM users u
JOIN users_users uf ON u.id = uf.users_id
WHERE uf.users_id = 1 OR uf.users_id = 2
GROUP BY u.id
HAVING count(u.id)=2)

4. Получить cписок всех фильмов

SELECT * 
FROM films

5. Получить cписок всех фильмов с жанорм {genre}

SELECT * FROM films f
JOIN genres_films gf ON f.id = gf.films_id
JOIN genres gs ON gs.id = gf.genre_id
WHERE gs."name" = {genre}

6. Получить cписок всех фильмов лайкнутых пользоватлем с {id}

SELECT * FROM films f
JOIN likes l ON l.films_id = f.id
JOIN users u ON u.id = l.users_id
WHERE u.id = {id}

7. Получить 10 самых популярных фильмов

SELECT * FROM films  
WHERE films.id in(
SELECT f.id FROM films f
JOIN likes l ON f.id = l.films_id
group by f.id
order by count(f.id)
LIMIT 10)