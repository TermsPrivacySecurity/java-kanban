package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    static FileBackedTaskManager taskManager;
    static Task task1;
    static Task task2;
    static Epic epic1;
    static Epic epic2;
    static Subtask subtask1;
    static Subtask subtask2;
    static Subtask subtask3;

    @BeforeEach
    public void beforeEach() throws IOException {
        taskManager = new FileBackedTaskManager(File.createTempFile("TaskManager", ".txt"));
        task1 = new Task("task1", "description1", Status.NEW);
        taskManager.addNewTask(task1);
        task2 = new Task("task2", "description2", Status.IN_PROGRESS);
        taskManager.addNewTask(task2);
        epic1 = new Epic("epic1", "description1");
        taskManager.addNewEpic(epic1);
        epic2 = new Epic("epic2", "description2");
        taskManager.addNewEpic(epic2);
        subtask1 = new Subtask("subtask1", "description1", Status.NEW, epic1.getId());
        taskManager.addNewSubtask(subtask1);
        subtask2 = new Subtask("subtask2", "description2", Status.IN_PROGRESS, epic1.getId());
        taskManager.addNewSubtask(subtask2);
        subtask3 = new Subtask("subtask3", "description3", Status.NEW, epic1.getId());
        taskManager.addNewSubtask(subtask3);
    }

    @Test
    public void shouldBePositiveIfLoadIsCorrect() {
        FileBackedTaskManager newTaskManager = new FileBackedTaskManager(taskManager.getFile());
        assertTrue(newTaskManager.getTasksList().isEmpty());
        assertTrue(newTaskManager.getEpicsList().isEmpty());
        assertTrue(newTaskManager.getSubtasksList().isEmpty());
        newTaskManager.load();
        assertEquals(taskManager.getTasksList(), newTaskManager.getTasksList());
        assertEquals(taskManager.getEpicsList(), newTaskManager.getEpicsList());
        assertEquals(taskManager.getSubtasksList(), newTaskManager.getSubtasksList());
    }
}