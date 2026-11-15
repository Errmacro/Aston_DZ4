package service;

import dao.UserDao;
import entity.User;
import org.hibernate.cfg.Configuration;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Long addUser(User user) {
        if (user == null || user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("Некорректные данные пользователя");
        }
        return userDao.add(user);
    }

    public User getUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Пользователя с таким id не существует");
        }
        return userDao.findById(Math.toIntExact(id));
    }

    public void updateUserData(User user){
        if (user == null || user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("Некорректные данные пользователя");
        }
        userDao.update(user);
    }

    public void removeUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Пользователя с таким id не существует");
        }
        userDao.deleteUser(Math.toIntExact(id));
    }
}
