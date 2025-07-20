package manager;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public static FileBackedTaskManager load(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                String[] split = line.split(",");
                int id = Integer.parseInt(split[0]);
                TaskType type = TaskType.valueOf(split[1]);

                if (manager.nextId < id) {
                    manager.nextId = id;
                }
                switch (type) {
                    case TASK -> manager.tasks.put(id, Task.fromString(line));
                    case EPIC -> manager.epics.put(id, Epic.fromString(line));
                    case SUBTASK -> manager.subtasks.put(id, Subtask.fromString(line));
                }
            }
            manager.nextId++;
            return manager;
        } catch (IOException e) {
            throw new ManagerLoadException("Load exception.");
        }
    }

    private void save() {
        List<Task> tasks = getTasksList();
        tasks.addAll(getEpicsList());
        tasks.addAll(getSubtasksList());

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,
                StandardCharsets.UTF_8, false))) {
            bufferedWriter.write("id,type,name,status,description,epic\n");
            for (Task task : tasks) {
                bufferedWriter.write(task.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Save exception.");
        }
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void addNewTask(Task newTask) {
        super.addNewTask(newTask);
        save();
    }

    @Override
    public void addNewEpic(Epic newEpic) {
        super.addNewEpic(newEpic);
        save();
    }

    @Override
    public void addNewSubtask(Subtask newSubtask) {
        super.addNewSubtask(newSubtask);
        save();
    }

    @Override
    public void updateTasks(Task task) {
        super.updateTasks(task);
        save();
    }

    @Override
    public void updateEpics(Epic epic) {
        super.updateEpics(epic);
        save();
    }

    @Override
    public void updateSubtasks(Subtask subtask) {
        super.updateSubtasks(subtask);
        save();
    }

    @Override
    public void removeTaskById(int taskId) {
        super.removeTaskById(taskId);
        save();
    }

    @Override
    public void removeEpicById(int epicId) {
        super.removeEpicById(epicId);
        save();
    }

    @Override
    public void removeSubtaskById(int subtaskId) {
        super.removeSubtaskById(subtaskId);
        save();
    }
}
