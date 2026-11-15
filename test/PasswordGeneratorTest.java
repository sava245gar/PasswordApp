import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {

    @Test
    void length() {
        assertEquals(8, PasswordGenerator.generate(8).length());
        assertEquals(16, PasswordGenerator.generate(16).length());
        assertEquals(32, PasswordGenerator.generate(32).length());
    }

    @Test
    void minLength() {
        assertEquals(8, PasswordGenerator.generate(4).length());
    }

    @Test
    void alwaysStrong() {
        for (int i = 0; i < 100; i++) {
            assertEquals(PasswordChecker.STRONG, PasswordChecker.getLevel(PasswordGenerator.generate(12)));
        }
    }

    @Test
    void allCriteria() {
        assertEquals(6, PasswordChecker.getScore(PasswordGenerator.generate(16)));
    }

    @Test
    void differentPasswords() {
        assertNotEquals(PasswordGenerator.generate(16), PasswordGenerator.generate(16));
    }
}
