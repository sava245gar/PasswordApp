import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class AdminWindow extends BaseWindow {

    DefaultListModel<String> model = new DefaultListModel<>();
    JList<String> list = new JList<>(model);
    JLabel count = label("", SwingConstants.LEFT);
    JLabel msg = messageLabel();

    AdminWindow() {
        super("PasswordApp — Администратор");

        list.setFont(font);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(28);
        list.setBorder(new EmptyBorder(4, 10, 4, 10));
        JScrollPane scroll = new JScrollPane(list);
        scroll.setPreferredSize(new Dimension(0, 200));

        JPanel listPanel = panel(new BorderLayout(0, 6));
        listPanel.add(count, BorderLayout.NORTH);
        listPanel.add(scroll, BorderLayout.CENTER);

        JButton resetBtn = button("Сбросить пароль", false);
        JButton deleteBtn = button("Удалить", true);
        deleteBtn.setBackground(red);
        JPanel buttons = panel(new GridLayout(1, 2, 10, 0));
        buttons.add(resetBtn);
        buttons.add(deleteBtn);

        JPanel actions = panel(new BorderLayout(0, 10));
        actions.add(buttons, BorderLayout.NORTH);
        actions.add(msg, BorderLayout.CENTER);

        JPanel center = panel(new BorderLayout(0, 12));
        center.add(listPanel, BorderLayout.NORTH);
        center.add(actions, BorderLayout.CENTER);

        JButton logoutBtn = button("Выйти", false);
        JPanel bottom = panel(new BorderLayout());
        bottom.add(logoutBtn, BorderLayout.EAST);

        root.add(header("Администратор", "Управление учётными записями пользователей", 300), BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        refresh();

        deleteBtn.addActionListener(e -> delete());
        resetBtn.addActionListener(e -> reset());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginWindow(null);
        });

        open(420);
    }

    void refresh() {
        model.clear();
        try {
            for (String user : UserStorage.load().keySet()) {
                if (!user.equals(UserStorage.ADMIN)) model.addElement(user);
            }
        } catch (Exception ex) {
            showError(msg, "Не удалось прочитать файл пользователей");
        }
        count.setText("Пользователи: " + model.size());
    }

    void delete() {
        msg.setVisible(false);
        String user = list.getSelectedValue();
        if (user == null) {
            showError(msg, "Выберите пользователя в списке");
            return;
        }
        try {
            Map<String, String> users = UserStorage.load();
            users.remove(user);
            UserStorage.save(users);
            refresh();
            showMessage(msg, "Пользователь " + user + " удалён", green);
        } catch (Exception ex) {
            showError(msg, "Не удалось сохранить данные пользователя");
        }
    }

    void reset() {
        msg.setVisible(false);
        String user = list.getSelectedValue();
        if (user == null) {
            showError(msg, "Выберите пользователя в списке");
            return;
        }
        try {
            String pass = PasswordGenerator.generate(12);
            Map<String, String> users = UserStorage.load();
            users.put(user, UserStorage.hash(pass));
            UserStorage.save(users);
            copy(pass);
            showMessage(msg, "Новый пароль для " + user + ": <b>" + pass + "</b> (скопирован в буфер обмена)", green);
        } catch (Exception ex) {
            showError(msg, "Не удалось сохранить данные пользователя");
        }
    }
}
