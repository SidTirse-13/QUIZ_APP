package com.quizapp;

import java.sql.*;
import java.util.Scanner;

public class QuizService {

    // List all quizzes
    public void listQuizzes() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT q.id, q.title, u.username, q.created_at " +
                    "FROM quizzes q JOIN users u ON q.created_by = u.id";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            System.out.println("Available quizzes:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String createdBy = rs.getString("username");
                String createdAt = rs.getString("created_at");

                System.out.println("ID: " + id + " | " + title +
                        " | by: " + createdBy +
                        " | " + createdAt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Take quiz
    public void takeQuiz(Scanner sc, int quizId, String username) {
        try (Connection conn = DBConnection.getConnection()) {
            // Get user id
            PreparedStatement userStmt = conn.prepareStatement("SELECT id FROM users WHERE username = ?");
            userStmt.setString(1, username);
            ResultSet userRs = userStmt.executeQuery();
            if (!userRs.next()) {
                System.out.println("User not found!");
                return;
            }
            int userId = userRs.getInt("id");

            // Get questions
            String query = "SELECT * FROM questions WHERE quiz_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, quizId);
            ResultSet rs = stmt.executeQuery();

            int score = 0;
            int total = 0;

            while (rs.next()) {
                total++;
                String question = rs.getString("question_text");
                String a = rs.getString("option_a");
                String b = rs.getString("option_b");
                String c = rs.getString("option_c");
                String d = rs.getString("option_d");
                String correct = rs.getString("correct_option");

                System.out.println("\nQ" + total + ": " + question);
                System.out.println("A) " + a);
                System.out.println("B) " + b);
                System.out.println("C) " + c);
                System.out.println("D) " + d);
                System.out.print("Your answer: ");
                String ans = sc.nextLine().trim().toUpperCase();

                if (ans.equals(correct)) {
                    System.out.println("✅ Correct!");
                    score++;
                } else {
                    System.out.println("❌ Wrong! Correct answer is: " + correct);
                }
            }

            System.out.println("\nYour Score: " + score + "/" + total);

            // Save score
            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO scores (user_id, quiz_id, score) VALUES (?, ?, ?)"
            );
            insertStmt.setInt(1, userId);
            insertStmt.setInt(2, quizId);
            insertStmt.setInt(3, score);
            insertStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
