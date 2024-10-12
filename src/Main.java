public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task updateIdea = new Task("update idea", "download and install minor patch 1.00.123",
                Status.NEW);
        taskManager.addNewTask(updateIdea);

        Task deleteAllGames = new Task("delete all games",
                "delete all games in steam, origin, epicGames etc.", Status.NEW);
        taskManager.addNewTask(deleteAllGames);

        Epic learnJava = new Epic("learn java", "learn 4'th sprint in practicum");
        taskManager.addNewEpic(learnJava);

        Subtask theory = new Subtask("theory", "learn 4'th sprint theory",
                Status.IN_PROGRESS, learnJava.getId());
        taskManager.addNewSubtask(theory);

        Subtask finalTask = new Subtask("final task", "complete 4'th sprint final task",
                Status.NEW, learnJava.getId());
        taskManager.addNewSubtask(finalTask);

        Epic regularTraining = new Epic("regular training",
                "training after every two hours learning java");
        taskManager.addNewEpic(regularTraining);

        Subtask training = new Subtask("training program", "пресс качат, бегит, турник, анжуманя",
                Status.IN_PROGRESS, regularTraining.getId());
        taskManager.addNewSubtask(training);

        System.out.println(taskManager.getTasksList());
        System.out.println(taskManager.getEpicsList());
        System.out.println(taskManager.getSubtasksList());
        System.out.println(taskManager.getTaskById(deleteAllGames.getId()));
        System.out.println(taskManager.getEpicById(learnJava.getId()));
        System.out.println(taskManager.getSubtaskById(theory.getId()));

        deleteAllGames.setStatus(Status.DONE);
        taskManager.updateTasks(deleteAllGames);
        System.out.println(taskManager.getTasksList());

        theory.setStatus(Status.DONE);
        finalTask.setStatus(Status.DONE);
        taskManager.updateSubtasks(theory);
        taskManager.updateSubtasks(finalTask);
        System.out.println(taskManager.getEpicById(learnJava.getId()));
        System.out.println(taskManager.getSubtaskListByEpic(learnJava.getId()));
        regularTraining.setDescription("training after every hour of java learning");
        taskManager.updateEpics(regularTraining);
        System.out.println(taskManager.getEpicsList());

        taskManager.removeTaskById(deleteAllGames.getId());
        System.out.println(taskManager.getTasksList());
        taskManager.removeSubtaskById(theory.getId());
        System.out.println(taskManager.getSubtasksList());
        taskManager.removeEpicById(learnJava.getId());
        System.out.println(taskManager.getEpicsList());
        System.out.println(taskManager.getSubtasksList());
        taskManager.removeAllTasks();
        System.out.println(taskManager.getTasksList());
        taskManager.removeAllSubtasks();
        System.out.println(taskManager.getSubtasksList());
        taskManager.removeAllEpics();
        System.out.println(taskManager.getEpicsList());
    }
}
