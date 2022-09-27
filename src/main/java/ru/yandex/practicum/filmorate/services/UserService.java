package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserStorage userStorage;

    public boolean addFriend(long userId, long friendId) {
        if (isInputDataValid(userId, friendId)) {
            User user = this.userStorage.getUsers().get(userId);
            User friend = this.userStorage.getUsers().get(friendId);
            if (!user.getFriends().contains(friendId)) {
                user.getFriends().add(friendId);
                friend.getFriends().add(userId);
                return true;
            } else {
                throw new AlreadyExistException("User with id " + userId + " has already friend with user " + friendId);
            }
        }
        return false;
    }

    public boolean removeFriend(long userId, long friendId) {
        if (isInputDataValid(userId, friendId)) {
            User user = this.userStorage.getUsers().get(userId);
            User friend = this.userStorage.getUsers().get(friendId);
            if (user.getFriends().contains(friendId)) {

                user.getFriends().remove(friendId);
                friend.getFriends().remove(userId);
                return true;
            } else {
                throw new AlreadyExistException("User with id " + userId + " has not friend with user " + friendId);
            }
        }
        return false;
    }

    public List<User> geUserFriends(long userId) {
        Set<Long> friendsId = userStorage.getUsers().get(userId).getFriends();
        List<User> friends = new ArrayList<>();
        for (var id : friendsId) {
            friends.add(userStorage.getUsers().get(id));
        }
        return friends;
    }

    private boolean isInputDataValid(long userId, long friendId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotExistException("User with specified id " + userId + "is not exist");
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new NotExistException("User with specified id " + friendId + "is not exist");
        }
        return true;
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            //  log.warn("В запросе не передано Имя пользователя email {}. В качестве имени будет использован login  {}",
            //   user.getEmail(), user.getLogin());
            user.setName(user.getLogin());
        }
    }

    public User add(User user) {
        checkName(user);
        return this.userStorage.add(user);
    }

    public User update(User user) {
        checkName(user);
        return this.userStorage.update(user);
    }

    public User remove(long id) {
        return userStorage.remove(id);
    }

    public List<User> getAll() {
        return new ArrayList<>(userStorage.getUsers().values());
    }

    public User getUserById(long id) {
        return userStorage.getById(id);
    }
}