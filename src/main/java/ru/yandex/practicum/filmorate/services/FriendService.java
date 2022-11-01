package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.Friend;
import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

@Service
@Slf4j
public class FriendService {
    private final UserDao userDao;
    private final FriendDao friendDao;

    @Autowired
    public FriendService(UserDao userDao, FriendDao friendDao) {
        this.userDao = userDao;
        this.friendDao = friendDao;
    }

    public boolean addFriend(long userId, long friendId) {
        User user = userDao
                .findById(userId)
                .orElseThrow(() -> new NotExistException("User are not exist in the DB"));
        User friend = userDao
                .findById(friendId)
                .orElseThrow(() -> new NotExistException("User are not exist in the DB"));
        try {
            friendDao.add(user.getId(), friend.getId());
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("User with id " + userId + " is already friend with user with id " + friendId);
        }
        return true;
    }

    public boolean confirmFriends(long userId, long friendId) {
        Friend friend = friendDao
                .find(userId, friendId)
                .orElseThrow(() -> new NotExistException("User with id " + friendId + "is not a friend for " + userId));
        friendDao.confirm(friend.getUserId(), friend.getFriendId());
        return true;
    }

    public boolean removeFriend(long userId, long friendId) {
        Friend friend = friendDao
                .find(userId, friendId)
                .orElseThrow(() -> new NotExistException("User with id " + friendId + "is not a friend for " + userId));
        friendDao.remove(friend.getUserId(), friend.getFriendId());
        return true;
    }

    public List<User> getFriendsForUser(long userId) {
        return userDao.findFriends(userId);
    }


    public List<User> getCommonFriends(long id, long friendId) {
        return userDao.findCommonFriends(id, friendId);
    }
}