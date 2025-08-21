package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Override
    @BeforeEach
    public void beforeEach() throws IOException {
        taskManager = new FileBackedTaskManager(File.createTempFile("TaskManager", ".txt"));
        super.beforeEach();
    }

    @Test
    public void shouldBePositiveIfLoadIsCorrect() {
        FileBackedTaskManager newTaskManager = FileBackedTaskManager.load(taskManager.getFile());
        assertEquals(taskManager.getTasksList(), newTaskManager.getTasksList());
        assertEquals(taskManager.getEpicsList(), newTaskManager.getEpicsList());
        assertEquals(taskManager.getSubtasksList(), newTaskManager.getSubtasksList());
        assertEquals(taskManager.getPrioritizedTasks(), newTaskManager.getPrioritizedTasks());
    }
}