package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class Unregister extends Action {
    private String studentId;
    private String course;



    public Unregister(String studentId, String course) {
        // { "Action": "Unregister", "Student": "123456789", "Course": "Data Structures" }
        setActionName("Unregister");
        this.studentId = studentId;
        this.course = course;
    }


    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected void start() throws IllegalAccessException {
        if( !(this.actorState instanceof CoursePrivateState) )
            throw new IllegalAccessException("only CoursePrivateState actor could enter here");

        if ( ((CoursePrivateState) this.actorState).getRegStudents().contains(this.studentId)) {
            complete(true + " Student \"" + this.studentId + "\" already unregistered for the course");
            return;
        }

        List<Action<Boolean>> removeDependency = new LinkedList<>();
        Action<Boolean> removeCourseFromGradesSheet = new RemoveCourseFromGradesSheet(this.studentId, this.course);
        removeDependency.add(removeCourseFromGradesSheet);

        then(removeDependency, () -> {
            if (removeDependency.get(0).getResult().get()) {
                ((CoursePrivateState) this.actorState).unregisterStudent(this.studentId);
                complete(true + " Student \"" + this.studentId + "\" unregistered successfully for the course \"" + this.course + "\"");
            }
            else {
                complete(false + " Some Error occurred while removing course from grading sheet");
            }
        });
        sendMessage(removeCourseFromGradesSheet, studentId, new StudentPrivateState());
    }
}
