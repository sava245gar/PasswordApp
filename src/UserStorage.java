import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserStorage {

    static final String USERS_FILE = "users.txt";
    static final String ADMIN = "admin";
    static final String ADMIN_PASS = "Admin2026!";

    static Map<String, String> load() throws Exception {
        Map<String, String> users = new LinkedHashMap<>();
        users.put(ADMIN, hash(ADMIN_PASS));
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(USERS_FILE), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(";", 2);
                if (parts.length == 2) users.put(parts[0], parts[1]);
            }
        } catch (FileNotFoundException e) {
            return users;
        }
        return users;
    }

    static void save(Map<String, String> users) throws IOException {
        try (PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(USERS_FILE), StandardCharsets.UTF_8))) {
            for (String login : users.keySet()) {
                w.println(login + ";" + users.get(login));
            }
        }
    }

    static String hash(String pass) throws Exception {
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(pass.getBytes(StandardCharsets.UTF_8));
        String hex = "";
        for (byte b : hash) hex += String.format("%02x", b);
        return hex;
    }
}
