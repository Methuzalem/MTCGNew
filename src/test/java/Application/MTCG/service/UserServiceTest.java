package Application.MTCG.service;

import Application.MTCG.entity.User;
import Application.MTCG.repositorys.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("UUID123", "TestUser", "password", "token123", 10, 0, "name", "bio", "image", 100, 1, 0);
    }

    @Test
    void createShouldCreateUserWhenUserDoesNotExist() {
        when(userRepo.findUserByName(user.getUsername())).thenReturn(Optional.empty());

        User savedUser = new User("UUID123", "TestUser", "password", "token123", 10, 0, "name", "bio", "image", 100, 1, 0);
        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        var createdUserDTO = userService.create(user);

        assertEquals("TestUser", createdUserDTO.getUsername());
    }

    @Test
    void getUserByTokenShouldReturnUserWhenTokenIsValid() {
        when(userRepo.findUserByToken("token123")).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByToken("token123");

        assertEquals("TestUser", foundUser.getUsername());
    }

    @Test
    void matchTokenWithPathShouldReturnTrueWhenTokenMatchesPath() {
        assertTrue(userService.matchTokenWithPath("TestUser-token", "/users/TestUser"));
    }

    @Test
    void matchTokenWithPathShouldReturnFalseWhenPathDoesNotStartWithUsers() {
        assertFalse(userService.matchTokenWithPath("TestUser-token", "/notusers/TestUser"));
    }

}

