package view;

import model.statement.*;
import model.expression.*;
import model.type.*;
import model.value.*;
import model.state.*;
import repository.IRepository;
import repository.Repo;
import controller.Controller;
import model.exception.*;

import java.util.ArrayList;
import java.util.Scanner;

public class View {
    private View() {
    }

    static void main() {
        // Example 1: int v; v=2; Print(v)
        Statement ex1 =
                new CompStatement(
                        new VarDeclStatement(new IntType(), "v"),
                        new CompStatement(
                                new AssignStatement("v", new ValueExpression(new IntValue(2))),
                                new PrintStatement(new VariableExpression("v"))));

        // Example 2: int a; int b; a=2+3*5; b=a+1; Print(b)
        Statement ex2 =
                new CompStatement(
                        new VarDeclStatement(new IntType(), "a"),
                        new CompStatement(
                                new VarDeclStatement(new IntType(), "b"),
                                new CompStatement(
                                        new AssignStatement(
                                                "a",
                                                new ArithmeticExpression(
                                                        new ValueExpression(new IntValue(2)),
                                                        new ArithmeticExpression(
                                                                new ValueExpression(new IntValue(3)),
                                                                new ValueExpression(new IntValue(5)),
                                                                '*'),
                                                        '+')),
                                        new CompStatement(
                                                new AssignStatement(
                                                        "b",
                                                        new ArithmeticExpression(
                                                                new VariableExpression("a"),
                                                                new ValueExpression(new IntValue(1)),
                                                                '+')),
                                                new PrintStatement(new VariableExpression("b"))))));

        // Example 3: bool a; int v; a=true; (IF a THEN v=2 ELSE v=3); Print(v)
        Statement ex3 =
                new CompStatement(
                        new VarDeclStatement(new BoolType(), "a"),
                        new CompStatement(
                                new VarDeclStatement(new IntType(), "v"),
                                new CompStatement(
                                        new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                                        new CompStatement(
                                                new IfStatement(
                                                        new VariableExpression("a"),
                                                        new AssignStatement("v", new ValueExpression(new IntValue(2))),
                                                        new AssignStatement("v", new ValueExpression(new IntValue(3)))),
                                                new PrintStatement(new VariableExpression("v"))))));


        // Example 4 (division by zero): int a; int b; a = 100; b = 0; print(a/b)
        Statement ex4 =
                new CompStatement(
                        new VarDeclStatement(new IntType(), "a"),
                        new CompStatement(
                                new VarDeclStatement(new IntType(), "b"),
                                new CompStatement(
                                        new AssignStatement("a", new ValueExpression(new IntValue(100))),
                                        new CompStatement(
                                                new AssignStatement("b", new ValueExpression(new IntValue(0))),
                                                new PrintStatement(
                                                        new ArithmeticExpression(
                                                                new VariableExpression("a"), new VariableExpression("b"), '/'))))));

        // Example 5 (type error): int x; x = true
        Statement ex5 =
                new CompStatement(
                        new VarDeclStatement(new IntType(), "x"),
                        new AssignStatement("x", new ValueExpression(new BoolValue(true))));

        // Example 6 (for files): string varf; varf=<<test.in>>; OpenRF(varf); int varc; RF(varf, varc)
        // Print(varc); RF(varf, varc); Print(varc); CloseRF(varf)
        Statement ex6 =
                new CompStatement(
                        new VarDeclStatement(new StringType(), "varf"),
                        new CompStatement(
                                new AssignStatement("varf", new ValueExpression(new StringValue("test.in"))),
                                new CompStatement(
                                        new OpenRFileStatement(new VariableExpression("varf")),
                                        new CompStatement(
                                                new VarDeclStatement(new IntType(), "varc"),
                                                new CompStatement(
                                                        new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                        new CompStatement(
                                                                new PrintStatement(new VariableExpression("varc")),
                                                                new CompStatement(
                                                                        new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                                        new CompStatement(
                                                                                new PrintStatement(new VariableExpression("varc")),
                                                                                new CloseRFileStatement(
                                                                                        new VariableExpression("varf"))))))))));

        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            System.out.print("Choose a program (0–6): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> runProgram(ex1);
                case "2" -> runProgram(ex2);
                case "3" -> runProgram(ex3);
                case "4" -> runProgram(ex4);
                case "5" -> runProgram(ex5);
                case "6" -> runProgram(ex6);
                case "0" -> {
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void printMenu() {
        IO.println("\n=== Toy Language Interpreter Menu ===");
        IO.println("1. Example 1: int v; v=2; Print(v)");
        IO.println("2. Example 2: int a; int b; a=2+3*5; b=a+1; Print(b)");
        IO.println("3. Example 3: bool a; int v; a=true; (IF a THEN v=2 ELSE v=3); Print(v)");
        IO.println("4. Example 4 (error - division by zero): int a; int b; a = 100; b = 0; Print(a/b)");
        IO.println("5. Example 5 (type error): int x; x = true;");
        IO.println("6. Example 6 (for files) string varf; varf=<<test.in>>; OpenRF(varf); int varc; RF(varf, varc); Print(varc); RF(varf, varc); Print(varc); CloseRF(varf);");
        IO.println("0. Exit");
    }

    private static void runProgram(Statement program) {
        try {
            // create the ADTs for the program state
            Stack<Statement> exeStack = new Stack<>();
            Dictionary<Value> symTable = new Dictionary<>();
            List<Value> out = new List<>();
            IFileTable fileTable = new FileTable();
            //Load program into the exeStack
            exeStack.push(program);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Log File Path: ");
            String logFilePath = scanner.nextLine();

            //Create the program state
            ProgramState progState = new ProgramState(exeStack, symTable, out, fileTable);

            //Create a list of program states
            java.util.List<ProgramState> programStates = new ArrayList<>();
            programStates.add(progState);


            // create the repo and the controller
            IRepository repo = new Repo(programStates, logFilePath);
            Controller controller = new Controller(repo);

            // ask for display flag
            String displayFlag;
            while (true) {
                System.out.print("Display program state after each step? (yes/no): ");
                displayFlag = scanner.nextLine().trim().toLowerCase();

                if (displayFlag.equals("yes") || displayFlag.equals("no")) {
                    break;
                }
                System.out.println("Invalid input. Please type 'yes' or 'no'.");
            }

            controller.setFlag(displayFlag.equals("yes"));


            // run the program
            System.out.println("Running program...");
            controller.executeAllSteps();

            // print the final output
            System.out.println("\nEND\n");
            System.out.println(repo.getCrtPrg().toString());

        } catch (ExpressionException | StatementException ee) {
            System.out.println("ERROR: " + ee.getMessage());
        }
    }
}
