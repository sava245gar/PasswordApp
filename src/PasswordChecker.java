public class PasswordChecker {

    public static final String WEAK = "СЛАБЫЙ";
    public static final String MEDIUM = "СРЕДНИЙ";
    public static final String STRONG = "НАДЁЖНЫЙ";

    static String[] criteria = {
            "Длина не менее 8 символов",
            "Длина не менее 12 символов",
            "Строчные буквы",
            "Заглавные буквы",
            "Цифры",
            "Спецсимволы"
    };

    static String[] common = {
            "123456", "12345678", "123456789", "1234567890", "111111", "000000",
            "qwerty", "qwerty123", "qwertyuiop", "123qwe", "1q2w3e4r", "1q2w3e4r5t", "zaq12wsx",
            "password", "password123", "admin", "admin123", "letmein", "welcome", "iloveyou",
            "abc123", "monkey", "dragon", "football", "sunshine", "master",
            "йцукен", "пароль", "привет"
    };

    static boolean[] check(String p) {
        if (p == null) p = "";
        boolean lower = false, upper = false, digit = false, special = false;
        for (char c : p.toCharArray()) {
            if (Character.isLowerCase(c)) lower = true;
            else if (Character.isUpperCase(c)) upper = true;
            else if (Character.isDigit(c)) digit = true;
            else if (!Character.isWhitespace(c)) special = true;
        }
        return new boolean[]{p.length() >= 8, p.length() >= 12, lower, upper, digit, special};
    }

    public static boolean isCommon(String p) {
        if (p == null) return false;
        for (String c : common) {
            if (c.equals(p.toLowerCase())) return true;
        }
        return false;
    }

    public static boolean hasRepeat(String p) {
        if (p == null) return false;
        for (int i = 0; i + 3 < p.length(); i++) {
            char c = p.charAt(i);
            if (p.charAt(i + 1) == c && p.charAt(i + 2) == c && p.charAt(i + 3) == c) return true;
        }
        return false;
    }

    public static boolean hasSequence(String p) {
        if (p == null) return false;
        String s = p.toLowerCase();
        for (int i = 0; i + 3 < s.length(); i++) {
            char a = s.charAt(i), b = s.charAt(i + 1), c = s.charAt(i + 2), d = s.charAt(i + 3);
            if (!Character.isLetterOrDigit(a) || !Character.isLetterOrDigit(d)) continue;
            if (b == a + 1 && c == b + 1 && d == c + 1) return true;
            if (b == a - 1 && c == b - 1 && d == c - 1) return true;
        }
        return false;
    }

    public static int getScore(String p) {
        int score = 0;
        for (boolean ok : check(p)) {
            if (ok) score++;
        }
        return score;
    }

    public static String getLevel(String p) {
        int score = getScore(p);
        if (score <= 2 || isCommon(p) || hasRepeat(p) || hasSequence(p)) return WEAK;
        if (score <= 4) return MEDIUM;
        return STRONG;
    }

    public static String checkPassword(String p) {
        if (p == null || p.isEmpty()) return "Пароль не введён";

        boolean[] res = check(p);
        String text = "Оценка по критериям:\n";
        for (int i = 0; i < criteria.length; i++) {
            text += (res[i] ? "  [+] " : "  [-] ") + criteria[i] + "\n";
        }

        String warn = "";
        if (isCommon(p)) warn += "  • Пароль есть в списке популярных паролей\n";
        if (hasRepeat(p)) warn += "  • Один символ повторяется 4 раза подряд\n";
        if (hasSequence(p)) warn += "  • Есть простая последовательность (1234, abcd)\n";
        if (!warn.isEmpty()) text += "\nПредупреждения:\n" + warn;

        text += "\nБаллы: " + getScore(p) + " из 6";
        text += "\nУровень: " + getLevel(p);
        text += "\n\nРекомендации:\n";

        if (getScore(p) == 6 && warn.isEmpty()) {
            text += "  • Пароль соответствует всем критериям\n";
        } else {
            for (int i = 0; i < criteria.length; i++) {
                if (!res[i]) text += "  • Добавьте: " + criteria[i].toLowerCase() + "\n";
            }
            if (!warn.isEmpty()) text += "  • Не используйте популярные пароли, повторы и последовательности\n";
        }
        return text;
    }
}
