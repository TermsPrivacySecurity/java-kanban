package task;

import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void shouldBePositiveWhenTasksEqualsById() {
        Task task1 = new Task("Task1", "description1", 60, LocalDateTime.parse("01.01.2025 10:10", Task.formatter));
        Task task2 = new Task("Task2", "description2", 600, LocalDateTime.parse("01.02.2025 10:10", Task.formatter));
        task1.setId(1);
        task2.setId(1);
        Assertions.assertEquals(task1, task2);
    }

    @Test
    public void shouldBePositiveWhenGetTaskFromString() {
        Task task1 = new Task("Task1", "description1", 60, LocalDateTime.parse("01.01.2025 10:10", Task.formatter));
        Task task2 = Task.fromString(task1.toString());
        assertEquals(task1, task2);
    }
}