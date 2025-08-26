package com.quizapp;

import java.sql.*;
import java.util.Scanner;

public class AdminService {

    // Create a new quiz and return generated quiz id
    public int createQuiz(Scanner sc, UserSession admin) {
        System.out.print("Enter quiz title: ");
        String title = sc.nextLine().trim();

        String query = "INSERT INTO quizzes (title, created_by, created_at) VALUES (?, ?, NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
            stmt.setInt(2, admin.getId());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet gk = stmt.getGeneratedKeys()) {
                    if (gk.next()) {
                        int quizId = gk.getInt(1);
                        System.out.println("✅ Quiz created with id: " + quizId);
                        return quizId;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("❌ Failed to create quiz.");
        return -1;
    }

    // Add a question to an existing quiz
    public void addQuestion(Scanner sc) {
        System.out.print("Enter quiz id to add question to: ");
        int quizId = Integer.parseInt(sc.nextLine().trim());

        System.out.print("Enter question text: ");
        String qtext = sc.nextLine().trim();

        System.out.print("Option A: ");
        String a = sc.nextLine().trim();
        System.out.print("Option B: ");
        String b = sc.nextLine().trim();
        System.out.print("Option C: ");
        String c = sc.nextLine().trim();
        System.out.print("Option D: ");
        String d = sc.nextLine().trim();

        String correct;
        while (true) {
            System.out.print("Correct option (A/B/C/D): ");
            correct = sc.nextLine().trim().toUpperCase();
            if (correct.matches("[ABCD]")) break;
            System.out.println("Enter A, B, C or D");
        }

        String query = "INSERT INTO questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            stmt.setString(2, qtext);
            stmt.setString(3, a);
            stmt.setString(4, b);
            stmt.setString(5, c);
            stmt.setString(6, d);
            stmt.setString(7, correct);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Question added.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // List quizzes created by this admin
    public void listMyQuizzes(UserSession admin) {
        String query = "SELECT id, title, created_at FROM quizzes WHERE created_by = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, admin.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Your quizzes:");
                while (rs.next()) {
                    System.out.printf("ID: %d | %s | created_at: %s%n",
                            rs.getInt("id"), rs.getString("title"), rs.getTimestamp("created_at"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View scores for a quiz
    public void viewQuizScores(Scanner sc) {
        System.out.print("Enter quiz id to view scores: ");
        int quizId = Integer.parseInt(sc.nextLine().trim());

        String query = "SELECT s.id, u.username, s.score, s.taken_at " +
                "FROM scores s JOIN users u ON s.user_id = u.id " +
                "WHERE s.quiz_id = ? ORDER BY s.taken_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Scores for quiz id " + quizId + ":");
                while (rs.next()) {
                    System.out.printf("User: %s | Score: %d | At: %s%n",
                            rs.getString("username"), rs.getInt("score"), rs.getTimestamp("taken_at"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
