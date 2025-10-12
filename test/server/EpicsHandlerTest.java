package server;

import org.junit.jupiter.api.Test;
import task.Epic;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class EpicsHandlerTest extends BaseHttpHandlerTest {
    @Test
    public void handleGetEpicsTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics"))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getEpicsList(), gson.fromJson(response.body(), new EpicListTypeToken().getType()));
    }

    @Test
    public void handleGetEpicByIdTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics/3"))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getEpicById(3), gson.fromJson(response.body(), Epic.class));

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics/sometext"))
                .header("Accept", "application/json")
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics/3000"))
                .header("Accept", "application/json")
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void handlePostEpicTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic3", "description3");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics"))
                .header("Accept", "application/json")
                .build();
        assertEquals(2, manager.getEpicsList().size());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(3, manager.getEpicsList().size());

        Epic updatedEpic = manager.getEpicById(3);
        updatedEpic.setName("updated epic1");
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(updatedEpic)))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(manager.getEpicById(3), updatedEpic);

        updatedEpic.setId(3000);
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(updatedEpic)))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("not JSON"))
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    public void handleDeleteEpicTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics/3"))
                .header("Accept", "application/json")
                .build();
        assertEquals(2, manager.getEpicsList().size());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getEpicsList().size());
        assertEquals(0, manager.getSubtasksList().size());

        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics/3000"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void handleGetSubtasksByEpic() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics/3/subtasks"))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getSubtaskListByEpic(3), gson.fromJson(response.body(), new SubtaskListTypeToken().getType()));

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics/3000/subtasks"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics/sometext/subtasks"))
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }
}