package be.adil.tasktracer.cli;

import be.adil.tasktracer.model.Status;
import be.adil.tasktracer.model.Task;
import be.adil.tasktracer.service.TaskService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Cli {

    private final TaskService service;

    public Cli(TaskService service) {
        this.service = service;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Task Tracer ---");
            System.out.println("1) Add task");
            System.out.println("2) List tasks");
            System.out.println("3) Update status");
            System.out.println("4) Delete task");
            System.out.println("0) Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1": addTask(sc); break;
                    case "2": listTasks(); break;
                    case "3": updateStatus(sc); break;
                    case "4": deleteTask(sc); break;
                    case "0": System.out.println("Bye!"); return;
                    default: System.out.println("Unknown choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addTask(Scanner sc) {
        System.out.print("Title: ");
        String title = sc.nextLine();

        System.out.print("Description (optional): ");
        String desc = sc.nextLine();

        System.out.print("Due date YYYY-MM-DD (optional): ");
        String due = sc.nextLine().trim();
        LocalDate dueDate = due.isEmpty() ? null : LocalDate.parse(due);

        Task t = service.create(title, desc, dueDate);
        System.out.println("Created: " + t);
    }

    private void listTasks() {
        List<Task> tasks = service.list();
        if (tasks.isEmpty()) {
            System.out.println("(no tasks)");
            return;
        }
        tasks.forEach(System.out::println);
    }

    private void updateStatus(Scanner sc) {
        System.out.print("Task id: ");
        long id = Long.parseLong(sc.nextLine().trim());

        System.out.print("Status (TODO, IN_PROGRESS, DONE): ");
        Status status = Status.valueOf(sc.nextLine().trim().toUpperCase());

        Task t = service.updateStatus(id, status);
        System.out.println("Updated: " + t);
    }

    private void deleteTask(Scanner sc) {
        System.out.print("Task id: ");
        long id = Long.parseLong(sc.nextLine().trim());

        boolean ok = service.delete(id);
        System.out.println(ok ? "Deleted." : "Not found.");
    }
}
