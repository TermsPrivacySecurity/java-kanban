import manager.FileBackedTaskManager;
import manager.Managers;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //Заведите несколько разных задач, эпиков и подзадач.
        FileBackedTaskManager taskManager = Managers.loadFromFile(File.createTempFile("TaskManager", ".txt"));
        Task firstTask = new Task("First task", "for first time", Status.IN_PROGRESS);
        taskManager.addNewTask(firstTask);
        Task secondTask = new Task("Second task", "for other time", Status.NEW);
        taskManager.addNewTask(secondTask);
        Epic firstEpic = new Epic("First epic", "for 3 subtasks");
        taskManager.addNewEpic(firstEpic);
        Subtask firstSubtask = new Subtask("First subtask", "First", Status.IN_PROGRESS, firstEpic.getId());
        taskManager.addNewSubtask(firstSubtask);
        Subtask secondSubtask = new Subtask("Second subtask", "Second", Status.NEW, firstEpic.getId());
        taskManager.addNewSubtask(secondSubtask);
        Subtask thirdSubtask = new Subtask("Third subtask", "Third", Status.NEW, firstEpic.getId());
        taskManager.addNewSubtask(thirdSubtask);
        Epic emptyEpic = new Epic("Empty Epic", "it's empty");
        taskManager.addNewEpic(emptyEpic);

        //Создайте новый FileBackedTaskManager-менеджер из этого же файла.
        FileBackedTaskManager newTaskManager = new FileBackedTaskManager(taskManager.getFile());
        newTaskManager.load();

        //Проверьте, что все задачи, эпики, подзадачи, которые были в старом менеджере, есть в новом.
        System.out.println(taskManager.getTasksList());
        System.out.println(newTaskManager.getTasksList());

        System.out.println(taskManager.getEpicsList());
        System.out.println(newTaskManager.getEpicsList());

        System.out.println(taskManager.getSubtasksList());
        System.out.println(newTaskManager.getSubtasksList());


    }
}
