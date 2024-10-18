package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getTasksList();

    ArrayList<Subtask> getSubtasksList();

    ArrayList<Epic> getEpicsList();

    ArrayList<Subtask> getSubtaskListByEpic(int epicId);

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

    ArrayList<Task> getHistory();
}
