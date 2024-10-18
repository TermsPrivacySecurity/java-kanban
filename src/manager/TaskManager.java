package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getTasksList();

    List<Subtask> getSubtasksList();

    List<Epic> getEpicsList();

    List<Subtask> getSubtaskListByEpic(int epicId);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    Task getTaskById(int taskId);

    Epic getEpicById(int epicId);

    Subtask getSubtaskById(int subtaskId);

    void addNewTask(Task newTask);

    void addNewEpic(Epic newEpic);

    void addNewSubtask(Subtask newSubtask);

    void updateTasks(Task task);

    void updateEpics(Epic epic);

    void updateSubtasks(Subtask subtask);

    void removeTaskById(int taskId);

    void removeEpicById(int epicId);

    void removeSubtaskById(int subtaskId);

    List<Task> getHistory();
}
