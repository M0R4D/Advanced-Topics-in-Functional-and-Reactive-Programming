package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RegisterWithPreferences extends Action {
    private String student;
    private List<String> preferences;
    private List<String> grades;

    public RegisterWithPreferences(String student, List<String> preferences, List<String> grades) {
        // { "Action": "Register With Preferences", "Student": "5959595959", "Preferences": ["Data Bases","SPL"], "Grade": ["98","56"] }
        setActionName("Register With Preferences");
        this.student = student;
        this.preferences = preferences;
        this.grades = grades;
    }

    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected void start() throws IllegalAccessException {
        if( !(this.actorState instanceof StudentPrivateState) )
            throw new IllegalAccessException("only StudentPrivateState actor could enter here");
        if(preferences == null || preferences.size() == 0 || preferences.size() != grades.size()) {
            complete(false + "The List of preferred courses or grades couldn't be empty ..");
            return;
        }

        this.nextCourseRegister();
    }

    private void nextCourseRegister() {
        if (this.preferences.size() == 0)
            complete(false + "The student \"" + this.student + "\" does not succeeded to register to any of the preferred course");

        String nextCourse = this.preferences.get(0);
        List<String> nextCourseGrade = this.grades.subList(0,1);
        CourseParticipate courseParticipate = new CourseParticipate(this.actorId, nextCourse, nextCourseGrade);
        sendMessage(courseParticipate,nextCourse,new CoursePrivateState());
        List<Action> actions = new LinkedList<>();
        actions.add(courseParticipate);
        then(actions, () -> {
            if( (boolean) actions.get(0).getResult().get() ) {
                complete(true + "The student \"" + this.student + "\" registered successfully to the course \"" + nextCourse + "with grade: " + nextCourseGrade.get(0));
            }
            else {
                this.preferences.remove(0);
                this.grades.remove(0);
                this.nextCourseRegister();
            }
        });
    }

}
