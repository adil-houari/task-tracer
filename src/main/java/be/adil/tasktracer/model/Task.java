package be.adil.tasktracer.model;

import java.time.LocalDate;
import java.util.Objects;

public class Task {

    private final long id;
    private String jiraTicketKey;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private int progressPercent;
    private LocalDate dueDate;

    public Task(long id,
                String jiraTicketKey,
                String title,
                String description,
                Status status,
                Priority priority,
                int progressPercent,
                LocalDate dueDate) {
        this.id = id;
        this.jiraTicketKey = jiraTicketKey;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.progressPercent = progressPercent;
        this.dueDate = dueDate;
    }

    public long getId() {
        return id;
    }

    public String getJiraTicketKey() {
        return jiraTicketKey;
    }

    public void setJiraTicketKey(String jiraTicketKey) {
        this.jiraTicketKey = jiraTicketKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", jiraTicketKey='" + jiraTicketKey + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", progressPercent=" + progressPercent +
                ", dueDate=" + dueDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}