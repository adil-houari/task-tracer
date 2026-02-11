package be.adil.tasktracer.repo;

import be.adil.tasktracer.model.Task;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTaskRepository implements TaskRepository {

    private final Map<Long, Task> storage = new ConcurrentHashMap<>();

    @Override
    public Task save(Task task) {
        storage.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean delete(long id) {
        return storage.remove(id) != null;
    }
}
