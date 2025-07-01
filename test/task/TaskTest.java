package task;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void shouldBePositiveWhenTasksEqualsById() {
        Task task1 = new Task("Task1", "description1", Status.NEW);
        Task task2 = new Task("Task2", "description2", Status.DONE);
        task1.setId(1);
        task2.setId(1);
        Assertions.assertEquals(task1, task2);
    }

    @Test
    public void shouldBePositiveWhenGetTaskFromString() {
        Task task1 = new Task("Task1", "description1", Status.NEW);
        Task task2 = Task.fromString(task1.toString());
        assertEquals(task1, task2);
    }
}