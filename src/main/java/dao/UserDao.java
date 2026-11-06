package dao;

import entity.User;

public interface UserDao {
    Long add(User user);
    User findById(int id);
    void update(User user);
    void deleteUser(int id);
}
