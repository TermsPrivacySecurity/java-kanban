package manager;

import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    @BeforeEach
    public void beforeEach() throws IOException {
        taskManager = new InMemoryTaskManager();
        super.beforeEach();
    }
}