package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.List;

public class CheckAdministrativeObligations extends Action {
    private String department;
    private List<String> students;
    private String computer;
    private List<String> conditions;


    public CheckAdministrativeObligations(String department, List<String> students, String computer, List<String> conditions) {
        //{ "Action" : "Administrative Check", "Department": "CS", "Students": ["123456789","5959595959"], "Computer": "A", "Conditions" : ["SPL", "Data Structures"] }
        setActionName("Administrative Check");
        this.department = department;
        this.students = students;
        this.computer = computer;
        this.conditions = conditions;
    }

    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected void start() throws IllegalAccessException {
        if( !(this.actorState instanceof DepartmentPrivateState) )
            throw new IllegalAccessException("only DepartmentPrivateState actor could enter here");
        complete(true);
    }
}
