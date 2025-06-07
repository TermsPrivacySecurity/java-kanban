package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    static TaskManager taskManager;
    static Task task1;
    static Task task2;
    static Epic epic1;
    static Epic epic2;
    static Subtask subtask1;
    static Subtask subtask2;
    static Subtask subtask3;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
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
        assertEquals(task1, taskManager.getTaskById(0));
        assertEquals(task2, taskManager.getTaskById(1));
    }

    @Test
    public void shouldBeNegativeWhenGetHistory() {
        taskManager.getTaskById(0);
        assertNotEquals(new ArrayList<>(), taskManager.getHistory());
    }

    @Test
    public void shouldBePositiveWhenGetTheSameTaskAFewTimes() {
        for (int i = 0; i < 11; i++) {
            taskManager.getTaskById(0);
        }
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    public void shouldBePositiveWhenRemoveSubtask(){
        ArrayList<Integer> subtaskIds = new ArrayList<>();
        subtaskIds.add(subtask1.getId());
        subtaskIds.add(subtask2.getId());
        subtaskIds.add(subtask3.getId());
        assertEquals(subtaskIds, epic1.getSubtaskIds());
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
        taskManager.removeSubtaskById(subtask2.getId());
        subtaskIds.remove(Integer.valueOf(subtask2.getId()));
        assertEquals(subtaskIds, epic1.getSubtaskIds());
        assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
    public void shouldBePositiveWhenChangeSomeField(){
        taskManager.getTaskById(task1.getId());
        List<Task> historyBeforeChange = taskManager.getHistory();
        task1.setName("changedTask1");
        List<Task> historyAfterChange = taskManager.getHistory();
        assertEquals(historyBeforeChange.getFirst().getName(), historyAfterChange.getFirst().getName());
    }

    @Test
    public void shouldBeEmptyAfterRemoveAllEpics(){
        taskManager.removeAllEpics();
        assertTrue(taskManager.getEpicsList().isEmpty());
        assertTrue(taskManager.getSubtasksList().isEmpty());
    }

    @Test
    public void shouldBeEmptyAfterRemoveAllSubtasks(){
        taskManager.removeAllSubtasks();
        assertTrue(taskManager.getSubtasksList().isEmpty());
        assertTrue(taskManager.getEpicById(epic1.getId()).getSubtaskIds().isEmpty());
    }
}