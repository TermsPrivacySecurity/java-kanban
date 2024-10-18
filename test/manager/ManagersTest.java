package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {


    @Test
    public void shouldBeNotNullWhenCreated(){
        assertNotNull(Managers.getDefault());
        assertNotNull(Managers.getDefaultHistory());
    }
}