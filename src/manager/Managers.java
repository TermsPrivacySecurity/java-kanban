package manager;

import exceptions.ManagerLoadException;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            manager.load();
        } catch (ManagerLoadException e) {
            System.out.println("Return new TaskManager");
            return new FileBackedTaskManager(file);
        }
        return manager;
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
