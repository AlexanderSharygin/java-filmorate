package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
        User user;
        User friend;
        try {
            user = userDao.findUserById(userId).get();
            friend = userDao.findUserById(friendId).get();
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User are not exist in the DB");
        }
        try {
            friendDao.addFriend(user.getId(), friend.getId());
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("User with id " + userId + " is already friend with user with id " + friendId);
        }
        return true;
    }

    public boolean confirmFriends(long userId, long friendId) {
        try {
            Friend friend = friendDao.findFriendCombination(userId, friendId).get();
            friendDao.confirmFriend(friend.getUserId(), friend.getFriendId());
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User with id " + friendId + "is not a friend for " + userId);
        }
        return true;
    }

    public boolean removeFriend(long userId, long friendId) {
        try {
            Friend friend = friendDao.findFriendCombination(userId, friendId).get();
            friendDao.removeFriend(friend.getUserId(), friend.getFriendId());
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User with id " + friendId + "is not a friend for " + userId);
        }
        return true;
    }

    public List<User> getFriendsForUser(long userId) {
        try {
            return userDao.findFriendsForUser(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("Were are not friends for user with id " + userId);
        }
    }

    public List<User> getCommonFriends(long id, long friendId) {
        try {
            return userDao.findCommonFriends(id, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("Were are not common friends for user with id " + id + " and " + friendId);
        }
    }
}