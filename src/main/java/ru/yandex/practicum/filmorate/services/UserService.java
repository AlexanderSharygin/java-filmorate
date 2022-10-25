package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAOs.UserDao;
import ru.yandex.practicum.filmorate.models.User;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public Optional<User> getUserById(Long id) {
        return userDao.getUserById(id);
    }

    public List<User> getFriendsForUser(long userId) {
        return userDao.getFriendsForUser(userId);
    }

    public List<User> getCommonFriends(long id, long friendId) {
        return userDao.getCommonFriends(id, friendId);
    }


    public Optional<User> addUser(User user) {
        checkName(user);
        return userDao.addUser(user);
    }

    public boolean addFriend(long userId, long friendId) {
        userDao.addFriend(userId, friendId);
        return true;
    }

    public boolean confirmFriends(long userId, long friendId) {
        userDao.confirmFriend(userId, friendId);
        return true;
    }

    public Optional<User> updateUser(User user) {
        checkName(user);
        return userDao.updateUser(user);
    }

    public boolean removeFriend(long userId, long friendId) {
        userDao.removeFriend(userId, friendId);
        return true;
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("В запросе не передано Имя пользователя email {}. В качестве имени будет использован login  {}",
                    user.getEmail(), user.getLogin());
            user.setName(user.getLogin());
        }
    }
}