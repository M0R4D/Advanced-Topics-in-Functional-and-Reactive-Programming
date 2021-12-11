package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class CourseParticipate extends Action {
    private String studentId;
    private String course;
    private int grade;

    public CourseParticipate(String studentId, String course, List<String> grade) {
        // { "Action": "Participate In Course", "Student": "5959595959", "Course": "Intro To CS", "Grade": ["94"] }
        // { "Action": "Participate In Course", "Student": "123456789", "Course": "Data Bases", "Grade": ["-"] }
        setActionName("Participate In Course");
        this.studentId = studentId;
        this.course = course;
        if (grade.get(0).equals("-"))
            this.grade = 0;
        else this.grade = Integer.parseInt(grade.get(0));
        // this.grade = ( grade.get(0).equals("-") ) ? 0 : Integer.parseInt(grade.get(0));
    }

    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected void start() throws IllegalAccessException {
        if( !(this.actorState instanceof CoursePrivateState) )
            throw new IllegalAccessException("only CoursePrivateState actor could enter here");

        // Course closed for registration
        if ( ((CoursePrivateState) this.actorState).getAvailableSpots() == -1 )
            complete(false + "the course \"" + this.actorId + "\" has been closed");

        // No available places in the course
        if ( ((CoursePrivateState) this.actorState).getAvailableSpots() == 0 )
            complete(false + "There is no available places in course \"" + this.actorId );

        // Student already registered to the course
        if ( ((CoursePrivateState) this.actorState).getRegStudents().contains(this.studentId) )
            complete(false + "Student '" + this.studentId + "' is already registered to the course '" + this.actorId + "'");

        List<Action<Boolean>> preDependencies = new LinkedList<>();
        Action checkPreCourses =  new CheckCoursePrequisites(((CoursePrivateState) this.actorState).getPrequisites());
        preDependencies.add(checkPreCourses);

        then(preDependencies, () -> {
            boolean result = preDependencies.get(0).getResult().get();
            if(result == true) { // the student has completed all the pre courses
                ((CoursePrivateState) this.actorState).registerStudent(this.studentId);
                List<Action<Boolean>> addCourseToGradingSheet = new LinkedList<>();
                Action addToSheet = new AddCourseToGradingSheet(this.course, this.grade);
                addCourseToGradingSheet.add(addToSheet);
                then(addCourseToGradingSheet, () -> {
                    if (addCourseToGradingSheet.get(0).getResult().get()) { // the course has been added to the student's grades sheet
                        complete(true);
                    } else {
                        complete(false);
                    }
                });
                sendMessage(addToSheet, studentId, new StudentPrivateState());
            }
            else {
                complete(false);
            }
        });
        sendMessage(checkPreCourses, studentId, new StudentPrivateState());
    }
}
