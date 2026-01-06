package gui;

import controller.Controller;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.state.ProgramState;
import model.value.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InterpreterWindowView {
    private Controller controller;
    private VBox layout;

    // UI Components
    private TextField nrPrgStatesField;
    private TableView<Map.Entry<Integer, Value>> heapTableView;
    private ListView<Value> outListView;
    private ListView<String> fileTableListView;
    private ListView<Integer> prgIDsListView;
    private TableView<Map.Entry<String, Value>> symTableView;
    private ListView<String> exeStackListView;
    private Button stepButton;

    public InterpreterWindowView(Controller controller) {
        this.controller = controller;
        this.controller.setExecutor();
        initUI();
        populate();
    }

    private void initUI() {
        layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // 1. Number of PrgStates
        HBox topBox = new HBox(10);
        nrPrgStatesField = new TextField();
        nrPrgStatesField.setEditable(false);
        topBox.getChildren().addAll(new Label("Number of PrgStates:"), nrPrgStatesField);

        // 2. Grid for Lists and Tables
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        // Heap Table
        heapTableView = new TableView<>();
        TableColumn<Map.Entry<Integer, Value>, Integer> addrCol = new TableColumn<>("Address");
        addrCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getKey()));
        TableColumn<Map.Entry<Integer, Value>, String> valCol = new TableColumn<>("Value");
        valCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));
        heapTableView.getColumns().addAll(addrCol, valCol);

        // Out & FileTable
        outListView = new ListView<>();
        fileTableListView = new ListView<>();

        // IDs, SymTable, Stack
        prgIDsListView = new ListView<>();
        prgIDsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> updateSpecifics(newV));

        symTableView = new TableView<>();
        TableColumn<Map.Entry<String, Value>, String> varCol = new TableColumn<>("Variable");
        varCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        TableColumn<Map.Entry<String, Value>, String> symValCol = new TableColumn<>("Value");
        symValCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));
        symTableView.getColumns().addAll(varCol, symValCol);

        exeStackListView = new ListView<>();

        // Add to grid (Col, Row)
        grid.add(new VBox(5, new Label("Heap Table"), heapTableView), 0, 0);
        grid.add(new VBox(5, new Label("Output"), outListView), 1, 0);
        grid.add(new VBox(5, new Label("File Table"), fileTableListView), 2, 0);
        grid.add(new VBox(5, new Label("PrgState IDs"), prgIDsListView), 0, 1);
        grid.add(new VBox(5, new Label("Symbol Table"), symTableView), 1, 1);
        grid.add(new VBox(5, new Label("Execution Stack"), exeStackListView), 2, 1);

        // 3. Step Button
        stepButton = new Button("Run One Step");
        stepButton.setMaxWidth(Double.MAX_VALUE);
        stepButton.setOnAction(e -> handleStep());

        layout.getChildren().addAll(topBox, grid, stepButton);
    }

    private void handleStep() {
        try {
            List<ProgramState> prgs = controller.removeCompletedPrg(controller.getRepo().getPrgList());
            if (prgs.isEmpty()) {
                controller.stopExecutor();
                new Alert(Alert.AlertType.INFORMATION, "Program Finished!").show();
                return;
            }
            controller.oneStepForAllPrg(prgs);
            populate();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }

    private void populate() {
        List<ProgramState> prgs = controller.getRepo().getPrgList();
        if (prgs.isEmpty()) return;

        nrPrgStatesField.setText(String.valueOf(prgs.size()));

        // Shared structures (using first program in list)
        heapTableView.setItems(FXCollections.observableArrayList(prgs.get(0).getHeap().getContent().entrySet()));
        outListView.setItems(FXCollections.observableArrayList(prgs.get(0).getOut().getContent()));
        fileTableListView.setItems(FXCollections.observableArrayList(prgs.get(0).getFileTable().getContent().keySet()));

        prgIDsListView.setItems(FXCollections.observableArrayList(prgs.stream().map(ProgramState::getId).collect(Collectors.toList())));

        // Trigger update for selected ID
        updateSpecifics(prgIDsListView.getSelectionModel().getSelectedItem());
    }

    private void updateSpecifics(Integer selectedId) {
        ProgramState prg = controller.getRepo().getPrgList().stream()
                .filter(p -> selectedId != null && p.getId() == selectedId)
                .findFirst()
                .orElse(controller.getRepo().getPrgList().isEmpty() ? null : controller.getRepo().getPrgList().get(0));

        if (prg != null) {
            symTableView.setItems(FXCollections.observableArrayList(prg.getSymTable().getContent().entrySet()));
            List<String> stack = prg.getExeStack().getContent().stream().map(Object::toString).collect(Collectors.toList());
            java.util.Collections.reverse(stack);
            exeStackListView.setItems(FXCollections.observableArrayList(stack));
        }
    }

    public VBox getLayout() {
        return layout;
    }
}