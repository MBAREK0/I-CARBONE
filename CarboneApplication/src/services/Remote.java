package services;

import services.UiElements.Ui;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import repositories.UserRepository;
import entities.User;
import java.sql.SQLException;


public class Remote {
     private UserService userService = new UserService();
     private Scanner scanner = new Scanner(System.in);
     private ConsumptionService consumptionService = new ConsumptionService();

    public void main() {
        boolean running = true;
        boolean isMenu = true;

        while (running) {

            if (isMenu) {
                Ui ui = new Ui();
                ui.displayMenu2();
                isMenu = false;
            }

            System.out.print("\n\n\t\ti@carbone:~$ ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    updateAccount();
                    break;
                case 3:
                    deleteAccount();
                    break;
                case 4:
                    displayUserInfo();
                    break;
                case 5:
                    addCarbonConsumption();
                    break;

                case 12:
                    isMenu = true;
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("\n\t\tInvalid choice, please try again.");
            }

        }
    }

    // -------------------------------------------------------------
    // ✨ Account Methods
    // -------------------------------------------------------------

    private void createAccount() {
        userService.addUser();
    }


    private void updateAccount() {
        userService.updateUser();
    }

     private void deleteAccount() {
        userService.deleteUser();
         }

    public void displayUserInfo() {
        userService.displayUserInfo();
    }

    // -------------------------------------------------------------
    // ✨ Consumption Methods
    // -------------------------------------------------------------

    private void addCarbonConsumption() {
        consumptionService.addConsumption();
    }

    }
