import java.sql.*;
import java.util.Scanner;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;



public class Centralized {

    // ================= MYSQL CONFIG =================
    static final String MYSQL_URL = "jdbc:mysql://localhost:3306/estudy";
    static final String MYSQL_USER = "root";
    static final String MYSQL_PASS = "PranavKharage";

    // ================= MONGODB CONFIG =================
    static final String MONGO_URL = "mongodb://localhost:27017";
    static final String MONGO_DB = "estudy";
    static final String MONGO_COLLECTION = "subjects";

    // ================= LOGIN =================
    public static boolean login(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";

        try (Connection con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ User does not exist");
                return false;
            }

            if (!rs.getString("password").equals(password)) {
                System.out.println("❌ Incorrect password");
                return false;
            }

            return true;

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }

    // ================= REGISTER =================
    public static void register(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES (?, ?)";

        try (Connection con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();

            System.out.println("✅ Registration successful");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("❌ Username already exists");
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
        }
    }

    // ================= FETCH FROM MONGODB =================
   public static void showSubjectContent(String subjectName) {

    try (com.mongodb.client.MongoClient client =
         com.mongodb.client.MongoClients.create(MONGO_URL)) {

        com.mongodb.client.MongoDatabase db = client.getDatabase(MONGO_DB);
        com.mongodb.client.MongoCollection<org.bson.Document> collection =
                db.getCollection(MONGO_COLLECTION);

        org.bson.Document subject =
                collection.find(new org.bson.Document("subject", subjectName)).first();

        if (subject == null) {
            System.out.println("No content found");
            return;
        }

        if (subject == null) {
    System.out.println("No content found");
    return;
}

// ✅ CALL BROWSER METHOD HERE
openInBlankBrowserPage(subject);

         
        

    } catch (Exception e) {
        System.out.println("MongoDB error");
    }
}


    public static void openInBlankBrowserPage(org.bson.Document subject) {
    try {
        String html =
            "<html><head><title>Centralized E-Study Platform</title></head>" +
            "<body style='font-family:Arial; margin:40px; line-height:1.6;'>" +

            "<h2>Centralized E-Study Platform</h2><hr>" +
            "<h3>Subject: " + subject.getString("subject") + "</h3><hr>" +

            "<h3>📘 Concepts</h3>";

        for (org.bson.Document topic : subject.getList("topics", org.bson.Document.class)) {
            html += "<h4>" + topic.getString("topicName") + "</h4>";
            html += "<p><b>Theory:</b> " + topic.getString("theory") + "</p>";
            html += "<p><b>Example:</b> " + topic.getString("example") + "</p>";
            html += "<pre style='background:#f4f4f4;padding:10px;'>" +
                    topic.getString("code") + "</pre>";
        }

        html += "<h3>📊 Diagram Links</h3><ul>";
        for (org.bson.Document topic : subject.getList("topics", org.bson.Document.class)) {
            for (String link : topic.getList("diagrams", String.class)) {
                html += "<li><a href='" + link + "' target='_blank'>Open Diagram</a></li>";
            }
        }
        html += "</ul>";

        html += "<h3>❓ Questions & Answers</h3>";
        for (org.bson.Document q : subject.getList("questions", org.bson.Document.class)) {
            html += "<p><b>Q:</b> " + q.getString("question") +
                    "<br><b>A:</b> " + q.getString("answer") + "</p>";
        }

        html += "<h3>📺 YouTube References</h3><ul>";
        for (String yt : subject.getList("youtubeLinks", String.class)) {
            html += "<li><a href='" + yt + "' target='_blank'>Watch Video</a></li>";
        }
        html += "</ul></body></html>";

        // Create temp file
        java.io.File file = java.io.File.createTempFile("study-", ".html");
        try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
            writer.write(html);
        }

        // Open explicitly in Chrome
        Runtime.getRuntime().exec(
            "cmd /c start chrome \"" + file.getAbsolutePath() + "\""
        );

    } catch (Exception e) {
        System.out.println("Unable to open browser");
    }
}




    // ================= MAIN FLOW =================
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("----- Centralized E-Study Platform -----");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("Choose option: ");

        int choice = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (choice == 2) {
            register(username, password);
            return;
        }

        if (!login(username, password)) {
            System.out.println("Login failed");
            return;
        }

        System.out.println("✅ Login successful\n");

        System.out.println("\nSubjects available:");
        System.out.println("1. OOP");
        System.out.println("2. DBMS");
        System.out.println("3. SPOS");
        System.out.println("4. CNS");
        System.out.println("5. JAVA");

        System.out.print("Enter subject number: ");
        int subjectChoice = sc.nextInt();

        String subjectName;

        switch (subjectChoice) {
            case 1:
                subjectName = "OOP";
                break;
            case 2:
                subjectName = "DBMS";
                break;
            case 3:
                subjectName = "SPOS";
                break;
            case 4:
                subjectName = "CNS";
                break;
            case 5:
                subjectName = "JAVA";
                break;
            default:
                System.out.println("Invalid subject choice");
                return;
        }
        showSubjectContent(subjectName);


        sc.close();
    }
}
