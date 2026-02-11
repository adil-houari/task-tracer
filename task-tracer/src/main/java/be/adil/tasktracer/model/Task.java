package be.adil.tasktracer.model;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private final long id;
    private String title;
    private String description;
    private Status status;
    private LocalDate dueDate;

    public Task(long id, String title, String description, Status status, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public LocalDate getDueDate() { return dueDate; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(Status status) { this.status = status; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    @Override public String toString() {
        return "Task{id=" + id + ", title='" + title + "', status=" + status + ", dueDate=" + dueDate + "}";
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override public int hashCode() {
        return Objects.hash(id);
    }
}
