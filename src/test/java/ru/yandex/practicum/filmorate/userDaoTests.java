package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.DAOs.UserDao;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@SpringBootTest
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class userDaoTests {
    private final UserDao userStorage;

    @Test
    @Order(1)
    public void testGetUserById() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test");
        testUser.setEmail("Test@test.com");
        testUser.setLogin("Test");
        testUser.setBirthday(LocalDate.parse("1980-01-01"));
        userStorage.addUser(testUser);
        Optional<User> userOptional = userStorage.getUserById(1L);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    @Order(2)
    public void testGetUsers() {
        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setName("Test2");
        testUser2.setEmail("Test@test2.com");
        testUser2.setLogin("Test2");
        testUser2.setBirthday(LocalDate.parse("1980-01-01"));
        userStorage.addUser(testUser2);
        List<User> users = userStorage.getUsers();
        assertThat(users.size()).isEqualTo(2);
        assertThat(users.get(0).getName()).isEqualTo("Test");
        assertThat(users.get(1).getName()).isEqualTo("Test2");
    }

    @Test
    @Order(3)
    public void addUser() {
        User testUser = new User();
        testUser.setId(3L);
        testUser.setName("Test3");
        testUser.setEmail("Test@test3.com");
        testUser.setLogin("Test3");
        testUser.setBirthday(LocalDate.parse("1980-01-01"));
        Optional<User> userOptional = userStorage.addUser(testUser);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 3L)
                );
    }

    @Test
    @Order(4)
    @AutoConfigureTestDatabase
    public void updateUser() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Upd");
        testUser.setEmail("Test@test.com");
        testUser.setLogin("Test");
        testUser.setBirthday(LocalDate.parse("1980-01-01"));
        Optional<User> userOptional = userStorage.updateUser(testUser);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                ).hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("Name", "Upd"));
    }

    @Test
    @Order(5)
    @AutoConfigureTestDatabase
    public void testAddFriend() {
        assertDoesNotThrow(() -> userStorage.addFriend(1L, 2L));
    }

    @Test
    @Order(6)
    @AutoConfigureTestDatabase
    public void testConfirmFriend() {
        assertDoesNotThrow(() -> userStorage.confirmFriend(1L, 2L));
    }

    @Test
    @Order(7)
    @AutoConfigureTestDatabase
    public void testRemoveFriend() {
        assertDoesNotThrow(() -> userStorage.removeFriend(1L, 2L));
    }

    @Test
    @Order(8)
    @AutoConfigureTestDatabase
    public void testGetFriendsForUser() {
        userStorage.addFriend(1L, 2L);
        userStorage.addFriend(1L, 3L);
        List<User> users = userStorage.getFriendsForUser(1L);
        assertThat(users.size()).isEqualTo(2);
        assertThat(users.get(0).getName()).isEqualTo("Test2");
        assertThat(users.get(1).getName()).isEqualTo("Test3");
    }

    @Test
    @Order(9)
    @AutoConfigureTestDatabase
    public void testGetCommonFriendsForUsers() {
        userStorage.addFriend(2L, 3L);
        List<User> users = userStorage.getCommonFriends(1L, 2L);
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getName()).isEqualTo("Test3");
    }
}
