package server;

import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryHandlerTest extends BaseHttpHandlerTest {

    @Test
    public void handleGetHistoryTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/1"))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getHistory().getLast(), gson.fromJson(response.body(), Task.class));

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics/3"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getHistory().getLast(), gson.fromJson(response.body(), Epic.class));

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks/5"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getHistory().getLast(), gson.fromJson(response.body(), Subtask.class));

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/history"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> history = gson.fromJson(response.body(), new TaskListTypeToken().getType());
        assertEquals(manager.getHistory().size(), history.size());
    }
}