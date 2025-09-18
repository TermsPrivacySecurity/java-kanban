package server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

import task.Task;

class TasksHandlerTest extends BaseHttpHandlerTest {

    @Test
    public void handleGetTasksTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks"))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getTasksList(), gson.fromJson(response.body(), new TaskListTypeToken().getType()));
    }

    @Test
    public void handleGetTasksByIdTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/1"))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getTaskById(1), gson.fromJson(response.body(), Task.class));

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/sometext"))
                .header("Accept", "application/json")
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/1000"))
                .header("Accept", "application/json")
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void handlePostTaskTest() throws IOException, InterruptedException {
        Task task = new Task("task3", "description3", 3600, LocalDateTime.parse("2025-06-01T09:00:00"));
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks"))
                .header("Accept", "application/json")
                .build();
        assertEquals(2, manager.getTasksList().size());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(3, manager.getTasksList().size());

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"taskWithoutTimeFields\",\"description\":\"have no duration and startTime\"}"))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks"))
                .header("Accept", "application/json")
                .build();
        assertEquals(3, manager.getTasksList().size());
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(4, manager.getTasksList().size());

        Task updatedTask = manager.getTaskById(1);
        updatedTask.setName("updated task1");
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(updatedTask)))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(manager.getTaskById(1), updatedTask);

        updatedTask.setId(1000);
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(updatedTask)))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

        task = new Task("task4", "description4", 3600, LocalDateTime.parse("2025-06-01T09:00:00"));
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("not JSON"))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    public void handleDeleteTaskTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/1"))
                .header("Accept", "application/json")
                .build();
        assertEquals(2, manager.getTasksList().size());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getTasksList().size());

        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/1000"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }
}