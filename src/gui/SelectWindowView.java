package gui;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.state.*;
import model.statement.*;
import model.type.*;
import model.value.*;
import model.expression.*;
import repository.Repo;

import java.util.ArrayList;
import java.util.List;

public class SelectWindowView {
    private final VBox layout;
    private final ListView<Statement> programListView;
    private final Button runButton;

    public SelectWindowView() {
        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Select a Program to Run");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        programListView = new ListView<>();
        programListView.setItems(FXCollections.observableArrayList(getExamples()));

        runButton = new Button("Run Selected Program");
        runButton.setPrefWidth(200);
        runButton.setOnAction(e -> handleRun());

        layout.getChildren().addAll(title, programListView, runButton);
    }

    private void handleRun() {
        Statement selected = programListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.ERROR, "Please select a program!").show();
            return;
        }

        try {
            selected.typecheck(new Dictionary<Type>());

            IStack<Statement> stack = new Stack<>();
            stack.push(selected);
            ProgramState initialPrg = new ProgramState(stack, new Dictionary<Value>(), new model.state.List<Value>(), new FileTable(), new Heap());

            List<ProgramState> prgList = new ArrayList<>();
            prgList.add(initialPrg);

            // Create the Controller and Repository
            Controller controller = new Controller(new Repo(prgList, "gui_log.txt"));

            // Initialize the Main Execution Window
            InterpreterWindowView executionWindow = new InterpreterWindowView(controller);
            Stage stage = new Stage();
            stage.setTitle("Interpreter Execution Dashboard");
            stage.setScene(new Scene(executionWindow.getLayout(), 950, 700));
            stage.setOnCloseRequest(event -> controller.stopExecutor());

            stage.show();

            // Close the selection window
            ((Stage) runButton.getScene().getWindow()).close();

        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).show();
        }
    }

    public VBox getLayout() {
        return layout;
    }

    private List<Statement> getExamples() {
        List<Statement> list = new ArrayList<>();

        // Ex 1: int v; v=2; print(v)
        list.add(new CompStatement(new VarDeclStatement(new IntType(), "v"),
                new CompStatement(new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v")))));

        // Ex 2: int a; int b; a=2+3*5; b=a+1; print(b)
        list.add(new CompStatement(new VarDeclStatement(new IntType(), "a"),
                new CompStatement(new VarDeclStatement(new IntType(), "b"),
                        new CompStatement(new AssignStatement("a", new ArithmeticExpression(new ValueExpression(new IntValue(2)),
                                new ArithmeticExpression(new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5)), '*'), '+')),
                                new CompStatement(new AssignStatement("b", new ArithmeticExpression(new VariableExpression("a"), new ValueExpression(new IntValue(1)), '+')),
                                        new PrintStatement(new VariableExpression("b")))))));

        // Ex 3: bool a; int v; a=true; (IF a THEN v=2 ELSE v=3); print(v)
        list.add(new CompStatement(new VarDeclStatement(new BoolType(), "a"),
                new CompStatement(new VarDeclStatement(new IntType(), "v"),
                        new CompStatement(new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompStatement(new IfStatement(new VariableExpression("a"),
                                        new AssignStatement("v", new ValueExpression(new IntValue(2))),
                                        new AssignStatement("v", new ValueExpression(new IntValue(3)))),
                                        new PrintStatement(new VariableExpression("v")))))));

        // Ex 4: int a; int b; a=100; b=0; print(a/b)
        list.add(new CompStatement(new VarDeclStatement(new IntType(), "a"),
                new CompStatement(new VarDeclStatement(new IntType(), "b"),
                        new CompStatement(new AssignStatement("a", new ValueExpression(new IntValue(100))),
                                new CompStatement(new AssignStatement("b", new ValueExpression(new IntValue(0))),
                                        new PrintStatement(new ArithmeticExpression(new VariableExpression("a"), new VariableExpression("b"), '/')))))));

        // Ex 5: bool x; x=true (Variable Declaration)
        list.add(new CompStatement(new VarDeclStatement(new BoolType(), "x"),
                new AssignStatement("x", new ValueExpression(new BoolValue(true)))));

        // Ex 6: File operations (test.in)
        list.add(new CompStatement(new VarDeclStatement(new StringType(), "varf"),
                new CompStatement(new AssignStatement("varf", new ValueExpression(new StringValue("test.in"))),
                        new CompStatement(new OpenRFileStatement(new VariableExpression("varf")),
                                new CompStatement(new VarDeclStatement(new IntType(), "varc"),
                                        new CompStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                new CompStatement(new PrintStatement(new VariableExpression("varc")),
                                                        new CompStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                                new CompStatement(new PrintStatement(new VariableExpression("varc")),
                                                                        new CloseRFileStatement(new VariableExpression("varf")))))))))));

        // Ex 7: Relational Expression (a < b)
        list.add(new CompStatement(new VarDeclStatement(new IntType(), "a"),
                new CompStatement(new VarDeclStatement(new IntType(), "b"),
                        new CompStatement(new AssignStatement("a", new ValueExpression(new IntValue(5))),
                                new CompStatement(new AssignStatement("b", new ValueExpression(new IntValue(7))),
                                        new PrintStatement(new RelationalExpression(new VariableExpression("a"), new VariableExpression("b"), "<")))))));

        // Ex 8: Heap Allocation
        list.add(new CompStatement(new VarDeclStatement(new RefType(new IntType()), "v"),
                new CompStatement(new HeapAllocation("v", new ValueExpression(new IntValue(20))),
                        new CompStatement(new VarDeclStatement(new RefType(new RefType(new IntType())), "a"),
                                new CompStatement(new HeapAllocation("a", new VariableExpression("v")),
                                        new CompStatement(new PrintStatement(new VariableExpression("v")),
                                                new PrintStatement(new VariableExpression("a"))))))));

        // Ex 9: Read Heap
        list.add(new CompStatement(new VarDeclStatement(new RefType(new IntType()), "v"),
                new CompStatement(new HeapAllocation("v", new ValueExpression(new IntValue(20))),
                        new CompStatement(new VarDeclStatement(new RefType(new RefType(new IntType())), "a"),
                                new CompStatement(new HeapAllocation("a", new VariableExpression("v")),
                                        new CompStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                                new PrintStatement(new ArithmeticExpression(
                                                        new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))),
                                                        new ValueExpression(new IntValue(5)), '+'))))))));

        // Ex 10: Write Heap
        list.add(new CompStatement(new VarDeclStatement(new RefType(new IntType()), "v"),
                new CompStatement(new HeapAllocation("v", new ValueExpression(new IntValue(20))),
                        new CompStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                new CompStatement(new WriteHeapStatement("v", new ValueExpression(new IntValue(30))),
                                        new PrintStatement(new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v")),
                                                new ValueExpression(new IntValue(5)), '+')))))));

        // Ex 11: Garbage Collector Test
        list.add(new CompStatement(new VarDeclStatement(new RefType(new IntType()), "v"),
                new CompStatement(new HeapAllocation("v", new ValueExpression(new IntValue(20))),
                        new CompStatement(new VarDeclStatement(new RefType(new RefType(new IntType())), "a"),
                                new CompStatement(new HeapAllocation("a", new VariableExpression("v")),
                                        new CompStatement(new HeapAllocation("v", new ValueExpression(new IntValue(30))),
                                                new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))))))))));

        // Ex 12: While Statement Test
        list.add(new CompStatement(new VarDeclStatement(new IntType(), "v"),
                new CompStatement(new AssignStatement("v", new ValueExpression(new IntValue(4))),
                        new CompStatement(new WhileStatement(new RelationalExpression(new VariableExpression("v"), new ValueExpression(new IntValue(0)), ">"),
                                new CompStatement(new PrintStatement(new VariableExpression("v")),
                                        new AssignStatement("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), '-')))),
                                new PrintStatement(new VariableExpression("v"))))));

        // Ex 13: Fork (Concurrency)
        list.add(new CompStatement(new VarDeclStatement(new IntType(), "v"),
                new CompStatement(new VarDeclStatement(new RefType(new IntType()), "a"),
                        new CompStatement(new AssignStatement("v", new ValueExpression(new IntValue(10))),
                                new CompStatement(new HeapAllocation("a", new ValueExpression(new IntValue(22))),
                                        new CompStatement(new ForkStatement(new CompStatement(new WriteHeapStatement("a", new ValueExpression(new IntValue(30))),
                                                new CompStatement(new AssignStatement("v", new ValueExpression(new IntValue(32))),
                                                        new CompStatement(new PrintStatement(new VariableExpression("v")),
                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))))))),
                                                new CompStatement(new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))))))))));

        return list;
    }
}