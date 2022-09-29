package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserStorage userStorage;

    public List<User> getAll() {
        return new ArrayList<>(userStorage.getAll().values());
    }

    public User getUserById(long id) {
        return userStorage.getById(id);
    }

    public List<User> getCommonFriends(long id, long friendId) {
        if (!userStorage.getAll().containsKey(id)) {
            throw new NotExistException("User with specified id " + id + "is not exist");
        }
        if (!userStorage.getAll().containsKey(friendId)) {
            throw new NotExistException("User with specified id " + friendId + "is not exist");
        }
        Set<Long> firstUserFriends = userStorage.getAll().get(id).getFriends();
        Set<Long> secondUserFriends = userStorage.getAll().get(friendId).getFriends();
        Set<Long> commonFriendsId = firstUserFriends.stream().filter(secondUserFriends::contains).collect(Collectors.toSet());
        return commonFriendsId.stream().map(item -> userStorage.getById(item)).collect(Collectors.toList());
    }

    public List<User> getUserFriends(long userId) {
        Set<Long> friendsId = userStorage.getAll().get(userId).getFriends();
        List<User> friends = new ArrayList<>();
        for (var id : friendsId) {
            friends.add(userStorage.getAll().get(id));
        }
        return friends;
    }

    public User addUser(User user) {
        checkName(user);
        return this.userStorage.add(user);
    }

    public boolean addFriend(long userId, long friendId) {
        if (isInputDataValid(userId, friendId)) {
            User user = this.userStorage.getAll().get(userId);
            User friend = this.userStorage.getAll().get(friendId);
            if (!user.getFriends().contains(friendId)) {
                user.getFriends().add(friendId);
                friend.getFriends().add(userId);
                log.info("Пользователь с id {} добавил в друзья пользователя с id {}", userId, friendId);
                return true;
            } else {
                throw new AlreadyExistException("User with id " + userId + " has already friend with user " + friendId);
            }
        }
        return false;
    }

    public User updateUser(User user) {
        checkName(user);
        return this.userStorage.update(user);
    }

    public boolean removeFriend(long userId, long friendId) {
        if (isInputDataValid(userId, friendId)) {
            User user = this.userStorage.getAll().get(userId);
            User friend = this.userStorage.getAll().get(friendId);
            if (user.getFriends().contains(friendId)) {

                user.getFriends().remove(friendId);
                friend.getFriends().remove(userId);
                log.info("Пользователь с id {} удалил из друзей пользователя с id {}", userId, friendId);
                return true;
            } else {
                throw new AlreadyExistException("User with id " + userId + " has not friend with user " + friendId);
            }
        }
        return false;
    }

    private boolean isInputDataValid(long userId, long friendId) {
        if (!userStorage.getAll().containsKey(userId)) {
            throw new NotExistException("User with specified id " + userId + "is not exist");
        }
        if (!userStorage.getAll().containsKey(friendId)) {
            throw new NotExistException("User with specified id " + friendId + "is not exist");
        }
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