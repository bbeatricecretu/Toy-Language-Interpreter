package view;

import controller.Controller;
import model.exception.IException;

public class RunExample extends Command {
    private final Controller controller;

    public RunExample(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            controller.executeAllSteps();
        } catch (IException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
