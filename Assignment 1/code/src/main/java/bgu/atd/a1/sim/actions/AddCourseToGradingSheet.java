package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

public class AddCourseToGradingSheet extends Action {

    private String course;
    private int grade;

    public AddCourseToGradingSheet(String course, int grade) {

    }

    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    @Override
    protected void start() throws IllegalAccessException {
        if( !(this.actorState instanceof StudentPrivateState) )
            throw new IllegalAccessException("only StudentPrivateState actor could enter here");
        ((StudentPrivateState) this.actorState).getGrades().put(this.course, this.grade);
        complete(true);
    }

}
