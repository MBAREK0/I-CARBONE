import services.UiElements.Loader;
import services.UiElements.Ui;
import services.Remote;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Ui ui = new Ui();
        ui.displayMenu();

        System.out.print("\n\n\n\t\ti@carbone:~$ ");

        int choice = scanner.nextInt();
        scanner.nextLine(); 


            switch (choice) {
                case 1:
                    remote();
                    break;
                case 0:
                    System.out.println("\n\t\tExiting...");
                    break;
                default:
                    System.out.println("\n\t\tInvalid choice. Please try again.");
                    break;

        }
    }

    public static void loading() {
        Loader.loading();
    }

    public static void remote() {
        loading(); 
        Remote.main(null); 
    }
}
