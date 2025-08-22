package task;

import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void shouldBePositiveWhenTasksEqualsById() {
        Task task1 = new Task("Task1", "description1", 60,
                LocalDateTime.parse("2025-01-01T10:00:00"));
        Task task2 = new Task("Task2", "description2", 600,
                LocalDateTime.parse("2025-02-01T10:10:00"));
        task1.setId(1);
        task2.setId(1);
        Assertions.assertEquals(task1, task2);
    }

    @Test
    public void shouldBePositiveWhenGetTaskFromString() {
        Task task1 = new Task("Task1", "description1", 60,
                LocalDateTime.parse("2025-01-01T10:10:00"));
        Task task2 = Task.fromString(task1.toString());
        assertEquals(task1, task2);
    }
}