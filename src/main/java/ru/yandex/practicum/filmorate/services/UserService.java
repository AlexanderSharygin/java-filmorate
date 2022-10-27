package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAOs.FriendDao;
import ru.yandex.practicum.filmorate.DAOs.UserDao;
import ru.yandex.practicum.filmorate.models.Friend;
import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserDao userDao;
    private final FriendDao friendDao;

    @Autowired
    public UserService(UserDao userDao, FriendDao friendDao) {
        this.userDao = userDao;
        this.friendDao = friendDao;
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public User getUserById(Long id) {
        return userDao.findUserById(id).orElse(null);
    }

    public User addUser(User user) {
        checkName(user);
        userDao.addUser(user);
        return userDao.findNewUser().orElse(null);

    }

    public User updateUser(User user) {
        checkName(user);
        userDao.updateUser(user);
        return userDao.findUserById(user.getId()).get();
    }


    public List<User> getFriendsForUser(long userId) {

        return userDao.getFriendsForUser(userId);
    }

    public List<User> getCommonFriends(long id, long friendId) {
        return userDao.getCommonFriends(id, friendId);
    }


    public boolean addFriend(long userId, long friendId) {
        User user = userDao.findUserById(userId).get();
        User friend = userDao.findUserById(friendId).get();
        friendDao.addFriend(user.getId(), friend.getId());
        return true;
    }

    public boolean confirmFriends(long userId, long friendId) {
        Friend friend = friendDao.getFriend(userId, friendId).get();
        friendDao.confirmFriend(friend.getUserId(), friend.getFriendId());
        return true;
    }


    public boolean removeFriend(long userId, long friendId) {
        Friend friend = friendDao.getFriend(userId, friendId).get();
        friendDao.removeFriend(friend.getUserId(), friend.getFriendId());;
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