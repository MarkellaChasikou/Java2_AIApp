
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        /*System.out.println("Choose a username: ");
        String username = scanner.nextLine();
        System.out.println("Choose a password: ");
        String password = scanner.nextLine();*/
        System.out.println("PLease fill in your username: ");
        String logName = scanner.nextLine();
        System.out.println("Please fill in your password: ");
        String logPassword = scanner.nextLine();
         //Try register new user
        /*try {
        User user = new User(username, password);
        user.register(user);
        System.out.println("Welcome to FilmBro " +username+"!!!You have now been registered");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }*/

        boolean loggedIn = false;
        try {
        User user = User.authenticate(logName,logPassword);
        loggedIn = true;
            if (loggedIn) {
                //User.addToList(user, "watchlist", "Maestro");
                System.out.println(User.getList(user, "watchlist"));
            }
        } catch (Exception e ) {
            System.out.println(e.getMessage());
        }
        scanner.close();
    }
}













