package be.adil.tasktracer;

import be.adil.tasktracer.cli.Cli;
import be.adil.tasktracer.repo.InMemoryTaskRepository;
import be.adil.tasktracer.service.DefaultTaskService;
import be.adil.tasktracer.service.TaskService;

public class App {
    public static void main(String[] args) {
        TaskService service = new DefaultTaskService(new InMemoryTaskRepository());
        new Cli(service).run();
    }
}
