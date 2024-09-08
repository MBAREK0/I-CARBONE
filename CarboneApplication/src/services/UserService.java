package services;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import entities.*;
import repositories.ConsumptionRepository;
import repositories.UserRepository;



public class UserService {
    private Scanner scanner = new Scanner(System.in);
    private UserRepository UserRepository = new UserRepository();

    public void addUser() {
        System.out.print("\n\t\t-------------------\n");

        int id = 0;
        System.out.print("\t\tEnter Name: ");
        String name = scanner.nextLine();
        System.out.print("\t\tEnter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("\t\tEnter CIN: ");
        String cin = scanner.nextLine();

        // Create a new User object
        User user = new User(id, name, age, cin);

        try {
            UserRepository userRepository = new UserRepository();

            // Check if user already exists by CIN
            if (userRepository.getUserByCin(cin) == null) {
                // Add the user to the database
                userRepository.addUser(user);
                System.out.println("\n\t\t| Account created successfully |");
            } else {
                System.out.println("\n\t\t| Account creation failed. User with this CIN already exists |");
            }

        } catch (SQLException e) {
            System.out.println("\n\t\t| Account creation failed. An error occurred |");
            e.printStackTrace();
        }
    }

    public void updateUser() {
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter Cin of the user to update: ");
        String cin = scanner.nextLine();


        try {
            UserRepository userRepository = new UserRepository();
            // Fetch the user from the database
            User user = userRepository.getUserByCin(cin);

            if (user != null) {
                System.out.print("\t\tEnter new Name: ");
                String newName = scanner.nextLine();
                System.out.print("\t\tEnter new Age: ");
                int newAge = scanner.nextInt();
                scanner.nextLine(); // Consume the newline
                System.out.print("\t\tEnter New CIN: ");
                String newCin = scanner.nextLine();


                if (newAge < 0 || newAge > 120) {
                    System.out.println("\n\t\t| Account update failed. Please enter a valid age |");
                } else {
                    // Update user details
                    user = new User(user.getId(), newName, newAge, newCin);
                    userRepository.updateUser(user);
                    System.out.println("\n\t\t| Account updated successfully |");
                }
            } else {
                System.out.println("\n\t\t| User not found |");
            }
        } catch (SQLException e) {
            System.out.println("\n\t\t| Database error: " + e.getMessage() + " |");
        }
    }

    public void deleteUser(){
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter Cin of the user to delete: ");
        String cin = scanner.nextLine();


        try {
            UserRepository userRepository = new UserRepository();
            // Check if the user exists before attempting to delete
            User user = userRepository.getUserByCin(cin);

            if (user != null) {
                // Delete the user from the database
                userRepository.deleteUser(user.getId());
                System.out.println("\n\t\t| Account deleted successfully |");
            } else {
                System.out.println("\n\t\t| User not found |");
            }
        } catch (SQLException e) {
            System.out.println("\n\t\t| Database error: " + e.getMessage() + " |");
        }
    }

    public void displayUserInfo(){
        System.out.print("\n\t\t-------------------\n");
        System.out.print("\t\tEnter Cin of the user to display: ");
        String cin = scanner.nextLine();


        try {
            UserRepository userRepository = new UserRepository();
            // Fetch the user from the database
            User user = userRepository.getUserByCin(cin);

            if (user != null) {
                // Display user information
                System.out.println("\n\t\t" + user);
            } else {
                System.out.println("\n\t\t| User not found |");
            }
        } catch (SQLException e) {
            System.out.println("\n\t\t| Database error: " + e.getMessage() + " |");
        }
    }
}
