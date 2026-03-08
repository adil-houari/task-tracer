package be.adil.tasktracer;

import be.adil.tasktracer.repo.InMemoryTaskRepository;
import be.adil.tasktracer.service.DefaultTaskService;
import be.adil.tasktracer.service.TaskService;
import be.adil.tasktracer.ui.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        TaskService taskService = new DefaultTaskService(new InMemoryTaskRepository());
        MainView mainView = new MainView(taskService);

        Scene scene = new Scene(mainView.createContent(), 1200, 720);
        scene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());

        stage.setTitle("Task Tracer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}