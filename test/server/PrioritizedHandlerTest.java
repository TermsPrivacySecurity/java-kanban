package server;

import org.junit.jupiter.api.Test;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrioritizedHandlerTest extends BaseHttpHandlerTest {

    @Test
    public void handleGetPrioritizedTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + HttpTaskServer.PORT + "/prioritized"))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> prioritized = gson.fromJson(response.body(), new TaskListTypeToken().getType());
        assertEquals(manager.getPrioritizedTasks().size(), prioritized.size());
    }
}