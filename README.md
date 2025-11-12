# Java Toy Language Interpreter

This project is an interpreter for a **simple toy programming language**, implemented in **Java**.  
It demonstrates the core principles of **interpreter design** and the **Model-View-Controller (MVC)** architectural pattern.

---

## 📋 Project Overview

The interpreter executes programs written in a small, procedural language.  
It maintains and updates the **program’s state** using three core components:

* **Execution Stack (`ExeStack`)** – stores the statements yet to be executed  
* **Symbol Table (`SymTable`)** – maps variable names to their current values  
* **Output List (`Out`)** – collects printed values during execution  

Together, these form the **`ProgramState`**, representing the complete state of a running program.

---

## 🚀 Features (Language Specification)

The toy language supports basic data types, expressions, and control structures.

### **Types**
* `int` – Integer type  
* `bool` – Boolean type  

### **Expressions**
* **Values:** `Number` (e.g., `5`), `True`, `False`  
* **Variables:** `Id` (e.g., `v`, `a`)  
* **Arithmetic Expressions:** `Exp1 + Exp2`, `Exp1 - Exp2`, `Exp1 * Exp2`, `Exp1 / Exp2`  
* **Logical Expressions:** `Exp1 and Exp2`, `Exp1 or Exp2`  

### **Statements**
* **Variable Declaration:** `Type Id` (e.g., `int v`)  
* **Assignment:** `Id = Exp` (e.g., `v = 2`)  
* **Compound Statement:** `Stmt1; Stmt2`  
* **Print:** `Print(Exp)`  
* **If Statement:** `If Exp Then Stmt1 Else Stmt2`  
* **No Operation:** `nop`  

---

## 🏛️ Project Structure (MVC)

The project strictly follows the **Model-View-Controller (MVC)** pattern.

### **1. Model**
Contains all domain classes, data types, and logic.
* `model/statement/` – contains `IStmt` interface and concrete statements (`AssignStmt`, `IfStmt`, `PrintStmt`, `VarDeclStmt`, `NopStmt`, etc.)
* `model/expression/` – contains `IExp` interface and implementations (`ArithExp`, `LogicExp`, `VarExp`, `ValueExp`)
* `model/type/` – defines data types (`IntType`, `BoolType`)
* `model/value/` – defines runtime values (`IntValue`, `BoolValue`)
* `model/state/` – defines `ProgramState` and generic ADTs (`MyIStack`, `MyIDictionary`, `MyIList`)
* `model/exception/` – custom exception classes for runtime and ADT errors (`MyException`, etc.)

### **2. Repository**
Handles storage and management of program states.
* `repository/IRepository.java` – repository interface  
* `repository/MyListRepository.java` – concrete implementation using a list of `ProgramState` objects  

### **3. Controller**
Coordinates execution between the Model and the View.
* `controller/MyController.java` – implements:
  * `oneStep()` – executes a single statement  
  * `allStep()` – executes all program statements until completion  
Handles runtime exceptions and supports optional program state visualization after each step.

### **4. View**
Provides a simple **text-based interface** for interacting with the interpreter.
* `view/View.java` – main entry point of the project  
  * Displays a menu of predefined example programs  
  * Allows users to run and inspect program execution  

---

## ▶️ How to Run

1. **Clone or download** the repository.  
2. **Compile** the project in your preferred IDE (e.g., IntelliJ, Eclipse) or using the `javac` command.  
3. **Run** the `main()` method from `view/View.java`.  
4. A **text menu** will appear in the console with available toy programs.  
5. Choose a program to execute and select whether to display intermediate program states.  
6. The **final output** will be printed in the console.

---

## 🧪 Examples

The `View` class comes pre-loaded with the following examples from the assignment:

**Example 1**
int v; v=2; Print(v)
**Expected Output:** `2`

---

**Example 2**
int a; a=2+3*5; int b; b=a-4/2+7; Print(b)
**Expected Output:** `22` (Note: `a=17`, `b=17-2+7=22`)

---

**Example 3**
bool a; a=false; int v; If a Then v=2 Else v=3; Print(v) 
**Expected Output:** `3`

---

### Exception Handling Examples

These examples are included to demonstrate the custom exception handling for runtime errors.

**Example 4 (Type Mismatch Error)**
bool a; a = 7
**Expected Output:** `Error: declared type of variable a and type of the assigned expression do not match`

---

**Example 5 (Division by Zero Error)**
int a; int b; a = 7; b = 0; print(a/b)
**Expected Output:** `Error: division by zero`
