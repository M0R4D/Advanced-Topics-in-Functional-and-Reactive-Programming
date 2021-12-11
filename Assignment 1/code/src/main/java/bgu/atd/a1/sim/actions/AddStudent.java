package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;

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
    protected void start() {

    }
}
