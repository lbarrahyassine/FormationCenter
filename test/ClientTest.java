import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    private Client client;
    private Date registrationDate;

    @BeforeEach
    public void setUp() {
        registrationDate = new Date();
        client = new Client("John", "Doe", "john@example.com", "1234567890", registrationDate);
    }

    @Test
    public void testConstructorWithoutId() {
        assertEquals("John", client.getFirstname());
        assertEquals("Doe", client.getLastname());
        assertEquals("john@example.com", client.getEmail());
        assertEquals("1234567890", client.getPhoneNumber());
        assertEquals(registrationDate, client.getRegistrationDate());
    }

    @Test
    public void testConstructorWithId() {
        Client clientWithId = new Client(1, "Jane", "Smith", "jane@example.com", "0987654321", registrationDate);
        assertEquals(1, clientWithId.getClientId());
        assertEquals("Jane", clientWithId.getFirstname());
        assertEquals("Smith", clientWithId.getLastname());
        assertEquals("jane@example.com", clientWithId.getEmail());
        assertEquals("0987654321", clientWithId.getPhoneNumber());
        assertEquals(registrationDate, clientWithId.getRegistrationDate());
    }

    @Test
    public void testSettersAndGetters() {
        client.setClientId(2);
        client.setFirstname("Alice");
        client.setLastname("Johnson");
        client.setEmail("alice@example.com");
        client.setPhoneNumber("1112223333");
        Date newDate = new Date();
        client.setRegistrationDate(newDate);

        assertEquals(2, client.getClientId());
        assertEquals("Alice", client.getFirstname());
        assertEquals("Johnson", client.getLastname());
        assertEquals("alice@example.com", client.getEmail());
        assertEquals("1112223333", client.getPhoneNumber());
        assertEquals(newDate, client.getRegistrationDate());
    }

    @Test
    public void testToString() {
        assertEquals("John Doe", client.toString());
    }
}
