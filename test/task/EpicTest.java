package task;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    @Test
    public void shouldBePositiveWhenEpicsEqualsById() {
        Epic epic1 = new Epic("Epic 1", "description1");
        Epic epic2 = new Epic("Epic 2", "description2");
        epic1.setId(1);
        epic2.setId(1);
        Assertions.assertEquals(epic1, epic2);
    }

    @Test
    public void shouldBePositiveWhenGetEpicFromString() {
        Epic epic1 = new Epic("Task1", "description1");
        Epic epic2 = Epic.fromString(epic1.toString());
        assertEquals(epic1, epic2);
    }
}