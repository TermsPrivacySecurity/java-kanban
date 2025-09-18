package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import serializers.DurationSerializer;
import serializers.LocalDateTimeSerializer;
import serializers.SubtaskDeserializer;
import serializers.TaskDeserializer;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class BaseHttpHandlerTest {
    HttpTaskServer server = new HttpTaskServer();
    HttpClient client = HttpClient.newHttpClient();
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    TaskManager manager = server.getManager();
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationSerializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
            .registerTypeAdapter(Task.class, new TaskDeserializer())
            .registerTypeAdapter(Subtask.class, new SubtaskDeserializer())
            .create();

    @BeforeEach
    public void beforeEach() throws IOException {
        task1 = new Task("task1", "description1", 60,
                LocalDateTime.parse("2025-01-01T09:00:00"));
        manager.addNewTask(task1);
        task2 = new Task("task2", "description2", 180,
                LocalDateTime.parse("2025-02-01T09:00:00"));
        manager.addNewTask(task2);
        epic1 = new Epic("epic1", "description1");
        manager.addNewEpic(epic1);
        epic2 = new Epic("epic2", "description2");
        manager.addNewEpic(epic2);
        subtask1 = new Subtask("subtask1", "description1", epic1.getId(), 60,
                LocalDateTime.parse("2025-01-01T10:10:00"));
        manager.addNewSubtask(subtask1);
        subtask2 = new Subtask("subtask2", "description2", epic1.getId(), 60,
                LocalDateTime.parse("2025-01-02T10:00:00"));
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        manager.addNewSubtask(subtask2);
        subtask3 = new Subtask("subtask3", "description3", epic1.getId(), 90,
                LocalDateTime.parse("2025-01-03T09:00:00"));
        manager.addNewSubtask(subtask3);
        server.start();
    }

    @AfterEach
    public void afterEach() {
        server.stop();
    }
}

class TaskListTypeToken extends TypeToken<List<Task>> {
}

class SubtaskListTypeToken extends TypeToken<List<Subtask>> {
}

class EpicListTypeToken extends TypeToken<List<Epic>> {
}