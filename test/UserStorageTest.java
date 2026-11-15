import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserStorageTest {

    @Test
    void knownHash() throws Exception {
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", UserStorage.hash("abc"));
    }

    @Test
    void hashLength() throws Exception {
        assertEquals(64, UserStorage.hash("Qwerty1").length());
        assertEquals(64, UserStorage.hash("Пароль2024!").length());
    }

    @Test
    void sameHash() throws Exception {
        assertEquals(UserStorage.hash("Qwerty1"), UserStorage.hash("Qwerty1"));
    }

    @Test
    void differentHash() throws Exception {
        assertNotEquals(UserStorage.hash("Qwerty1"), UserStorage.hash("qwerty1"));
    }

    @Test
    void adminExists() throws Exception {
        assertEquals(UserStorage.hash(UserStorage.ADMIN_PASS), UserStorage.load().get(UserStorage.ADMIN));
    }
}
