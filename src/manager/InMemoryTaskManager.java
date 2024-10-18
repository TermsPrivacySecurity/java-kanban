package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 0;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtaskListByEpic(int epicId) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epicId) {
                subtasksByEpic.add(subtask);
            }
        }
        return subtasksByEpic;
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic.getId());
        }
    }

    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        historyManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    @Override
    public void addNewTask(Task newTask) {
        newTask.setId(nextId++);
        tasks.put(newTask.getId(), newTask);
    }

    @Override
    public void addNewEpic(Epic newEpic) {
        newEpic.setId(nextId++);
        epics.put(newEpic.getId(), newEpic);
    }

    @Override
    public void addNewSubtask(Subtask newSubtask) {
        newSubtask.setId(nextId++);
        subtasks.put(newSubtask.getId(), newSubtask);
        epics.get(newSubtask.getEpicId()).getSubtaskIds().add(newSubtask.getId());
        updateEpicStatus(newSubtask.getEpicId());
    }

    @Override
    public void updateTasks(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpics(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());
        }
    }

    @Override
    public void updateSubtasks(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public void removeTaskById(int taskId) {
        tasks.remove(taskId);
    }

    @Override
    public void removeEpicById(int epicId) {
        for (int subtaskId : epics.get(epicId).getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(epicId);
    }

    @Override
    public void removeSubtaskById(int subtaskId) {
        int epicId = subtasks.get(subtaskId).getEpicId();
        epics.get(epicId).getSubtaskIds().remove(Integer.valueOf(subtaskId));
        subtasks.remove(subtaskId);
        updateEpicStatus(epicId);
    }

    public void updateEpicStatus(int epicId) {
        if (epics.get(epicId).getSubtaskIds().isEmpty()) {
            epics.get(epicId).setStatus(Status.NEW);
            return;
        }
        int iterationCount = 0;
        int sumStatusOrdinals = 0;
        for (int id : epics.get(epicId).getSubtaskIds()) {
            iterationCount++;
            sumStatusOrdinals += subtasks.get(id).getStatus().ordinal();
        }
        if (sumStatusOrdinals == 0) {
            epics.get(epicId).setStatus(Status.NEW);
        } else if (sumStatusOrdinals == (iterationCount * Status.DONE.ordinal())) {
            epics.get(epicId).setStatus(Status.DONE);
        } else {
            epics.get(epicId).setStatus(Status.IN_PROGRESS);
        }

    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
