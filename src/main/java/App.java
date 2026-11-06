import dao.UserDao;
import dao.UserDaoImpl;
import entity.User;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        UserDao userDao = new UserDaoImpl(sessionFactory);
        while (true) {
            System.out.println("=== Меню ===");
            System.out.println("1. Добавить пользователя");
            System.out.println("2. Найти пользователя по ID");
            System.out.println("3. Обновить пользователя");
            System.out.println("4. Удалить пользователя");
            System.out.println("0. Выход");
            System.out.println("Выберите действие:");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Введите имя: "); String name = scanner.nextLine();
                    System.out.print("Введите email: "); String email = scanner.nextLine();
                    System.out.print("Введите возраст: "); int age = scanner.nextInt(); scanner.nextLine();
                    User user = new User();
                    user.setName(name); user.setEmail(email); user.setAge(age);
                    Long id = userDao.add(user);
                    System.out.println("Пользователь добавлен с ID: " + id);
                    break;
                case 2:
                    System.out.print("Введите ID пользователя: ");
                    int searchId = scanner.nextInt();
                    scanner.nextLine();
                    User foundUser = userDao.findById(searchId);
                    if (foundUser != null) {
                        System.out.println("Найден: " + foundUser.getName() + ", Email: " + foundUser.getEmail() + ", Возраст: " + foundUser.getAge());
                    } else {
                        System.out.println("Пользователь с таким ID не найден");
                    }
                    break;
                case 3:
                    System.out.print("Введите ID пользователя: ");
                    searchId = scanner.nextInt();
                    scanner.nextLine();
                    foundUser = userDao.findById(searchId);
                    if (foundUser != null) {
                        System.out.print("Введите имя: "); name = scanner.nextLine();
                        System.out.print("Введите email: "); email = scanner.nextLine();
                        System.out.print("Введите возраст: "); age = scanner.nextInt(); scanner.nextLine();
                        foundUser.setName(name);
                        foundUser.setEmail(email);
                        foundUser.setAge(age);
                        userDao.update(foundUser);
                        System.out.println("Пользователь с ID: " + searchId +" изменен");} else {
                        System.out.println("Пользователь с таким ID не найден");
                    }
                    break;
                case 4:
                    System.out.print("Введите ID для удаления: ");
                    int deleteId = scanner.nextInt(); scanner.nextLine();
                    foundUser = userDao.findById(deleteId);
                    if (foundUser != null) {
                        userDao.deleteUser(deleteId);
                        System.out.println("Пользователь с ID: " + deleteId +" удален");
                    } else {
                        System.out.println("Пользователь с таким ID не найден");
                    }
                    break;
                case 0:
                    System.out.println("Вы вышли из программы");
                    return;
                default:
                    System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }
}

