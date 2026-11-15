import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginWindow extends BaseWindow {

    static int fails = 0;
    static long blockedUntil = 0;

    JTextField loginField = field(new JTextField());
    JPasswordField passField = field(new JPasswordField());
    JButton loginBtn = button("Войти", true);
    JLabel msg = messageLabel();

    LoginWindow(String info) {
        super("PasswordApp — Авторизация");

        JPanel form = panel(new GridLayout(2, 1, 0, 12));
        form.add(withLabel("Логин", loginField));
        form.add(withLabel("Пароль", passField));

        JPanel formBox = panel(new BorderLayout(0, 8));
        formBox.add(form, BorderLayout.NORTH);
        formBox.add(showPassword(passField), BorderLayout.CENTER);

        JButton regBtn = new JButton("<html>Нет аккаунта? <b>Регистрация</b></html>");
        regBtn.setForeground(accent);
        regBtn.setContentAreaFilled(false);
        regBtn.setBorderPainted(false);
        regBtn.setFocusPainted(false);

        JPanel bottom = panel(new BorderLayout(0, 10));
        bottom.add(loginBtn, BorderLayout.NORTH);
        bottom.add(msg, BorderLayout.CENTER);
        bottom.add(regBtn, BorderLayout.SOUTH);

        root.add(header("Вход", "Войдите в свою учётную запись"), BorderLayout.NORTH);
        root.add(top(formBox), BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> login());
        regBtn.addActionListener(e -> {
            dispose();
            new RegisterWindow();
        });

        if (info != null) showMessage(msg, info, green);
        getRootPane().setDefaultButton(loginBtn);
        open(380);
        if (System.currentTimeMillis() < blockedUntil) block();
    }

    void login() {
        msg.setVisible(false);
        try {
            String login = loginField.getText().trim();
            String pass = new String(passField.getPassword());
            if (login.isEmpty() || pass.isEmpty()) {
                showError(msg, "Заполните логин и пароль");
            } else if (!UserStorage.hash(pass).equals(UserStorage.load().get(login))) {
                fails++;
                if (fails >= 3) {
                    fails = 0;
                    blockedUntil = System.currentTimeMillis() + 30000;
                    block();
                } else {
                    showError(msg, "Неверный логин или пароль. Осталось попыток: " + (3 - fails));
                }
            } else {
                fails = 0;
                dispose();
                if (login.equals(UserStorage.ADMIN)) new AdminWindow();
                else new CheckWindow(login);
            }
        } catch (IOException ex) {
            showError(msg, "Не удалось прочитать файл пользователей");
        } catch (Exception ex) {
            showError(msg, "Ошибка: " + ex.getMessage());
        }
    }

    void block() {
        loginBtn.setEnabled(false);
        loginBtn.setBackground(new Color(0xA5B4FC));
        Timer timer = new Timer(1000, null);
        timer.addActionListener(e -> {
            long left = (blockedUntil - System.currentTimeMillis() + 999) / 1000;
            if (left <= 0 || !isDisplayable()) {
                timer.stop();
                loginBtn.setEnabled(true);
                loginBtn.setBackground(accent);
                msg.setVisible(false);
            } else {
                showError(msg, "Слишком много неверных попыток. Вход заблокирован на " + left + " с");
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }
}
