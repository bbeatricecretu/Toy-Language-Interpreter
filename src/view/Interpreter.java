package view;

import controller.Controller;
import model.exception.TypeCheckException;
import model.expression.*;
import model.statement.*;
import model.type.*;
import model.value.*;
import model.state.*;
import repository.*;

import java.util.ArrayList;

public class Interpreter {
    public static void main(String[] args) {

        // Examples remain same as previous definition, logic updated below

        Statement ex1 = new CompStatement(
                new VarDeclStatement(new IntType(), "v"),
                new CompStatement(
                        new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))
                )
        );

        Statement ex2 = new CompStatement(
                new VarDeclStatement(new IntType(), "a"),
                new CompStatement(
                        new VarDeclStatement(new IntType(), "b"),
                        new CompStatement(
                                new AssignStatement("a", new ArithmeticExpression(new ValueExpression(new IntValue(2)), new ArithmeticExpression(new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5)), '*'), '+')),
                                new CompStatement(
                                        new AssignStatement("b", new ArithmeticExpression(new VariableExpression("a"), new ValueExpression(new IntValue(1)), '+')),
                                        new PrintStatement(new VariableExpression("b"))
                                )
                        )
                )
        );

        Statement ex3 = new CompStatement(
                new VarDeclStatement(new BoolType(), "a"),
                new CompStatement(
                        new VarDeclStatement(new IntType(), "v"),
                        new CompStatement(
                                new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompStatement(
                                        new IfStatement(new VariableExpression("a"),
                                                new AssignStatement("v", new ValueExpression(new IntValue(2))),
                                                new AssignStatement("v", new ValueExpression(new IntValue(3)))),
                                        new PrintStatement(new VariableExpression("v"))
                                )
                        )
                )
        );

        Statement ex4 = new CompStatement(
                new VarDeclStatement(new IntType(), "a"),
                new CompStatement(
                        new VarDeclStatement(new IntType(), "b"),
                        new CompStatement(
                                new AssignStatement("a", new ValueExpression(new IntValue(100))),
                                new CompStatement(
                                        new AssignStatement("b", new ValueExpression(new IntValue(0))),
                                        new PrintStatement(new ArithmeticExpression(new VariableExpression("a"), new VariableExpression("b"), '/'))
                                )
                        )
                )
        );

        Statement ex5 = new CompStatement(
                new VarDeclStatement(new BoolType(), "x"),
                new AssignStatement("x", new ValueExpression(new BoolValue(true)))
        );

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

        Statement ex7 = new CompStatement(
                new VarDeclStatement(new IntType(), "a"),
                new CompStatement(
                        new VarDeclStatement(new IntType(), "b"),
                        new CompStatement(
                                new AssignStatement("a", new ValueExpression(new IntValue(5))),
                                new CompStatement(
                                        new AssignStatement("b", new ValueExpression(new IntValue(7))),
                                        new PrintStatement(new RelationalExpression(new VariableExpression("a"), new VariableExpression("b"), "<"))
                                )
                        )
                )
        );

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

        Statement ex9 = new CompStatement(
                new VarDeclStatement(new RefType(new IntType()), "v"),
                new CompStatement(
                        new HeapAllocation("v", new ValueExpression(new IntValue(20))),
                        new CompStatement(
                                new VarDeclStatement(new RefType(new RefType(new IntType())), "a"),
                                new CompStatement(
                                        new HeapAllocation("a", new VariableExpression("v")),
                                        new CompStatement(
                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                                new PrintStatement(new ArithmeticExpression(
                                                        new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))),
                                                        new ValueExpression(new IntValue(5)), '+'
                                                ))
                                        )
                                )
                        )
                )
        );

        Statement ex10 = new CompStatement(
                new VarDeclStatement(new RefType(new IntType()), "v"),
                new CompStatement(
                        new HeapAllocation("v", new ValueExpression(new IntValue(20))),
                        new CompStatement(
                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                new CompStatement(
                                        new WriteHeapStatement("v", new ValueExpression(new IntValue(30))),
                                        new PrintStatement(new ArithmeticExpression(
                                                new ReadHeapExpression(new VariableExpression("v")),
                                                new ValueExpression(new IntValue(5)), '+'
                                        ))
                                )
                        )
                )
        );

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
                                                new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))))
                                        )
                                )
                        )
                )
        );

        Statement ex12 = new CompStatement(
                new VarDeclStatement(new IntType(), "v"),
                new CompStatement(
                        new AssignStatement("v", new ValueExpression(new IntValue(4))),
                        new CompStatement(
                                new WhileStatement(
                                        new RelationalExpression(new VariableExpression("v"), new ValueExpression(new IntValue(0)), ">"),
                                        new CompStatement(
                                                new PrintStatement(new VariableExpression("v")),
                                                new AssignStatement("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), '-'))
                                        )
                                ),
                                new PrintStatement(new VariableExpression("v"))
                        )
                )
        );

        // Testing Fork (Concurrent Example)
        Statement ex13 = new CompStatement(
                new VarDeclStatement(new IntType(), "v"),
                new CompStatement(
                        new VarDeclStatement(new RefType(new IntType()), "a"),
                        new CompStatement(
                                new AssignStatement("v", new ValueExpression(new IntValue(10))),
                                new CompStatement(
                                        new HeapAllocation("a", new ValueExpression(new IntValue(22))),
                                        new CompStatement(
                                                new ForkStatement(new CompStatement(
                                                        new WriteHeapStatement("a", new ValueExpression(new IntValue(30))),
                                                        new CompStatement(
                                                                new AssignStatement("v", new ValueExpression(new IntValue(32))),
                                                                new CompStatement(
                                                                        new PrintStatement(new VariableExpression("v")),
                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                                                )
                                                        )
                                                )),
                                                new CompStatement(
                                                        new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                                )
                                        )
                                )
                        )
                )
        );
        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "Exit"));

        addExample(menu, ex1, "1", "int v; v=2; print(v)", "log1.txt");
        addExample(menu, ex2, "2", "int a; int b; a=2+3*5; b=a+1; print(b)", "log2.txt");
        addExample(menu, ex3, "3", "bool a; int v; a=true; (IF a THEN v=2 ELSE v=3); print(v)", "log3.txt");
        addExample(menu, ex4, "4", "int a; int b; a=100; b=0; print(a/b)", "log4.txt");
        addExample(menu, ex5, "5", "int x; x=true (type error)", "log5.txt");
        addExample(menu, ex6, "6", "file example with open/read/print/close", "log6.txt");
        addExample(menu, ex7, "7", "int a; int b; a=5; b=7; print(a<b)", "log7.txt");
        addExample(menu, ex8, "8", "Ref int v; new(v,20); Ref Ref int a; new(a,v); print(v); print(a)", "log8.txt");
        addExample(menu, ex9, "9", "Ref int v; new(v,20); Ref Ref int a; new(a,v); print(rH(v)); print(rH(rH(a))+5)", "log9.txt");
        addExample(menu, ex10, "10", "Ref int v; new(v,20); print(rH(v)); wH(v,30); print(rH(v)+5)", "log10.txt");
        addExample(menu, ex11, "11", "GC test", "log11.txt");
        addExample(menu, ex12, "12", "while test", "log12.txt");
        addExample(menu, ex13, "13", "fork test", "log13.txt");

        menu.show();
    }

    private static void addExample(TextMenu menu, Statement stmt, String key, String desc, String logFile) {
        try {
            stmt.typecheck(new Dictionary<>());
            Controller controller = makeController(stmt, logFile);
            menu.addCommand(new RunExample(key, desc, controller));
        } catch (TypeCheckException e) {
            System.out.println("TypeCheck error for example " + key + ": " + e.getMessage());
        }
    }

    private static Controller makeController(Statement stmt, String logFile) {
        IStack<Statement> stack = new Stack<>();
        stack.push(stmt);
        IDictionary<Value> symTable = new Dictionary<>();
        model.state.IList<Value> out = new model.state.List<>();
        IFileTable fileTable = new FileTable();
        IHeap heap = new Heap();

        // 3. Create the initial ProgramState
        ProgramState initialProgram = new ProgramState(stack, symTable, out, fileTable, heap);

        // 4. Repository List (MUST be java.util.List for Concurrency/Streams)
        java.util.List<ProgramState> prgList = new java.util.ArrayList<>();
        prgList.add(initialProgram);

        // 5. Initialize Repo and Controller
        IRepository repo = new Repo(prgList, logFile);
        return new Controller(repo);
    }
}