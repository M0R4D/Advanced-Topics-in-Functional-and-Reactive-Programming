package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

public class AddCoursePlaces extends Action {
    private String course;
    private int places;

    public AddCoursePlaces(String course, int places) {

        setActionName("Add Spaces");
        this.course = course;
        this.places = places;

    }

    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected void start() throws IllegalAccessException {
        if( !(this.actorState instanceof CoursePrivateState) )
            throw new IllegalAccessException("only CoursePrivateState actor could enter here");
        ((CoursePrivateState) this.actorState).addPlaces(places);
        complete(places + " new places added to the the course \"" + this.actorId.toUpperCase() + "\"\n\tThe amount of current available places now is: " + ((CoursePrivateState) this.actorState).getAvailableSpots());
    }
}
