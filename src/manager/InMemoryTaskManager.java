package manager;

import task.Epic;
import task.TaskStatus;
import task.Subtask;
import task.Task;

import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTaskManager implements TaskManager {
    protected int nextId = 1;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream()
                .toList();
    }

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
        return subtasks.values().stream()
                .filter(subtask -> epicId == subtask.getEpicId())
                .toList();
    }

    @Override
    public void removeAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            for (int subtaskId : epic.getSubtaskIds()) {
                historyManager.remove(subtaskId);
                prioritizedTasks.remove(subtasks.get(subtaskId));
            }
            historyManager.remove(epic.getId());
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (int subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
            prioritizedTasks.remove(subtasks.get(subtaskId));
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            calculateEpicFields(epic.getId());
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
        historyManager.add(subtasks.get(subtaskId));
        return subtasks.get(subtaskId);
    }

    @Override
    public void addNewTask(Task newTask) {
        if (isNotCrossing(newTask)) {
            newTask.setId(nextId++);
            tasks.put(newTask.getId(), newTask);
            prioritizedTasks.add(newTask);
        }
    }

    @Override
    public void addNewEpic(Epic newEpic) {
        newEpic.setId(nextId++);
        epics.put(newEpic.getId(), newEpic);
    }

    @Override
    public void addNewSubtask(Subtask newSubtask) {
        if (isNotCrossing(newSubtask)) {
            newSubtask.setId(nextId++);
            subtasks.put(newSubtask.getId(), newSubtask);
            prioritizedTasks.add(newSubtask);
            epics.get(newSubtask.getEpicId()).getSubtaskIds().add(newSubtask.getId());
            calculateEpicFields(newSubtask.getEpicId());
        }
    }

    @Override
    public void updateTasks(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            prioritizedTasks.remove(task);
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateEpics(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            calculateEpicFields(epic.getId());
        }
    }

    @Override
    public void updateSubtasks(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            prioritizedTasks.remove(subtask);
            prioritizedTasks.add(subtask);
            calculateEpicFields(subtask.getEpicId());
        }
    }

    @Override
    public void removeTaskById(int taskId) {
        historyManager.remove(taskId);
        prioritizedTasks.remove(tasks.get(taskId));
        tasks.remove(taskId);
    }

    @Override
    public void removeEpicById(int epicId) {
        for (int subtaskId : epics.get(epicId).getSubtaskIds()) {
            historyManager.remove(subtaskId);
            prioritizedTasks.remove(subtasks.get(subtaskId));
            subtasks.remove(subtaskId);
        }
        historyManager.remove(epicId);
        epics.remove(epicId);
    }

    @Override
    public void removeSubtaskById(int subtaskId) {
        int epicId = subtasks.get(subtaskId).getEpicId();
        epics.get(epicId).getSubtaskIds().remove(Integer.valueOf(subtaskId));
        historyManager.remove(subtaskId);
        prioritizedTasks.remove(subtasks.get(subtaskId));
        subtasks.remove(subtaskId);
        calculateEpicFields(epicId);
    }

    private void calculateEpicFields(int epicId) {
        AtomicInteger sumStatusOrdinals = new AtomicInteger();
        AtomicLong duration = new AtomicLong();
        Epic epic = epics.get(epicId);
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        List<Subtask> subtasksList = epics.get(epicId).getSubtaskIds().stream()
                .map(subtasks::get)
                .sorted(Comparator.comparing(Task::getStartTime))
                .peek(subtask -> duration.addAndGet(subtask.getDuration().toMinutes()))
                .peek(subtask -> sumStatusOrdinals.addAndGet(subtask.getStatus().ordinal()))
                .toList();

        if (!subtasksList.isEmpty()) {
            startTime = subtasksList.getFirst().getStartTime();
            endTime = subtasksList.getLast().getEndTime();
        }

        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(Duration.ofMinutes(duration.get()));

        if (sumStatusOrdinals.get() == 0) {
            epics.get(epicId).setStatus(TaskStatus.NEW);
        } else if (sumStatusOrdinals.get() == (subtasksList.size() * TaskStatus.DONE.ordinal())) {
            epics.get(epicId).setStatus(TaskStatus.DONE);
        } else {
            epics.get(epicId).setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private boolean isNotCrossing(Task task) {
        if (prioritizedTasks.isEmpty()) {
            return true;
        }
        return prioritizedTasks.stream()
                .filter(t -> t.getStartTime().isBefore(task.getEndTime())
                        && t.getEndTime().isAfter(task.getStartTime()))
                .toList()
                .isEmpty();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}