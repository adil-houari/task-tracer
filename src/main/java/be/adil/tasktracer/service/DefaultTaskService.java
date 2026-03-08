package be.adil.tasktracer.service;

import be.adil.tasktracer.model.Priority;
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
    public Task create(String jiraTicketKey,
                       String title,
                       String description,
                       Priority priority,
                       LocalDate dueDate) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (jiraTicketKey == null || jiraTicketKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Jira ticket key is required");
        }

        Task task = new Task(
                idSeq.getAndIncrement(),
                jiraTicketKey.trim().toUpperCase(),
                title.trim(),
                description,
                Status.TODO,
                priority,
                0,
                dueDate
        );

        return repo.save(task);
    }

    @Override
    public List<Task> list() {
        return repo.findAll();
    }

    @Override
    public Task updateStatus(long id, Status status) {
        Task task = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + id));

        task.setStatus(status);

        if (status == Status.DONE) {
            task.setProgressPercent(100);
        }

        return repo.save(task);
    }

    @Override
    public Task updateProgress(long id, int progressPercent) {
        if (progressPercent < 0 || progressPercent > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }

        Task task = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + id));

        task.setProgressPercent(progressPercent);

        if (progressPercent == 100) {
            task.setStatus(Status.DONE);
        } else if (progressPercent > 0 && task.getStatus() == Status.TODO) {
            task.setStatus(Status.IN_PROGRESS);
        }

        return repo.save(task);
    }

    @Override
    public boolean delete(long id) {
        return repo.delete(id);
    }
}