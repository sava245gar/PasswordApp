import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class CheckWindow extends BaseWindow {

    JPasswordField passField = field(new JPasswordField());
    JSpinner length = new JSpinner(new SpinnerNumberModel(16, 8, 32, 1));
    JLabel level = new JLabel("—");
    JProgressBar scale = new JProgressBar(0, 6);
    JTextArea result = new JTextArea();
    JLabel msg = messageLabel();

    CheckWindow(String login) {
        super("PasswordApp — Проверка пароля (" + login + ")");

        length.setFont(font);
        JButton genBtn = button("Сгенерировать", false);
        JButton checkBtn = button("Проверить", true);

        JPanel lengthBox = panel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        lengthBox.add(label("Длина", SwingConstants.LEFT));
        lengthBox.add(length);

        JPanel options = panel(new BorderLayout());
        options.add(showPassword(passField), BorderLayout.WEST);
        options.add(lengthBox, BorderLayout.EAST);

        JPanel buttons = panel(new GridLayout(1, 2, 10, 0));
        buttons.add(genBtn);
        buttons.add(checkBtn);

        JPanel inputs = panel(new BorderLayout(0, 10));
        inputs.add(withLabel("Пароль", passField), BorderLayout.NORTH);
        inputs.add(options, BorderLayout.CENTER);
        inputs.add(buttons, BorderLayout.SOUTH);

        JPanel north = panel(new BorderLayout(0, 20));
        north.add(header("Проверка пароля", "Введите пароль, чтобы оценить его надёжность", 330), BorderLayout.NORTH);
        north.add(inputs, BorderLayout.CENTER);

        level.setFont(bold);
        JPanel levelRow = panel(new BorderLayout());
        levelRow.add(label("Уровень надёжности", SwingConstants.LEFT), BorderLayout.WEST);
        levelRow.add(level, BorderLayout.EAST);

        scale.setBorderPainted(false);
        scale.setBackground(new Color(0xE5E7EB));
        scale.setPreferredSize(new Dimension(0, 10));

        JPanel scaleText = panel(new GridLayout(1, 3));
        scaleText.add(label(PasswordChecker.WEAK, SwingConstants.LEFT));
        scaleText.add(label(PasswordChecker.MEDIUM, SwingConstants.CENTER));
        scaleText.add(label(PasswordChecker.STRONG, SwingConstants.RIGHT));

        JPanel status = panel(new BorderLayout(0, 6));
        status.add(levelRow, BorderLayout.NORTH);
        status.add(scale, BorderLayout.CENTER);
        status.add(scaleText, BorderLayout.SOUTH);

        result.setEditable(false);
        result.setLineWrap(true);
        result.setWrapStyleWord(true);
        result.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        result.setBackground(fieldBg);
        result.setBorder(new EmptyBorder(10, 12, 10, 12));
        JScrollPane scroll = new JScrollPane(result);
        scroll.setPreferredSize(new Dimension(0, 230));

        JPanel resultPanel = panel(new BorderLayout(0, 6));
        resultPanel.add(label("Результат проверки", SwingConstants.LEFT), BorderLayout.NORTH);
        resultPanel.add(scroll, BorderLayout.CENTER);

        JPanel center = panel(new BorderLayout(0, 12));
        center.add(status, BorderLayout.NORTH);
        center.add(msg, BorderLayout.CENTER);
        center.add(resultPanel, BorderLayout.SOUTH);

        JButton logoutBtn = button("Выйти", false);
        JPanel bottom = panel(new BorderLayout());
        bottom.add(logoutBtn, BorderLayout.EAST);

        root.add(north, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        passField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            public void removeUpdate(DocumentEvent e) {
                update();
            }

            public void changedUpdate(DocumentEvent e) {
                update();
            }
        });

        checkBtn.addActionListener(e -> {
            update();
            if (passField.getPassword().length == 0) showError(msg, "Введите пароль для проверки");
        });
        genBtn.addActionListener(e -> generate());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginWindow(null);
        });

        getRootPane().setDefaultButton(checkBtn);
        open(520);
    }

    void update() {
        msg.setVisible(false);
        String pass = new String(passField.getPassword());
        if (pass.isEmpty()) {
            scale.setValue(0);
            level.setText("—");
            level.setForeground(gray);
            result.setText("");
            return;
        }
        String lvl = PasswordChecker.getLevel(pass);
        Color color = lvl.equals(PasswordChecker.WEAK) ? red : lvl.equals(PasswordChecker.MEDIUM) ? yellow : green;

        scale.setValue(PasswordChecker.getScore(pass));
        scale.setForeground(color);
        level.setText(lvl);
        level.setForeground(color);
        result.setText(PasswordChecker.checkPassword(pass));
        result.setCaretPosition(0);

        if (lvl.equals(PasswordChecker.WEAK)) showMessage(msg, "Внимание: пароль слабый и может быть быстро подобран", yellow);
        else if (lvl.equals(PasswordChecker.MEDIUM)) showMessage(msg, "Пароль средней надёжности, его стоит усилить", yellow);
        else showMessage(msg, "Пароль надёжный", green);
    }

    void generate() {
        String pass = PasswordGenerator.generate((Integer) length.getValue());
        passField.setText(pass);
        copy(pass);
        showMessage(msg, "Пароль сгенерирован и скопирован в буфер обмена", green);
    }
}
