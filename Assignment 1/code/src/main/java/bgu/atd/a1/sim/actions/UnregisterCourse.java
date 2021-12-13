package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class UnregisterCourse extends Action {

    private String course;

    public UnregisterCourse(String course) {
        this.course = course;
    }

    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    @Override
    protected void start() throws IllegalAccessException {
        if( !(this.actorState instanceof CoursePrivateState) )
            throw new IllegalAccessException("only CoursePrivateState actor could enter here");
        if ( ((CoursePrivateState) this.actorState).getAvailableSpots() == -1 ) {
            complete(false + "Course \"" + this.course + "\" was closed earlier");
            return;
        }

        ((CoursePrivateState) this.actorState).closeCourse();
        List<Action> unregisterStudentsFromCourse = new LinkedList<>();
        for (String student : ((CoursePrivateState) this.actorState).getRegStudents()) {
            Action removeCourseForStudent = new RemoveCourseFromGradesSheet(student, this.course);
            unregisterStudentsFromCourse.add(removeCourseForStudent);
            sendMessage(removeCourseForStudent, student, new StudentPrivateState());
        }
        then(unregisterStudentsFromCourse, () -> {
            for (Action action : unregisterStudentsFromCourse) {
                if( !(boolean) action.getResult().get()) {
                    complete(false);
                    return;
                }
            }
            complete(true + "Course \"" + this.course + "\" closed");
        });
    }
}
