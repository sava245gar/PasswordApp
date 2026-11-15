import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class BaseWindow extends JFrame {

    static Color accent = new Color(0x4F6BED);
    static Color gray = new Color(0x6B7280);
    static Color fieldBg = new Color(0xF9FAFB);
    static Color red = new Color(0xEF4444);
    static Color yellow = new Color(0xF59E0B);
    static Color green = new Color(0x22C55E);

    static Font font = new Font("Segoe UI", Font.PLAIN, 14);
    static Font bold = new Font("Segoe UI", Font.BOLD, 14);
    static Font small = new Font("Segoe UI", Font.PLAIN, 12);

    JPanel root;

    BaseWindow(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        root = new JPanel(new BorderLayout(0, 20));
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(24, 24, 24, 24));
        setContentPane(root);
    }

    void open(int width) {
        pack();
        setSize(width, getHeight());
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    static void showError(JLabel msg, String text) {
        showMessage(msg, text, red);
    }

    static void showMessage(JLabel msg, String text, Color color) {
        int width = msg.getParent().getWidth() > 0 ? (msg.getParent().getWidth() - 30) * 3 / 4 : 220;
        msg.setText("<html><div style='width:" + width + "px'>" + text + "</div></html>");
        msg.setForeground(color.darker().darker());
        msg.setBackground(mix(color));
        msg.setBorder(new CompoundBorder(new LineBorder(color, 1, true), new EmptyBorder(8, 12, 8, 12)));
        msg.setVisible(true);

        Window w = SwingUtilities.getWindowAncestor(msg);
        if (w != null && w.isVisible() && w.getPreferredSize().height > w.getHeight()) {
            w.setSize(w.getWidth(), w.getPreferredSize().height);
        }
        msg.getParent().revalidate();
    }

    static Color mix(Color c) {
        return new Color((c.getRed() + 255 * 5) / 6, (c.getGreen() + 255 * 5) / 6, (c.getBlue() + 255 * 5) / 6);
    }

    static void copy(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }

    static JPanel panel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(false);
        return p;
    }

    static JPanel top(JComponent c) {
        JPanel p = panel(new BorderLayout());
        p.add(c, BorderLayout.NORTH);
        return p;
    }

    static JPanel header(String title, String subtitle) {
        return header(title, subtitle, 220);
    }

    static JPanel header(String title, String subtitle, int width) {
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JLabel s = new JLabel("<html><div style='width:" + width + "px'>" + subtitle + "</div></html>");
        s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        s.setForeground(gray);

        JPanel p = panel(new BorderLayout(0, 4));
        p.add(t, BorderLayout.NORTH);
        p.add(s, BorderLayout.CENTER);
        return p;
    }

    static JPanel withLabel(String text, JComponent c) {
        JPanel p = panel(new BorderLayout(0, 5));
        p.add(label(text, SwingConstants.LEFT), BorderLayout.NORTH);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    static JLabel label(String text, int align) {
        JLabel l = new JLabel(text, align);
        l.setFont(small);
        l.setForeground(gray);
        return l;
    }

    static <T extends JTextField> T field(T f) {
        f.setFont(font);
        f.setBackground(fieldBg);
        f.setBorder(new CompoundBorder(new LineBorder(new Color(0xD1D5DB), 1, true), new EmptyBorder(8, 12, 8, 12)));
        return f;
    }

    static JCheckBox showPassword(JPasswordField... fields) {
        JCheckBox box = new JCheckBox("Показать пароль");
        box.setFont(font);
        box.setOpaque(false);
        box.setFocusPainted(false);
        char echo = fields[0].getEchoChar();
        box.addActionListener(e -> {
            for (JPasswordField f : fields) f.setEchoChar(box.isSelected() ? 0 : echo);
        });
        return box;
    }

    static JButton button(String text, boolean primary) {
        JButton b = new JButton(text);
        b.setFont(bold);
        b.setForeground(primary ? Color.WHITE : accent);
        b.setBackground(primary ? accent : Color.WHITE);
        b.setBorder(primary ? new EmptyBorder(10, 18, 10, 18)
                : new CompoundBorder(new LineBorder(new Color(0xC7D0F8), 1, true), new EmptyBorder(9, 17, 9, 17)));
        b.setContentAreaFilled(false);
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    static JLabel messageLabel() {
        JLabel l = new JLabel();
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setOpaque(true);
        l.setVisible(false);
        return l;
    }
}
