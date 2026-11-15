import dao.UserDao;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserDao userDao;
    UserService userService;

    @BeforeEach
    void setUp() {
    userService = new UserService(userDao);
    }

    @Test
    void testAddUserSuccess() {
        User user = new User();
        user.setName("Joao");
        when(userDao.add(user)).thenReturn(1L);

        Long id = userService.addUser(user);
        assertEquals(1L, id);

        verify(userDao).add(user);
    }

    @Test
    void testAddUserWithEmptyNameThrowsException() {
        User user = new User();
        user.setName("");

        assertThrows(IllegalArgumentException.class, () -> userService.addUser(user));

        verify(userDao, never()).add(user);
    }

    @Test
    void testGetUserSuccess() {
        User user = new User();
        user.setName("Joao");
        when(userDao.findById(1)).thenReturn(user);

        User result = userService.getUser(1L);
        assertEquals("Joao", result.getName());

        verify(userDao).findById(1);
    }

    @Test
    void testGetUserThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(null));

        verify(userDao, never()).findById(anyInt());
    }

    @Test
    void testUpdateUserSuccess() {
        User user = new User();
        user.setName("Joao");

        userService.updateUserData(user);

        verify(userDao).update(user);
    }

    @Test
    void testUpdateUserThrowsException() {
        User user = new User();
        user.setName("");

        assertThrows(IllegalArgumentException.class, () -> userService.updateUserData(user));

        verify(userDao, never()).update(any());
    }

    @Test
    void testDeleteUserSuccess() {
        userService.removeUser(1L);

        verify(userDao).deleteUser(1);
    }

    @Test
    void testDeleteUserThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> userService.removeUser(null));

        verify(userDao, never()).deleteUser(anyInt());
    }
}
