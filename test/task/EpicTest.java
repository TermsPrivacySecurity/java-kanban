package task;

import org.junit.jupiter.api.*;

class EpicTest {
    @Test
    public void shouldBePositiveWhenEpicsEqualsById() {
        Epic epic1 = new Epic("Epic 1", "description1");
        Epic epic2 = new Epic("Epic 2", "description2");
        epic1.setId(1);
        epic2.setId(1);
        Assertions.assertEquals(epic1, epic2);
    }
}