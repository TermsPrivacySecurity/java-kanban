package task;

import org.junit.jupiter.api.*;

class SubtaskTest {
    @Test
    public void shouldBePositiveWhenSubtasksEqualsById(){
        Subtask subtask1 = new Subtask("Subtask1", "description1", Status.NEW, 1);
        Subtask subtask2 = new Subtask("Subtask2", "description1", Status.IN_PROGRESS, 2);
        subtask1.setId(1);
        subtask2.setId(1);
        Assertions.assertEquals(subtask1, subtask2);
    }
}