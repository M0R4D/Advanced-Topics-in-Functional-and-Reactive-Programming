package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class OpenCourse extends Action {

    // private String actionName;
    private String courseName;
    private String department;
    private int space;
    private List<String> pre;


    public OpenCourse(String courseName, String department, int space, List<String> pre) {
        // { "Action":"Open Course", "Department": "CS", "Course": "Intro To CS", "Space": "200", "Prerequisites" : [] }
        setActionName("Open Course");
        this.courseName = courseName;
        this.department = department;
        this.space = space;
        this.pre = pre;
    }


    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected void start() throws IllegalAccessException {
        if( !(this.actorState instanceof DepartmentPrivateState) )
            throw new IllegalAccessException("only DepartmentPrivateState actor could enter here");
        if( ((DepartmentPrivateState) this.actorState).getCourseList().contains(courseName))
            complete(false + this.courseName + " course already opened");

        List<Action> initiateCourse = new LinkedList<>();
        Action initiateCourseAction = new InitiateCourse(this.courseName, this.department, this.space, this.pre);
        initiateCourse.add(initiateCourseAction);

        then(initiateCourse, () -> {
            if((boolean) initiateCourse.get(0).getResult().get()) {
                complete(true + "Course \"" + this.courseName + "\" has been opened successfully");
            }
            else
                complete(false + "Error occurred while opening course \"" + this.courseName + "\"");
        });
        sendMessage(initiateCourseAction, this.courseName, new CoursePrivateState());
    }
}
