package pl.danielw;

import pl.danielw.entity.User;
import pl.danielw.entity.UserDAO;

public class TaskManager {
    public static void main(String[] args) {

        /* Creation
        User u = new User("WOJO2114@GMAIL.COM", "WOJO", "haslo123");
        UserDAO.create(u);
        System.out.println(u);
         */

        /* Print all users in db
        User[] uArr = UserDAO.readAll();
        for (User u : uArr) {System.out.println(u);}
         */

        /* Read by id
        User u2 = UserDAO.readById(3);
        System.out.println(u2);
         */

        /* Read by email substring
        User[] uArr2 = UserDAO.readByEmail("WOJO");
        for (User u : uArr2) {System.out.println(u);}
         */

        /* Update user data
        User u3 = new User(3, "pomelo@china.cn", "Kamil", "czarnybanan");
        UserDAO.update(u3);
         */

        /* Delete user by ID
        UserDAO.delete(6);
         */
    }
}
