import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PasswordCheckerTest {

    @Test
    void emptyPassword() {
        assertEquals(0, PasswordChecker.getScore(""));
        assertEquals(PasswordChecker.WEAK, PasswordChecker.getLevel(""));
        assertEquals("Пароль не введён", PasswordChecker.checkPassword(""));
    }

    @Test
    void nullPassword() {
        assertEquals(0, PasswordChecker.getScore(null));
        assertEquals("Пароль не введён", PasswordChecker.checkPassword(null));
    }

    @Test
    void weakPassword() {
        assertEquals(1, PasswordChecker.getScore("abc"));
        assertEquals(PasswordChecker.WEAK, PasswordChecker.getLevel("abc"));
    }

    @Test
    void mediumPassword() {
        assertEquals(4, PasswordChecker.getScore("Password1"));
        assertEquals(PasswordChecker.MEDIUM, PasswordChecker.getLevel("Password1"));
    }

    @Test
    void strongPassword() {
        assertEquals(6, PasswordChecker.getScore("Gr7#mPx!2qLs"));
        assertEquals(PasswordChecker.STRONG, PasswordChecker.getLevel("Gr7#mPx!2qLs"));
    }

    @Test
    void lengthBoundaries() {
        assertEquals(4, PasswordChecker.getScore("aB1!xyz"));
        assertEquals(5, PasswordChecker.getScore("aB1!xyzw"));
        assertEquals(5, PasswordChecker.getScore("aB1!xyzwqrs"));
        assertEquals(6, PasswordChecker.getScore("aB1!xyzwqrst"));
    }

    @Test
    void onlyDigits() {
        assertEquals(2, PasswordChecker.getScore("12345678"));
        assertEquals(PasswordChecker.WEAK, PasswordChecker.getLevel("12345678"));
    }

    @Test
    void onlySpecialSymbols() {
        assertEquals(2, PasswordChecker.getScore("!@#$%^&*"));
        assertEquals(PasswordChecker.WEAK, PasswordChecker.getLevel("!@#$%^&*"));
    }

    @Test
    void onlySpaces() {
        assertEquals(1, PasswordChecker.getScore("        "));
        assertEquals(PasswordChecker.WEAK, PasswordChecker.getLevel("        "));
    }

    @Test
    void cyrillicPassword() {
        assertEquals(5, PasswordChecker.getScore("Пароль2024!"));
        assertEquals(PasswordChecker.STRONG, PasswordChecker.getLevel("Пароль2024!"));
    }

    @Test
    void commonPassword() {
        assertTrue(PasswordChecker.isCommon("qwerty123"));
        assertTrue(PasswordChecker.isCommon("QWERTY"));
        assertFalse(PasswordChecker.isCommon("Password1"));
        assertEquals(PasswordChecker.WEAK, PasswordChecker.getLevel("qwerty123"));
    }

    @Test
    void repeatedSymbols() {
        assertTrue(PasswordChecker.hasRepeat("aaaa1111"));
        assertFalse(PasswordChecker.hasRepeat("aaa111"));
        assertEquals(PasswordChecker.WEAK, PasswordChecker.getLevel("Xaaaa#1yZ"));
    }

    @Test
    void sequences() {
        assertTrue(PasswordChecker.hasSequence("abcd1234"));
        assertTrue(PasswordChecker.hasSequence("Qwer4321!x"));
        assertFalse(PasswordChecker.hasSequence("Gr7#mPx!2qLs"));
        assertEquals(PasswordChecker.WEAK, PasswordChecker.getLevel("Qwer4321!x"));
    }

    @Test
    void recommendations() {
        String text = PasswordChecker.checkPassword("password");
        assertTrue(text.contains("Добавьте: цифры"));
        assertTrue(text.contains("Добавьте: заглавные буквы"));
        assertTrue(text.contains("Предупреждения"));
        assertTrue(PasswordChecker.checkPassword("Gr7#mPx!2qLs").contains("Пароль соответствует всем критериям"));
    }

    @ParameterizedTest
    @CsvSource({
            "abc, СЛАБЫЙ",
            "abcdefgh, СЛАБЫЙ",
            "Abcdefgh, СЛАБЫЙ",
            "Kotik2026, СРЕДНИЙ",
            "Kotik_mur, СРЕДНИЙ",
            "Kotik_2026, НАДЁЖНЫЙ",
            "Gr7#mPx!2qLs, НАДЁЖНЫЙ",
            "Пароль2024!, НАДЁЖНЫЙ",
            "123456, СЛАБЫЙ",
            "Zx9$Zx9$Zx9$, НАДЁЖНЫЙ"
    })
    void levels(String password, String level) {
        assertEquals(level, PasswordChecker.getLevel(password));
    }
}
