package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.LinkedList;
import java.util.List;

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
    protected void start() throws IllegalAccessException {
        if( !(this.actorState instanceof DepartmentPrivateState) )
            throw new IllegalAccessException("only DepartmentPrivateState actor could enter here");
        if( !((DepartmentPrivateState) this.actorState).getCourseList().contains(this.course) ) {
            complete(false + "Course \"" + this.course + "\" is is not exist");
            return;
        }

        List<Action> unregisterCourse = new LinkedList<>();
        Action unregisterCourseAction = new UnregisterCourse(this.course);
        unregisterCourse.add(unregisterCourseAction);
        then(unregisterCourse, () -> {
            if((boolean) unregisterCourse.get(0).getResult().get() ) {
                complete(true + "course \"" + this.course + "\" was closed successfully");
            }
        });
        sendMessage(unregisterCourseAction, this.course, new CoursePrivateState());
    }
}
