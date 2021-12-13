package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

public class AddStudent extends Action {
    private String department;
    private String studentId;

    public AddStudent(String department, String studentId) {
        // { "Action": "Add Student", "Department": "CS", "Student": "5959595959" }
        setActionName("Add Student");
        this.department = department;
        this.studentId = studentId;
    }


    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    @Override
    protected void start() throws IllegalAccessException {
        if( !(this.actorState instanceof DepartmentPrivateState) )
            throw new IllegalAccessException("only DepartmentPrivateState actor could enter here");
        if ( ((DepartmentPrivateState) this.actorState).getStudentList().contains(this.studentId) ) {
            complete(false + "The student \"" + this.studentId + "\" is already exist" );
        }
        else {
            ((DepartmentPrivateState) this.actorState).getStudentList().add(this.studentId);
            complete(true + "The student \"" + this.studentId + "\" was added successfully" );
        }
    }
}
