package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

public class RemoveCourseFromGradesSheet extends Action {

    private String studentId;
    private String course;

    public RemoveCourseFromGradesSheet(String studentId, String course) {
        this.studentId = studentId;
        this.course = course;
    }


    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    @Override
    protected void start() throws IllegalAccessException {
        if( !(this.actorState instanceof StudentPrivateState) )
            throw new IllegalAccessException("only StudentPrivateState actor could enter here");
        if ( ((StudentPrivateState) this.actorState).getGrades().containsKey(this.course) )
            ((StudentPrivateState) this.actorState).getGrades().remove(this.course);
        complete(true);
    }
}
