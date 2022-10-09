# java-filmorate
Template repository for Filmorate project.

Схема базы данных:
![Схема БД](/assets/images/db_cheme.jpg)

Основные запросы:
1. Получить всех польщователей

  select * 
  from users

2. Получить всех  друзей для пользователя c {id}

  select * from users
  where id in(
  select  friend_id 
  from users_friends where user_id = {id})

3. Получить общих друзей для пользователей c {id1}, {id2}

4. Получить cписок всех фильмов

  select * 
  from films

5. Получить cписок всех фильмов с жанорм {genre}

6. Получить cписок всех фильмов лайкнутых пользоватлем с {id}

7. Получить 10 самых популярных фильмов