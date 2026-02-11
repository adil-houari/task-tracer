package be.adil.tasktracer.service;

import be.adil.tasktracer.model.Status;
import be.adil.tasktracer.model.Task;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    Task create(String title, String description, LocalDate dueDate);
    List<Task> list();
    Task updateStatus(long id, Status status);
    boolean delete(long id);
}
