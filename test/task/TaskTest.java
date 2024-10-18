package task;

import org.junit.jupiter.api.*;

class TaskTest {
    @Test
    public void shouldBePositiveWhenTasksEqualsById(){
        Task task1 = new Task ("Task1", "description1", Status.NEW);
        Task task2 = new Task ("Task2", "description2", Status.DONE);
        task1.setId(1);
        task2.setId(1);
        Assertions.assertEquals(task1, task2);
    }
}