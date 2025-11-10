package view;

import controller.Controller;
import model.expression.*;
import model.statement.*;
import model.type.*;
import model.value.*;
import model.state.*;
import repository.*;

import java.util.ArrayList;

public class Interpreter {
    public static void main(String[] args) {

        // === Example 1 ===
        // int v;
        // v = 2;
        // print(v);
        Statement ex1 = new CompStatement(
                new VarDeclStatement(new IntType(), "v"),
                new CompStatement(
                        new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))
                )
        );

        // === Example 2 ===
        // int a;
        // int b;
        // a = 2 + 3 * 5;
        // b = a + 1;
        // print(b);
        Statement ex2 = new CompStatement(
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
                                                        '*'
                                                ),
                                                '+'
                                        )
                                ),
                                new CompStatement(
                                        new AssignStatement(
                                                "b",
                                                new ArithmeticExpression(
                                                        new VariableExpression("a"),
                                                        new ValueExpression(new IntValue(1)),
                                                        '+'
                                                )
                                        ),
                                        new PrintStatement(new VariableExpression("b"))
                                )
                        )
                )
        );

        // === Example 3 ===
        // bool a;
        // int v;
        // a = true;
        // if (a) then v = 2 else v = 3;
        // print(v);
        Statement ex3 = new CompStatement(
                new VarDeclStatement(new BoolType(), "a"),
                new CompStatement(
                        new VarDeclStatement(new IntType(), "v"),
                        new CompStatement(
                                new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompStatement(
                                        new IfStatement(
                                                new VariableExpression("a"),
                                                new AssignStatement("v", new ValueExpression(new IntValue(2))),
                                                new AssignStatement("v", new ValueExpression(new IntValue(3)))
                                        ),
                                        new PrintStatement(new VariableExpression("v"))
                                )
                        )
                )
        );

        // === Example 4 ===
        // int a;
        // int b;
        // a = 100;
        // b = 0;
        // print(a / b);   // (will throw division by zero)
        Statement ex4 = new CompStatement(
                new VarDeclStatement(new IntType(), "a"),
                new CompStatement(
                        new VarDeclStatement(new IntType(), "b"),
                        new CompStatement(
                                new AssignStatement("a", new ValueExpression(new IntValue(100))),
                                new CompStatement(
                                        new AssignStatement("b", new ValueExpression(new IntValue(0))),
                                        new PrintStatement(
                                                new ArithmeticExpression(
                                                        new VariableExpression("a"),
                                                        new VariableExpression("b"),
                                                        '/'
                                                )
                                        )
                                )
                        )
                )
        );

        // === Example 5 ===
        // int x;
        // x = true;   // type error (cannot assign bool to int)
        Statement ex5 = new CompStatement(
                new VarDeclStatement(new IntType(), "x"),
                new AssignStatement("x", new ValueExpression(new BoolValue(true)))
        );

        // === Example 6 ===
        // string varf;
        // varf = "test.in";
        // openRFile(varf);
        // int varc;
        // readFile(varf, varc);
        // print(varc);
        // readFile(varf, varc);
        // print(varc);
        // closeRFile(varf);
        Statement ex6 = new CompStatement(
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
                                                                        new CloseRFileStatement(new VariableExpression("varf"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        // === Example 7 ===
        // int a;
        // int b;
        // a = 5;
        // b = 7;
        // print(a < b);   // prints true
        Statement ex7 = new CompStatement(
                new VarDeclStatement(new IntType(), "a"),
                new CompStatement(
                        new VarDeclStatement(new IntType(), "b"),
                        new CompStatement(
                                new AssignStatement("a", new ValueExpression(new IntValue(5))),
                                new CompStatement(
                                        new AssignStatement("b", new ValueExpression(new IntValue(7))),
                                        new PrintStatement(
                                                new RelationalExpression(
                                                        new VariableExpression("a"),
                                                        new VariableExpression("b"),
                                                        "<"
                                                )
                                        )
                                )
                        )
                )
        );


        // === Build controllers and repos ===
        Controller c1 = makeController(ex1, "log1.txt");
        Controller c2 = makeController(ex2, "log2.txt");
        Controller c3 = makeController(ex3, "log3.txt");
        Controller c4 = makeController(ex4, "log4.txt");
        Controller c5 = makeController(ex5, "log5.txt");
        Controller c6 = makeController(ex6, "log6.txt");
        Controller c7 = makeController(ex7, "log7.txt");

        // === Menu ===
        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "Exit"));
        menu.addCommand(new RunExample("1", ex1.toString(), c1));
        menu.addCommand(new RunExample("2", ex2.toString(), c2));
        menu.addCommand(new RunExample("3", ex3.toString(), c3));
        menu.addCommand(new RunExample("4", ex4.toString(), c4));//division by zero error
        menu.addCommand(new RunExample("5", ex5.toString(), c5));// type error
        menu.addCommand(new RunExample("6", ex6.toString(), c6)); //for files
        menu.addCommand(new RunExample("7", ex7.toString(), c7)); //for realational expression

        menu.show();
    }

    private static Controller makeController(Statement stmt, String logFile) {
        Stack<Statement> stack = new Stack<>();
        Dictionary<Value> dict = new Dictionary<>();
        List<Value> out = new List<>();
        IFileTable fileTable = new FileTable();

        stack.push(stmt);
        ProgramState ps = new ProgramState(stack, dict, out, fileTable);
        java.util.List<ProgramState> l = new ArrayList<>();
        l.add(ps);
        IRepository repo = new Repo(l, logFile);
        return new Controller(repo);
    }
}
