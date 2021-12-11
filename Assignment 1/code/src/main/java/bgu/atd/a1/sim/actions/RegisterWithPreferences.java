package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;

import java.util.List;

public class RegisterWithPreferences extends Action {
    private String student;
    private List<String> preferences;

    public RegisterWithPreferences(String student, List<String> preferences) {
        setActionName("Register With Preferences");
        this.student = student;
        this.preferences = preferences;
    }

    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected void start() {

    }
}
