package test;
import java.util.Scanner;

public class TestUser {
    public static void main(String[] args) {
        User user1 = new User("mark", "mark@123");
        User user2 = new User("nick", "nick2020");
        User user3 = new User("nick", "nick31638");
        user1.addFavorite("The tourist");
        
        try {
            User.getUser("John");
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
        user1.followUser(user2);
        System.out.println(User.getAllUsers());
        System.out.println(User.getUsersCredentials());
        user1.logIn("nick", "nick2020");
        if (user1.checkLogIn("mark","mark@123") == true) {
            user1.followUser(user2);
            System.out.println(user1);
            System.out.println(user2);
        }
            user1.logIn("kate", "12345");
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Give your username: ");
        String username = scanner.nextLine();
        System.out.println("Give your password: ");
        String password = scanner.nextLine();
        System.out.println("Give your new username: ");
        String new_username = scanner.nextLine();
        scanner.close();
        user1.setUsername(username, password, new_username);
        System.out.println(user1);
    }
}