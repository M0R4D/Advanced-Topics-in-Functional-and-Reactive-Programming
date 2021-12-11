package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.List;

public class CheckCoursePrequisites extends Action {

    List<String> preCourses;

    public CheckCoursePrequisites(List<String> preCourses) {
        this.preCourses = preCourses;
    }
    
    
    
    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected void start() throws IllegalAccessException {
        if( !(this.actorState instanceof StudentPrivateState) )
            throw new IllegalAccessException("only StudentPrivateState actor could enter here");
        for ( String course : preCourses ) {
            if ( !((StudentPrivateState) this.actorState).getGrades().containsKey(course)) {
                complete(false );
                // TODO
                return;
            }
            complete(true);
        }
    }
}
