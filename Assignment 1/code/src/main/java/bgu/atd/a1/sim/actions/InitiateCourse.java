package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

import java.util.List;

public class InitiateCourse extends Action {
    private String courseName;
    private String department;
    private int space;
    private List<String> preCourses;

    public InitiateCourse(String courseName, String department, int space, List<String> pre) {
        this.courseName = courseName;
        this.department = department;
        this.space = space;
        this.preCourses = pre;
    }

    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    @Override
    protected void start() throws IllegalAccessException {
        if( !(this.actorState instanceof CoursePrivateState) )
            throw new IllegalAccessException("only CoursePrivateState actor could enter here");
        ((CoursePrivateState) this.actorState).getPrequisites().addAll(this.preCourses);
        ((CoursePrivateState) this.actorState).addPlaces(this.space);
        complete(true);
    }
}
