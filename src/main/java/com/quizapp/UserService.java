package com.quizapp;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.Scanner;

public class UserService {

    // Register user
    public void register(Scanner sc) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter username: ");
            String username = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            String query = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, 'USER')";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Registration successful!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Login user and return UserSession
    public UserSession login(Scanner sc) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter username: ");
            String username = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            String query = "SELECT id, password_hash, role FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                String storedHash = rs.getString("password_hash");
                String role = rs.getString("role");

                if (BCrypt.checkpw(password, storedHash)) {
                    System.out.println("✅ Login successful! Welcome " + username + " (" + role + ")");
                    return new UserSession(userId, username, role);
                } else {
                    System.out.println("❌ Invalid password.");
                }
            } else {
                System.out.println("❌ User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // View scores for logged-in user
    public void viewScores(String username) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT q.title, s.score, s.taken_at " +
                    "FROM scores s JOIN quizzes q ON s.quiz_id = q.id " +
                    "JOIN users u ON s.user_id = u.id " +
                    "WHERE u.username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            System.out.println("=== Your Scores ===");
            while (rs.next()) {
                String quizTitle = rs.getString("title");
                int score = rs.getInt("score");
                String takenAt = rs.getString("taken_at");
                System.out.println(quizTitle + " | Score: " + score + " | Taken at: " + takenAt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
