import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testFullConstructor() {
        User user = new User(1, "john_doe", "pass123", "John Doe", "admin");

        assertEquals(1, user.getUserId());
        assertEquals("john_doe", user.getUsername());
        assertEquals("pass123", user.getPassword());
        assertEquals("John Doe", user.getFullName());
        assertEquals("admin", user.getRole());
    }

    @Test
    public void testNewUserConstructor() {
        User user = new User("aymen_O", "aymen1234", "Oumous Aymen", "receptionniste", true);

        // userId is not set
        assertEquals("aymen_0", user.getUsername());
        assertEquals("aymen1234", user.getPassword());
        assertEquals("Oumous Aymen", user.getFullName());
        assertEquals("receptionniste", user.getRole());
    }

    @Test
    public void testSettersAndGetters() {
        User user = new User(0, "", "", "", "");

        user.setUserId(10);
        user.setUsername("alex");
        user.setPassword("newpass");
        user.setFullName("Alex Smith");
        user.setRole("admin");

        assertEquals(10, user.getUserId());
        assertEquals("alex", user.getUsername());
        assertEquals("newpass", user.getPassword());
        assertEquals("Alex Smith", user.getFullName());
        assertEquals("admin", user.getRole());
    }
}
