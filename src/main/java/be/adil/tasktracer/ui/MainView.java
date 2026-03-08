package be.adil.tasktracer.ui;

import be.adil.tasktracer.model.Priority;
import be.adil.tasktracer.model.Status;
import be.adil.tasktracer.model.Task;
import be.adil.tasktracer.service.TaskService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.List;

public class MainView {

    private final TaskService taskService;
    private final TableView<Task> table = new TableView<>();

    private final TextField jiraTicketField = new TextField();
    private final TextField titleField = new TextField();
    private final TextArea descriptionField = new TextArea();
    private final ComboBox<Priority> priorityBox = new ComboBox<>();
    private final DatePicker dueDatePicker = new DatePicker();
    private final Label statusBar = new Label("Ready");

    private final Label totalCountLabel = new Label("0");
    private final Label doneCountLabel = new Label("0");
    private final Label inProgressCountLabel = new Label("0");

    public MainView(TaskService taskService) {
        this.taskService = taskService;
    }

    public Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(24));

        VBox header = createHeader();
        HBox stats = createStatsRow();
        VBox formCard = createFormCard();
        VBox tableCard = createTableCard();

        HBox content = new HBox(20, formCard, tableCard);
        HBox.setHgrow(tableCard, javafx.scene.layout.Priority.ALWAYS);

        VBox topSection = new VBox(20, header, stats);

        root.setTop(topSection);
        root.setCenter(content);
        root.setBottom(statusBar);

        statusBar.getStyleClass().add("status-bar");
        BorderPane.setMargin(topSection, new Insets(0, 0, 20, 0));
        BorderPane.setMargin(content, new Insets(0, 0, 12, 0));

        refreshAll();

        return root;
    }

    private VBox createHeader() {
        Label title = new Label("Task Tracer");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label("Track Jira-related work with progress, priority and structure.");
        subtitle.getStyleClass().add("page-subtitle");

        return new VBox(4, title, subtitle);
    }

    private HBox createStatsRow() {
        VBox totalCard = createStatCard("Total", totalCountLabel);
        VBox doneCard = createStatCard("Done", doneCountLabel);
        VBox inProgressCard = createStatCard("In Progress", inProgressCountLabel);

        HBox row = new HBox(16, totalCard, doneCard, inProgressCard);
        return row;
    }

    private VBox createStatCard(String title, Label valueLabel) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("stat-title");

        valueLabel.getStyleClass().add("stat-value");

        VBox card = new VBox(8, titleLabel, valueLabel);
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(180);

        return card;
    }

    private VBox createFormCard() {
        Label formTitle = new Label("Create Work Item");
        formTitle.getStyleClass().add("card-title");

        jiraTicketField.setPromptText("Example: ECO-11392");
        titleField.setPromptText("Title");
        descriptionField.setPromptText("Description");
        descriptionField.setPrefRowCount(4);
        descriptionField.setWrapText(true);

        priorityBox.setItems(FXCollections.observableArrayList(Priority.values()));
        priorityBox.setValue(Priority.MEDIUM);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);

        form.add(new Label("Jira Ticket"), 0, 0);
        form.add(jiraTicketField, 0, 1);

        form.add(new Label("Title"), 0, 2);
        form.add(titleField, 0, 3);

        form.add(new Label("Description"), 0, 4);
        form.add(descriptionField, 0, 5);

        form.add(new Label("Priority"), 0, 6);
        form.add(priorityBox, 0, 7);

        form.add(new Label("Due Date"), 0, 8);
        form.add(dueDatePicker, 0, 9);

        Button addButton = new Button("Add Task");
        addButton.setOnAction(event -> handleAddTask());

        Button refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("secondary-button");
        refreshButton.setOnAction(event -> {
            refreshAll();
            statusBar.setText("Table refreshed");
        });

        HBox actions = new HBox(10, addButton, refreshButton);
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(16, formTitle, form, actions);
        card.getStyleClass().add("card");
        card.setPrefWidth(340);

        return card;
    }

    private VBox createTableCard() {
        Label tableTitle = new Label("Work Items");
        tableTitle.getStyleClass().add("card-title");

        configureTable();

        Button deleteButton = new Button("Delete Selected");
        deleteButton.getStyleClass().add("danger-button");
        deleteButton.setOnAction(event -> handleDeleteSelected());

        Button updateProgressButton = new Button("Set Progress");
        updateProgressButton.getStyleClass().add("secondary-button");
        updateProgressButton.setOnAction(event -> handleUpdateProgress());

        Button updateStatusButton = new Button("Change Status");
        updateStatusButton.getStyleClass().add("secondary-button");
        updateStatusButton.setOnAction(event -> handleUpdateStatus());

        HBox actions = new HBox(10, updateProgressButton, updateStatusButton, deleteButton);

        VBox card = new VBox(16, tableTitle, actions, table);
        card.getStyleClass().add("card");

        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);

        return card;
    }

    private void configureTable() {
        if (!table.getColumns().isEmpty()) {
            return;
        }

        TableColumn<Task, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getId()));

        TableColumn<Task, String> jiraCol = new TableColumn<>("Jira");
        jiraCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJiraTicketKey()));

        TableColumn<Task, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));

        TableColumn<Task, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus().name()));
        statusCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status) {
                        case "DONE":
                            setStyle("-fx-text-fill: #22c55e; -fx-font-weight: bold;");
                            break;
                        case "IN_PROGRESS":
                            setStyle("-fx-text-fill: #3b82f6; -fx-font-weight: bold;");
                            break;
                        case "TODO":
                            setStyle("-fx-text-fill: #94a3b8; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        TableColumn<Task, String> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPriority().name()));
        priorityCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String priority, boolean empty) {
                super.updateItem(priority, empty);

                if (empty || priority == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(priority);
                    switch (priority) {
                        case "HIGH":
                            setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
                            break;
                        case "MEDIUM":
                            setStyle("-fx-text-fill: #f59e0b; -fx-font-weight: bold;");
                            break;
                        case "LOW":
                            setStyle("-fx-text-fill: #22c55e; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        TableColumn<Task, Number> progressCol = new TableColumn<>("Progress");
        progressCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getProgressPercent()));
        progressCol.setCellFactory(column -> new TableCell<>() {
            private final ProgressBar progressBar = new ProgressBar();
            private final Label label = new Label();
            private final VBox box = new VBox(4, progressBar, label);

            @Override
            protected void updateItem(Number progress, boolean empty) {
                super.updateItem(progress, empty);

                if (empty || progress == null) {
                    setGraphic(null);
                } else {
                    int value = progress.intValue();
                    progressBar.setProgress(value / 100.0);
                    label.setText(value + "%");
                    label.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 11px;");
                    progressBar.setMaxWidth(Double.MAX_VALUE);
                    setGraphic(box);
                }
            }
        });

        table.getColumns().addAll(idCol, jiraCol, titleCol, statusCol, priorityCol, progressCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No work items yet"));
    }

    private void handleAddTask() {
        try {
            String jiraKey = jiraTicketField.getText();
            String title = titleField.getText();
            String description = descriptionField.getText();
            Priority priority = priorityBox.getValue();
            LocalDate dueDate = dueDatePicker.getValue();

            taskService.create(jiraKey, title, description, priority, dueDate);

            clearForm();
            refreshAll();
            statusBar.setText("Task created successfully");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void handleDeleteSelected() {
        Task selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Please select a task first.");
            return;
        }

        boolean deleted = taskService.delete(selected.getId());

        if (deleted) {
            refreshAll();
            statusBar.setText("Task deleted");
        } else {
            showError("Task could not be deleted.");
        }
    }

    private void handleUpdateProgress() {
        Task selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Please select a task first.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(selected.getProgressPercent()));
        dialog.setTitle("Update Progress");
        dialog.setHeaderText("Set progress for task: " + selected.getTitle());
        dialog.setContentText("Progress (0-100):");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int progress = Integer.parseInt(input.trim());
                taskService.updateProgress(selected.getId(), progress);
                refreshAll();
                statusBar.setText("Progress updated");
            } catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    private void handleUpdateStatus() {
        Task selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Please select a task first.");
            return;
        }

        ChoiceDialog<Status> dialog = new ChoiceDialog<>(selected.getStatus(), Status.values());
        dialog.setTitle("Change Status");
        dialog.setHeaderText("Change status for task: " + selected.getTitle());
        dialog.setContentText("Status:");

        dialog.showAndWait().ifPresent(status -> {
            try {
                taskService.updateStatus(selected.getId(), status);
                refreshAll();
                statusBar.setText("Status updated");
            } catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    private void refreshAll() {
        List<Task> tasks = taskService.list();
        table.setItems(FXCollections.observableArrayList(tasks));
        updateStats(tasks);
    }

    private void updateStats(List<Task> tasks) {
        long total = tasks.size();
        long done = tasks.stream().filter(task -> task.getStatus() == Status.DONE).count();
        long inProgress = tasks.stream().filter(task -> task.getStatus() == Status.IN_PROGRESS).count();

        totalCountLabel.setText(String.valueOf(total));
        doneCountLabel.setText(String.valueOf(done));
        inProgressCountLabel.setText(String.valueOf(inProgress));
    }

    private void clearForm() {
        jiraTicketField.clear();
        titleField.clear();
        descriptionField.clear();
        priorityBox.setValue(Priority.MEDIUM);
        dueDatePicker.setValue(null);
    }

    private void showError(String message) {
        statusBar.setText("Error: " + message);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(message);
        alert.showAndWait();
    }
}