package dao;

import entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDaoImplTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");
    private static SessionFactory sessionFactory;
    private UserDao userDao;

    @BeforeAll
    static void setUp() {
        Configuration configuration = new Configuration();

        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");

        configuration.addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();
    }
    @AfterAll
    static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
    void init() {
        userDao = new UserDaoImpl(sessionFactory);
    }

    @Test
    void testAddUser() {
        User user = new User();
        user.setName("Joao"); user.setEmail("joao@test.com"); user.setAge(25);
        Long id = userDao.add(user);
        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    void testFindById() {
        User user = new User();
        user.setName("Joao");
        user.setEmail("joao@test.com");
        user.setAge(25);
        Long id = userDao.add(user);

        User foundUser = userDao.findById(Math.toIntExact(id));

        assertNotNull(foundUser);
        assertEquals("Joao", foundUser.getName());
        assertEquals("joao@test.com", foundUser.getEmail());
        assertEquals(25, foundUser.getAge());
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setName("Joao");
        user.setEmail("joao@test.com");
        user.setAge(25);
        Long id = userDao.add(user);

        user.setName("Joao Silva");
        user.setAge(26);
        userDao.update(user);

        User updatedUser = userDao.findById(Math.toIntExact(id));
        assertNotNull(updatedUser);
        assertEquals("Joao Silva", updatedUser.getName());
        assertEquals(26, updatedUser.getAge());
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setName("Joao");
        user.setEmail("joao@test.com");
        user.setAge(25);
        Long id = userDao.add(user);

        userDao.deleteUser(Math.toIntExact(id));

        User deletedUser = userDao.findById(Math.toIntExact(id));
        assertNull(deletedUser);
    }
}
