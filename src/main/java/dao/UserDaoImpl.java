package dao;

import entity.User;
import exceptions.DaoException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class UserDaoImpl implements UserDao {

    private final SessionFactory sessionFactory;

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long add(User user){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            return user.getId();
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            handleException("Ошибка при сохранении пользователя", e);
            return (long) -1;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public User findById(int id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            return user;
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            handleException("Ошибка при поиске пользователя", e);
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void update(User user) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
                session.merge(user);
                transaction.commit();
        }catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            handleException("Ошибка при редактировании", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void deleteUser(int id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                transaction.commit();
            }
        }catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            handleException("Ошибка при удалении", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void handleException(String message, Throwable e) {
        throw new DaoException(message, e);
    }
}
