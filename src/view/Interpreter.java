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
        // int v; v = 2; print(v);
        Statement ex1 = new CompStatement(
                new VarDeclStatement(new IntType(), "v"),
                new CompStatement(
                        new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))
                )
        );

        // === Example 2 ===
        // int a; int b; a = 2 + 3 * 5; b = a + 1; print(b);
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
        // bool a; int v; a = true;
        // if (a) then v = 2 else v = 3; print(v);
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
        // int a; int b; a = 100; b = 0; print(a / b);   // division by zero
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
        // int x; x = true;   // type error
        Statement ex5 = new CompStatement(
                new VarDeclStatement(new IntType(), "x"),
                new AssignStatement("x", new ValueExpression(new BoolValue(true)))
        );

        // === Example 6 ===
        // string varf; varf="test.in"; openRFile(varf);
        // int varc; readFile(varf,varc); print(varc);
        // readFile(varf,varc); print(varc); closeRFile(varf);
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
        // int a; int b; a = 5; b = 7; print(a < b);
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

        // === Example 8 === (heap allocation)
        // Ref int v; new(v,20); Ref Ref int a; new(a,v); print(v); print(a)
        Statement ex8 = new CompStatement(
                new VarDeclStatement(new RefType(new IntType()), "v"),
                new CompStatement(
                        new HeapAllocation("v", new ValueExpression(new IntValue(20))),
                        new CompStatement(
                                new VarDeclStatement(new RefType(new RefType(new IntType())), "a"),
                                new CompStatement(
                                        new HeapAllocation("a", new VariableExpression("v")),
                                        new CompStatement(
                                                new PrintStatement(new VariableExpression("v")),
                                                new PrintStatement(new VariableExpression("a"))
                                        )
                                )
                        )
                )
        );

        // === Example 9 === (heap reading)
        // Ref int v; new(v,20); Ref Ref int a; new(a,v);
        // print(rH(v)); print(rH(rH(a)) + 5);
        Statement ex9 = new CompStatement(
                new VarDeclStatement(new RefType(new IntType()), "v"),
                new CompStatement(
                        new HeapAllocation("v", new ValueExpression(new IntValue(20))),
                        new CompStatement(
                                new VarDeclStatement(new RefType(new RefType(new IntType())), "a"),
                                new CompStatement(
                                        new HeapAllocation("a", new VariableExpression("v")),
                                        new CompStatement(
                                                new PrintStatement(
                                                        new ReadHeapExpression(new VariableExpression("v"))
                                                ),
                                                new PrintStatement(
                                                        new ArithmeticExpression(
                                                                new ReadHeapExpression(
                                                                        new ReadHeapExpression(
                                                                                new VariableExpression("a")
                                                                        )
                                                                ),
                                                                new ValueExpression(new IntValue(5)),
                                                                '+'
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        // === Example 10 === (heap writing)
        // Ref int v; new(v,20); print(rH(v)); wH(v,30); print(rH(v)+5);
        Statement ex10 = new CompStatement(
                new VarDeclStatement(new RefType(new IntType()), "v"),
                new CompStatement(
                        new HeapAllocation("v", new ValueExpression(new IntValue(20))),
                        new CompStatement(
                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                new CompStatement(
                                        new WriteHeapStatement("v", new ValueExpression(new IntValue(30))),
                                        new PrintStatement(
                                                new ArithmeticExpression(
                                                        new ReadHeapExpression(new VariableExpression("v")),
                                                        new ValueExpression(new IntValue(5)),
                                                        '+'
                                                )
                                        )
                                )
                        )
                )
        );

        // === Example 11 === (garbage collector)
        // Ref int v; new(v,20); Ref Ref int a; new(a,v); new(v,30); print(rH(rH(a)));
        Statement ex11 = new CompStatement(
                new VarDeclStatement(new RefType(new IntType()), "v"),
                new CompStatement(
                        new HeapAllocation("v", new ValueExpression(new IntValue(20))),
                        new CompStatement(
                                new VarDeclStatement(new RefType(new RefType(new IntType())), "a"),
                                new CompStatement(
                                        new HeapAllocation("a", new VariableExpression("v")),
                                        new CompStatement(
                                                new HeapAllocation("v", new ValueExpression(new IntValue(30))),
                                                new PrintStatement(
                                                        new ReadHeapExpression(
                                                                new ReadHeapExpression(
                                                                        new VariableExpression("a")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        // === Example 12 === (while)
        // int v; v = 4; (while (v > 0) print(v); v = v - 1); print(v)
        Statement ex12 = new CompStatement(
                new VarDeclStatement(new IntType(), "v"),
                new CompStatement(
                        new AssignStatement("v", new ValueExpression(new IntValue(4))),
                        new CompStatement(
                                new WhileStatement(
                                        new RelationalExpression(
                                                new VariableExpression("v"),
                                                new ValueExpression(new IntValue(0)),
                                                ">"
                                        ),
                                        new CompStatement(
                                                new PrintStatement(new VariableExpression("v")),
                                                new AssignStatement(
                                                        "v",
                                                        new ArithmeticExpression(
                                                                new VariableExpression("v"),
                                                                new ValueExpression(new IntValue(1)),
                                                                '-'
                                                        )
                                                )
                                        )
                                ),
                                new PrintStatement(new VariableExpression("v"))
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
        Controller c8 = makeController(ex8, "log8.txt");
        Controller c9 = makeController(ex9, "log9.txt");
        Controller c10 = makeController(ex10, "log10.txt");
        Controller c11 = makeController(ex11, "log11.txt");
        Controller c12 = makeController(ex12, "log12.txt");

        // === Menu ===
        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "Exit"));

        menu.addCommand(new RunExample("1", "int v; v=2; print(v)", c1));
        menu.addCommand(new RunExample("2", "int a; int b; a=2+3*5; b=a+1; print(b)", c2));
        menu.addCommand(new RunExample("3", "bool a; int v; a=true; (IF a THEN v=2 ELSE v=3); print(v)", c3));
        menu.addCommand(new RunExample("4", "int a; int b; a=100; b=0; print(a/b)", c4));
        menu.addCommand(new RunExample("5", "int x; x=true (type error)", c5));
        menu.addCommand(new RunExample("6", "file example with open/read/print/close", c6));
        menu.addCommand(new RunExample("7", "int a; int b; a=5; b=7; print(a<b)", c7));

        menu.addCommand(new RunExample("8", "Ref int v; new(v,20); Ref Ref int a; new(a,v); print(v); print(a)", c8));
        menu.addCommand(new RunExample("9", "Ref int v; new(v,20); Ref Ref int a; new(a,v); print(rH(v)); print(rH(rH(a))+5)", c9));
        menu.addCommand(new RunExample("10", "Ref int v; new(v,20); print(rH(v)); wH(v,30); print(rH(v)+5)", c10));
        menu.addCommand(new RunExample("11", "GC test: Ref int v; new(v,20); Ref Ref int a; new(a,v); new(v,30); print(rH(rH(a)))", c11));
        menu.addCommand(new RunExample("12", "int v; v=4; (while (v>0) print(v); v=v-1); print(v)", c12));

        menu.show();
    }

    private static Controller makeController(Statement stmt, String logFile) {
        Stack<Statement> stack = new Stack<>();
        Dictionary<Value> dict = new Dictionary<>();
        List<Value> out = new List<>();
        IFileTable fileTable = new FileTable();
        IHeap heap = new Heap();

        stack.push(stmt);
        ProgramState ps = new ProgramState(stack, dict, out, fileTable, heap);

        java.util.List<ProgramState> list = new ArrayList<>();
        list.add(ps);

        IRepository repo = new Repo(list, logFile);
        return new Controller(repo);
    }
}
