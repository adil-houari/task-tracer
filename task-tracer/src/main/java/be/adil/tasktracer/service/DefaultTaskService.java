package be.adil.tasktracer.service;

import be.adil.tasktracer.model.Status;
import be.adil.tasktracer.model.Task;
import be.adil.tasktracer.repo.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultTaskService implements TaskService {

    private final TaskRepository repo;
    private final AtomicLong idSeq = new AtomicLong(1);

    public DefaultTaskService(TaskRepository repo) {
        this.repo = repo;
    }

    @Override
    public Task create(String title, String description, LocalDate dueDate) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        long id = idSeq.getAndIncrement();
        Task task = new Task(id, title.trim(), description, Status.TODO, dueDate);
        return repo.save(task);
    }

    @Override
    public List<Task> list() {
        return repo.findAll();
    }

    @Override
    public Task updateStatus(long id, Status status) {
        Task task = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found: " + id));
        task.setStatus(status);
        return repo.save(task);
    }

    @Override
    public boolean delete(long id) {
        return repo.delete(id);
    }
}
