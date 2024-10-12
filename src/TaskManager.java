import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int nextId;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtaskListByEpic(int epicId){
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        for(Subtask subtask : subtasks.values()){
            if(subtask.getEpicId() == epicId){
                subtasksByEpic.add(subtask);
            }
        }
        return subtasksByEpic;
    }

    public void removeAllTasks() {
        tasks = new HashMap<>();
    }

    public void removeAllEpics() {
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public void removeAllSubtasks() {
        subtasks = new HashMap<>();
        for (Epic epic : epics.values()) {
            epic.setSubtaskIds(new ArrayList<>());
            updateEpicStatus(epic.getId());
        }
    }

    public Task getTaskById(int taskId) {
        return tasks.get(taskId);
    }

    public Epic getEpicById(int epicId) {
        return epics.get(epicId);
    }

    public Subtask getSubtaskById(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    public void addNewTask(Task newTask) {
        newTask.setId(nextId++);
        tasks.put(newTask.getId(), newTask);
    }

    public void addNewEpic(Epic newEpic) {
        newEpic.setId(nextId++);
        epics.put(newEpic.getId(), newEpic);
    }

    public void addNewSubtask(Subtask newSubtask) {
        newSubtask.setId(nextId++);
        subtasks.put(newSubtask.getId(), newSubtask);
        epics.get(newSubtask.getEpicId()).getSubtaskIds().add(newSubtask.getId());
        updateEpicStatus(newSubtask.getEpicId());
    }

    public void updateTasks(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpics(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    public void updateSubtasks(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    public void removeTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void removeEpicById(int epicId) {
        for (int subtaskId : epics.get(epicId).getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(epicId);
    }

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
}
