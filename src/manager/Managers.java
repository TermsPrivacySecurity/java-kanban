package manager;

import exceptions.ManagerLoadException;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager getFromFile(File file) {
        try {
            return FileBackedTaskManager.load(file);
        } catch (ManagerLoadException e) {
            System.out.println("Return new TaskManager");
            return new FileBackedTaskManager(file);
        }

    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
