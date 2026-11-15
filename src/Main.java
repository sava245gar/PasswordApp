import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        UIManager.put("swing.boldMetal", false);
        SwingUtilities.invokeLater(() -> new LoginWindow(null));
    }
}