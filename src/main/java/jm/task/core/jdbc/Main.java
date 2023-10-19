package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Nikita", "Kirilov", (byte) 28);
        userService.saveUser("Georgy", "Alburov", (byte) 27);
        userService.saveUser("Andrey", "Pavlov", (byte) 25);
        userService.saveUser("Ekaterina", "Morozova", (byte) 26);
        userService.getAllUsers().forEach(System.out::println);
        userService.cleanUsersTable();
        userService.dropUsersTable();
        Util.closePool();
    }
}