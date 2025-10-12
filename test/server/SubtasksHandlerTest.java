package server;

import org.junit.jupiter.api.Test;
import task.Subtask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtasksHandlerTest extends BaseHttpHandlerTest {

    @Test
    public void handleGetSubtasksTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks"))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getSubtasksList(), gson.fromJson(response.body(), new SubtaskListTypeToken().getType()));
    }

    @Test
    public void handleGetSubtasksByIdTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks/5"))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getSubtaskById(5), gson.fromJson(response.body(), Subtask.class));

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks/sometext"))
                .header("Accept", "application/json")
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks/1000"))
                .header("Accept", "application/json")
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void handlePostSubtaskTest() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("subtask4", "description4", epic1.getId(), 60,
                LocalDateTime.parse("2025-01-10T16:10:00"));
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks"))
                .header("Accept", "application/json")
                .build();
        assertEquals(3, manager.getSubtasksList().size());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(4, manager.getSubtasksList().size());

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"subtaskWithoutTimeFields\",\"description\":\"have no duration and startTime\", \"epicId\":3}"))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks"))
                .header("Accept", "application/json")
                .build();
        assertEquals(4, manager.getSubtasksList().size());
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(5, manager.getSubtasksList().size());

        Subtask updatedSubtask = manager.getSubtaskById(5);
        updatedSubtask.setName("updated subtask1");
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(updatedSubtask)))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(manager.getSubtaskById(5), updatedSubtask);

        updatedSubtask.setId(1000);
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(updatedSubtask)))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

        subtask = new Subtask("subtask4", "description4", epic1.getId(), 60,
                LocalDateTime.parse("2025-01-10T16:10:00"));
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("not JSON"))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    public void handleDeleteSubtaskTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks/5"))
                .header("Accept", "application/json")
                .build();
        assertEquals(3, manager.getSubtasksList().size());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(2, manager.getSubtasksList().size());

        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks/5000"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }
}