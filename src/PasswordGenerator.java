import java.security.SecureRandom;

public class PasswordGenerator {

    static String lower = "abcdefghijkmnopqrstuvwxyz";
    static String upper = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    static String digits = "23456789";
    static String special = "!@#$%^*_-+=?";

    static SecureRandom random = new SecureRandom();

    public static String generate(int length) {
        if (length < 8) length = 8;
        String pass;
        do {
            pass = make(length);
        } while (!PasswordChecker.getLevel(pass).equals(PasswordChecker.STRONG));
        return pass;
    }

    static String make(int length) {
        String all = lower + upper + digits + special;
        char[] p = new char[length];
        p[0] = pick(lower);
        p[1] = pick(upper);
        p[2] = pick(digits);
        p[3] = pick(special);
        for (int i = 4; i < length; i++) p[i] = pick(all);

        for (int i = length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char t = p[i];
            p[i] = p[j];
            p[j] = t;
        }
        return new String(p);
    }

    static char pick(String s) {
        return s.charAt(random.nextInt(s.length()));
    }
}
