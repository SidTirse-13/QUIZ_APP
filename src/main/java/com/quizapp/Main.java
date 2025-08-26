package com.quizapp;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        QuizService quizService = new QuizService();

        boolean running = true;

        while (running) {
            System.out.println("=== Welcome to Quiz App ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> userService.register(sc);
                case 2 -> {
                    UserSession session = userService.login(sc);
                    if (session != null) {
                        boolean isLoggedIn = true;
                        while (isLoggedIn) {
                            System.out.println("\n=== Logged in as: " + session.getUsername() + " (" + session.getRole() + ") ===");
                            System.out.println("1. List Quizzes");
                            System.out.println("2. Take Quiz");
                            System.out.println("3. View My Scores");
                            System.out.println("4. Logout");
                            System.out.print("Choose option: ");
                            int userChoice = sc.nextInt();
                            sc.nextLine(); // consume newline

                            switch (userChoice) {
                                case 1 -> quizService.listQuizzes();
                                case 2 -> {
                                    System.out.print("Enter quiz id to take: ");
                                    int quizId = sc.nextInt();
                                    sc.nextLine(); // consume newline
                                    quizService.takeQuiz(sc, quizId, session.getUsername());
                                }
                                case 3 -> userService.viewScores(session.getUsername());
                                case 4 -> isLoggedIn = false;
                                default -> System.out.println("Invalid option!");
                            }
                        }
                    }
                }
                case 3 -> running = false;
                default -> System.out.println("Invalid option!");
            }
        }

        sc.close();
    }
}
