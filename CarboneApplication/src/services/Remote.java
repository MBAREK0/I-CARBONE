package services;

import services.UiElements.Ui;
import services.consumption.ConsumptionService;
import services.user.UserService;

import java.util.Scanner;


public class Remote {
     private UserService userService = new UserService();
     private Scanner scanner = new Scanner(System.in);
     private ConsumptionService consumptionService = new ConsumptionService();
     private ConsoleService consoleService = new ConsoleService();

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
                case 6:
                    try {
                        filterUsers();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    DisplayAverageConsumption();
                    break;
                case 8:
                    findInactiveUsers();
                    break;
                case 9:
                    displayAverageConsumptionForPeriod();
                    break;
                case 10:
                    displaySortedUsersByConsumption();
                    break;
                case 11:
                    generateUserConsumptionReport();
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
        consoleService.addUser();
    }
    private void updateAccount() {
        consoleService.updateUser();
    }
    private void deleteAccount() {
        consoleService.deleteUser();
         }
    public void displayUserInfo() {
        consoleService.displayUserInfo();
    }

    // -------------------------------------------------------------
    // ✨ Consumption Methods
    // -------------------------------------------------------------

    private void addCarbonConsumption() {
        consoleService.addConsumption();
    }

    public void filterUsers() throws Exception {
        userService.printFilteredUsers();
    }
    public void DisplayAverageConsumption() {
        consoleService.getAndDisplayAverageConsumption();
    }

    public void findInactiveUsers() {
        try {
            consoleService.findInactiveUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void displaySortedUsersByConsumption() {
        try {
            userService.displaySortedUsersByConsumption();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void displayAverageConsumptionForPeriod() {
        consoleService.displayAverageConsumptionForPeriod();
    }
    public void generateUserConsumptionReport() {
        consoleService.generateUserConsumptionReport();
    }


}
