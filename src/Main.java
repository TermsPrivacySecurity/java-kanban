import manager.FileBackedTaskManager;
import manager.Managers;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //Заведите несколько разных задач, эпиков и подзадач.
        FileBackedTaskManager taskManager = Managers.getFromFile(File.createTempFile("TaskManager", ".txt"));
        Task firstTask = new Task("First task", "for first time");
        taskManager.addNewTask(firstTask);
        Task secondTask = new Task("Second task", "for other time");
        taskManager.addNewTask(secondTask);
        Epic firstEpic = new Epic("First epic", "for 3 subtasks");
        taskManager.addNewEpic(firstEpic);
        Subtask firstSubtask = new Subtask("First subtask", "First", firstEpic.getId());
        taskManager.addNewSubtask(firstSubtask);
        Subtask secondSubtask = new Subtask("Second subtask", "Second", firstEpic.getId());
        taskManager.addNewSubtask(secondSubtask);
        Subtask thirdSubtask = new Subtask("Third subtask", "Third", firstEpic.getId());
        taskManager.addNewSubtask(thirdSubtask);
        Epic emptyEpic = new Epic("Empty Epic", "it's empty");
        taskManager.addNewEpic(emptyEpic);

        //Создайте новый FileBackedTaskManager-менеджер из этого же файла.
        FileBackedTaskManager newTaskManager = FileBackedTaskManager.load(taskManager.getFile());

        //Проверьте, что все задачи, эпики, подзадачи, которые были в старом менеджере, есть в новом.
        System.out.println(taskManager.getTasksList());
        System.out.println(newTaskManager.getTasksList());

        System.out.println(taskManager.getEpicsList());
        System.out.println(newTaskManager.getEpicsList());

        System.out.println(taskManager.getSubtasksList());
        System.out.println(newTaskManager.getSubtasksList());


    }
}
