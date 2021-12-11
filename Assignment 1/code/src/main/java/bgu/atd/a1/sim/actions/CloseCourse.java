package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;

public class CloseCourse extends Action {
    private String course;

    public CloseCourse(String course) {
        setActionName("Close Course");
        this.course = course;
    }

    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected void start() {

    }
}
