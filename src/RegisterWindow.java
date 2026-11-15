import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

public class RegisterWindow extends BaseWindow {

    JTextField loginField = field(new JTextField());
    JPasswordField passField = field(new JPasswordField());
    JPasswordField pass2Field = field(new JPasswordField());
    JLabel msg = messageLabel();
    String weakPass = null;

    RegisterWindow() {
        super("PasswordApp — Регистрация");

        JPanel form = panel(new GridLayout(3, 1, 0, 12));
        form.add(withLabel("Логин", loginField));
        form.add(withLabel("Пароль", passField));
        form.add(withLabel("Повторите пароль", pass2Field));

        JPanel formBox = panel(new BorderLayout(0, 8));
        formBox.add(form, BorderLayout.NORTH);
        formBox.add(showPassword(passField, pass2Field), BorderLayout.CENTER);

        JButton backBtn = button("Назад", false);
        JButton createBtn = button("Создать", true);
        JPanel buttons = panel(new GridLayout(1, 2, 10, 0));
        buttons.add(backBtn);
        buttons.add(createBtn);

        JPanel bottom = panel(new BorderLayout(0, 10));
        bottom.add(buttons, BorderLayout.NORTH);
        bottom.add(msg, BorderLayout.CENTER);

        root.add(header("Регистрация", "Логин должен быть уникальным, пароли — совпадать"), BorderLayout.NORTH);
        root.add(top(formBox), BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        createBtn.addActionListener(e -> create());
        backBtn.addActionListener(e -> {
            dispose();
            new LoginWindow(null);
        });

        getRootPane().setDefaultButton(createBtn);
        open(380);
    }

    void create() {
        msg.setVisible(false);
        try {
            String login = loginField.getText().trim();
            String pass = new String(passField.getPassword());
            String pass2 = new String(pass2Field.getPassword());
            Map<String, String> users = UserStorage.load();

            if (login.isEmpty() || pass.isEmpty()) {
                showError(msg, "Заполните все поля");
            } else if (login.contains(";")) {
                showError(msg, "Логин не должен содержать символ «;»");
            } else if (users.containsKey(login)) {
                showError(msg, "Пользователь с таким логином уже существует");
            } else if (!pass.equals(pass2)) {
                showError(msg, "Пароли не совпадают");
            } else if (PasswordChecker.getLevel(pass).equals(PasswordChecker.WEAK) && !pass.equals(weakPass)) {
                weakPass = pass;
                showMessage(msg, "Пароль слабый. Рекомендуется использовать более надёжный пароль. "
                        + "Нажмите «Создать» ещё раз, чтобы продолжить", yellow);
            } else {
                users.put(login, UserStorage.hash(pass));
                UserStorage.save(users);
                dispose();
                new LoginWindow("Учётная запись создана. Войдите в систему");
            }
        } catch (IOException ex) {
            showError(msg, "Не удалось сохранить данные пользователя");
        } catch (Exception ex) {
            showError(msg, "Ошибка: " + ex.getMessage());
        }
    }
}
