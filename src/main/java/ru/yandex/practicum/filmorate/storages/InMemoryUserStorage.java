package ru.yandex.practicum.filmorate.storages;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage{
    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User getById(long id) {
        return null;
    }

    @Override
    public User add(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public boolean remove(User user) {
        return false;
    }
}
