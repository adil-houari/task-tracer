package be.adil.tasktracer.repo;

import be.adil.tasktracer.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(long id);
    List<Task> findAll();
    boolean delete(long id);
}
