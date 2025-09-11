package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;
    static Task task1;
    static Task task2;
    static Epic epic1;
    static Epic epic2;
    static Subtask subtask1;
    static Subtask subtask2;
    static Subtask subtask3;

    @BeforeEach
    public void beforeEach() throws IOException {
        task1 = new Task("task1", "description1", 60,
                LocalDateTime.parse("2025-01-01T09:00:00"));
        taskManager.addNewTask(task1);
        task2 = new Task("task2", "description2", 180,
                LocalDateTime.parse("2025-02-01T09:00:00"));
        taskManager.addNewTask(task2);
        epic1 = new Epic("epic1", "description1");
        taskManager.addNewEpic(epic1);
        epic2 = new Epic("epic2", "description2");
        taskManager.addNewEpic(epic2);
        subtask1 = new Subtask("subtask1", "description1", epic1.getId(), 60,
                LocalDateTime.parse("2025-01-01T10:10:00"));
        taskManager.addNewSubtask(subtask1);
        subtask2 = new Subtask("subtask2", "description2", epic1.getId(), 60,
                LocalDateTime.parse("2025-01-02T10:00:00"));
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.addNewSubtask(subtask2);
        subtask3 = new Subtask("subtask3", "description3", epic1.getId(), 90,
                LocalDateTime.parse("2025-01-03T09:00:00"));
        taskManager.addNewSubtask(subtask3);
    }

    @Test
    public void shouldBeNegativeWhenTaskIsCrossing() {
        Task crossingTask = new Task("crossing task", "this task crossing with task1",
                60, LocalDateTime.parse("2025-01-01T09:20:00"));
        taskManager.addNewTask(crossingTask);
        assertFalse(taskManager.getPrioritizedTasks().contains(crossingTask));
    }

    @Test
    public void shouldBePositiveWhenTaskIsNotCrossing() {
        Task notCrossingTask = new Task("not crossing task", "this task isn't crossing with anything",
                60, LocalDateTime.parse("2025-01-01T11:10:00"));
        taskManager.addNewTask(notCrossingTask);
        assertTrue(taskManager.getPrioritizedTasks().contains(notCrossingTask));
    }

    @Test
    public void shouldBePositiveWhenGetTaskList() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        assertEquals(tasks, taskManager.getTasksList());
    }

    @Test
    public void shouldBePositiveWhenGetEpicList() {
        List<Epic> epics = new ArrayList<>();
        epics.add(epic1);
        epics.add(epic2);
        assertEquals(epics, taskManager.getEpicsList());
    }

    @Test
    public void shouldBePositiveWhenGetSubtaskList() {
        List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        subtasks.add(subtask3);
        assertEquals(subtasks, taskManager.getSubtasksList());
    }

    @Test
    public void shouldBePositiveWhenGetSubtaskListByEpic() {
        List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        subtasks.add(subtask3);
        assertEquals(subtasks, taskManager.getSubtaskListByEpic(epic1.getId()));
        assertEquals(new ArrayList<>(), taskManager.getSubtaskListByEpic(epic2.getId()));
    }

    @Test
    public void shouldBePositiveWhenGetTaskById() {
        assertEquals(task1, taskManager.getTaskById(task1.getId()));
        assertEquals(task2, taskManager.getTaskById(task2.getId()));
    }

    @Test
    public void shouldBeNegativeWhenGetHistory() {
        taskManager.getTaskById(task1.getId());
        assertNotEquals(new ArrayList<>(), taskManager.getHistory());
    }

    @Test
    public void shouldBePositiveWhenGetTheSameTaskAFewTimes() {
        for (int i = 0; i < 11; i++) {
            taskManager.getTaskById(task1.getId());
        }
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    public void shouldBePositiveWhenRemoveSubtask() {
        ArrayList<Integer> subtaskIds = new ArrayList<>();
        subtaskIds.add(subtask1.getId());
        subtaskIds.add(subtask2.getId());
        subtaskIds.add(subtask3.getId());
        assertEquals(subtaskIds, epic1.getSubtaskIds());
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        assertEquals(epic1.getDuration().toMinutes(), subtask1.getDuration().toMinutes()
                + subtask2.getDuration().toMinutes()
                + subtask3.getDuration().toMinutes());
        taskManager.removeSubtaskById(subtask2.getId());
        subtaskIds.remove(Integer.valueOf(subtask2.getId()));
        assertEquals(subtaskIds, epic1.getSubtaskIds());
        assertEquals(TaskStatus.NEW, epic1.getStatus());
        assertEquals(epic1.getDuration().toMinutes(), subtask1.getDuration().toMinutes()
                + subtask3.getDuration().toMinutes());
        assertFalse(taskManager.getPrioritizedTasks().contains(subtask2));
    }

    @Test
    public void shouldBePositiveWhenChangeSomeField() {
        taskManager.getTaskById(task1.getId());
        List<Task> historyBeforeChange = taskManager.getHistory();
        task1.setName("changedTask1");
        List<Task> historyAfterChange = taskManager.getHistory();
        assertEquals(historyBeforeChange.getFirst().getName(), historyAfterChange.getFirst().getName());
    }

    @Test
    public void shouldBeEmptyAfterRemoveAllEpics() {
        taskManager.removeAllEpics();
        assertTrue(taskManager.getEpicsList().isEmpty());
        assertTrue(taskManager.getSubtasksList().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().stream()
                .filter(task -> task.getType().equals(TaskType.SUBTASK))
                .toList()
                .isEmpty());
    }

    @Test
    public void shouldBeEmptyAfterRemoveAllSubtasks() {
        taskManager.removeAllSubtasks();
        assertTrue(taskManager.getSubtasksList().isEmpty());
        assertTrue(taskManager.getEpicById(epic1.getId()).getSubtaskIds().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().stream()
                .filter(task -> task.getType().equals(TaskType.SUBTASK))
                .toList()
                .isEmpty());
    }
}