package task;

import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {

    @Test
    public void shouldBePositiveWhenSubtasksEqualsById() {
        Subtask subtask1 = new Subtask("Subtask1", "description1", 1, 60, LocalDateTime.parse("01.01.2025 10:10", Task.formatter));
        Subtask subtask2 = new Subtask("Subtask2", "description1", 2, 600, LocalDateTime.parse("01.02.2025 10:10", Task.formatter));
        subtask1.setId(1);
        subtask2.setId(1);
        Assertions.assertEquals(subtask1, subtask2);
    }

    @Test
    public void shouldBePositiveWhenGetSubtaskFromString() {
        Subtask subtask1 = new Subtask("Task1", "description1", 0, 60, LocalDateTime.parse("01.01.2025 10:10", Task.formatter));
        Subtask subtask2 = Subtask.fromString(subtask1.toString());
        assertEquals(subtask1, subtask2);
    }
}