package view;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextMenu {
    private final Map<String, Command> commands = new HashMap<>();

    public void addCommand(Command c) {
        commands.put(c.getKey(), c);
    }

    private void printMenu() {
        System.out.println("\n=== Toy Language Interpreter ===");
        for (Command c : commands.values()) {
            System.out.printf("%4s : %s%n", c.getKey(), c.getDescription());
        }
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (commands.size() <= 1) {
                System.out.println("All examples have been executed. Exiting...");
                break;
            }

            printMenu();
            System.out.print("Option: ");
            String key = scanner.nextLine().trim();

            Command command = commands.get(key);
            if (command == null) {
                System.out.println("Invalid option. Available: " + commands.keySet());
                continue;
            }

            command.execute();
            if (!key.equals("0")) {
                commands.remove(key);
                System.out.println("Example " + key + " removed — cannot be re-run.\n");
            }
        }
    }
}
