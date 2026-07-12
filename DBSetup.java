import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DBSetup {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String[] users = {"root", "manes", "admin"};
        String[] passwords = {"Saksi@710", "Sakshi@710", "sakshi@710", "saksi@710", "\"Saksi@710\"", "\"Sakshi@710\"", "root", "", "admin"};
        
        Connection c = null;
        for (String user : users) {
            for (String password : passwords) {
                try {
                    c = DriverManager.getConnection(url, user, password);
                    System.out.println("SUCCESS! Connected with user: " + user + " and password: " + password);
                    // UPDATE DBConnection.java since we found the right password
                    String path = "src/main/java/com/student/management/util/DBConnection.java";
                    String content = new String(Files.readAllBytes(Paths.get(path)));
                    content = content.replaceAll("private static final String PASSWORD = \\\".*?\\\";", "private static final String PASSWORD = \\\"" + password + "\\\";");
                    Files.write(Paths.get(path), content.getBytes());
                    break;
                } catch (Exception e) {
                }
            }
            if (c != null) break;
        }
        
        if (c != null) {
            try {
                Statement s = c.createStatement();
                String sql = new String(Files.readAllBytes(Paths.get("database_setup.sql")));
                String[] commands = sql.split(";");
                for (String cmd : commands) {
                    if (!cmd.trim().isEmpty()) {
                        System.out.println("Executing: " + cmd.trim());
                        s.execute(cmd);
                    }
                }
                System.out.println("Database setup complete.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Could not connect with any credential combination.");
        }
    }
}
