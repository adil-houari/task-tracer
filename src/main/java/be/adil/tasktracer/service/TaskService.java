package be.adil.tasktracer.service;

import be.adil.tasktracer.model.Priority;
import be.adil.tasktracer.model.Status;
import be.adil.tasktracer.model.Task;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    Task create(String jiraTicketKey,
                String title,
                String description,
                Priority priority,
                LocalDate dueDate);

    List<Task> list();

    Task updateStatus(long id, Status status);

    Task updateProgress(long id, int progressPercent);

    boolean delete(long id);
}