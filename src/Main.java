import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

public class Main {
    public static void main(String[] args) {
        //Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач.
        TaskManager taskManager = Managers.getDefault();
        Task firstTask = new Task("First task", "for first time", Status.IN_PROGRESS);
        taskManager.addNewTask(firstTask);
        Task secondTask = new Task("Second task", "for other time", Status.NEW);
        taskManager.addNewTask(secondTask);
        Epic firstEpic = new Epic("First epic", "for 3 subtasks");
        taskManager.addNewEpic(firstEpic);
        Subtask firstSubtask = new Subtask("First subtask", "First", Status.NEW, firstEpic.getId());
        taskManager.addNewSubtask(firstSubtask);
        Subtask secondSubtask = new Subtask("Second subtask", "Second", Status.NEW, firstEpic.getId());
        taskManager.addNewSubtask(secondSubtask);
        Subtask thirdSubtask = new Subtask("Third subtask", "Third", Status.NEW, firstEpic.getId());
        taskManager.addNewSubtask(thirdSubtask);
        Epic emptyEpic = new Epic("Empty Epic", "it's empty");
        taskManager.addNewEpic(emptyEpic);

        //Запросите созданные задачи несколько раз в разном порядке.После каждого запроса выведите историю и убедитесь,
        // что в ней нет повторов.
        taskManager.getTaskById(firstTask.getId());
        taskManager.getEpicById(firstEpic.getId());
        taskManager.getSubtaskById(firstSubtask.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(firstTask.getId());
        System.out.println(taskManager.getHistory());
        firstTask.setStatus(Status.DONE);
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(firstTask.getId());
        System.out.println(taskManager.getHistory());

        //Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.
        taskManager.removeTaskById(firstTask.getId());
        System.out.println(taskManager.getHistory());

        //Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.
        taskManager.removeEpicById(firstEpic.getId());
        System.out.println(taskManager.getHistory());
    }
}
