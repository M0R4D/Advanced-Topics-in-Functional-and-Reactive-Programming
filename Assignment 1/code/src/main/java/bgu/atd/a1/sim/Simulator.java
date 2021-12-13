/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.atd.a1.sim;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import bgu.atd.a1.Action;
import bgu.atd.a1.ActorThreadPool;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.actions.*;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {
	
	public static ActorThreadPool actorThreadPool;
	public static CountDownLatch phase;
	
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start() throws InterruptedException {
		actorThreadPool.start();
		System.out.println(1);

		phase = new CountDownLatch(input.computers.size());
		for (Computer comp : input.computers) {
			Action<Boolean> action = new AddComputerAction(comp.computerType, comp);
			actorThreadPool.submit(new SchedulerPhaseAction(null, action, "Warehouse"), "Warehouse", new WarehousePrivateState());
		}
		phase.await();

		System.out.println(2);

		phase = new CountDownLatch(input.phase1.size());
		for (ActionTypesFormat action : input.phase1) {
			convertActionAndSubmit(action);
		}
		phase.await();

		System.out.println(3);

		phase = new CountDownLatch(input.phase2.size());
		for (ActionTypesFormat action : input.phase2) {
			convertActionAndSubmit(action);
		}
		phase.await();

		System.out.println(4);

		phase = new CountDownLatch(input.phase3.size());
		for (ActionTypesFormat action : input.phase3) {
			convertActionAndSubmit(action);
		}
		phase.await();

		System.out.println(5);

    }

	private static void convertActionAndSubmit(ActionTypesFormat actionFormat) {
		String actionName = actionFormat.action;
		Action action = null;
		String actorName = null;
		PrivateState actorState = null;
		DepartmentPrivateState departmentPrivateState;
		CoursePrivateState coursePrivateState;
		StudentPrivateState studentPrivateState;

		if(actorThreadPool.getPrivateState(actionFormat.department) == null)
			departmentPrivateState = new DepartmentPrivateState();
		else departmentPrivateState = (DepartmentPrivateState) actorThreadPool.getPrivateState(actionFormat.department);

		if(actorThreadPool.getPrivateState(actionFormat.course) == null)
			coursePrivateState = new CoursePrivateState();
		else coursePrivateState = (CoursePrivateState) actorThreadPool.getPrivateState(actionFormat.course);

		if(actorThreadPool.getPrivateState(actionFormat.studentId) == null)
			studentPrivateState = new StudentPrivateState();
		else studentPrivateState = (StudentPrivateState) actorThreadPool.getPrivateState(actionFormat.studentId);

		switch (actionName) {
			case "Open Course":
				action = new OpenCourse(actionFormat.department, actionFormat.course, Integer.parseInt(actionFormat.space), actionFormat.prerequisites);
				actorName = actionFormat.department;
				actorState = departmentPrivateState;
				break;
			case "Add Student":
				action = new AddStudent(actionFormat.department, actionFormat.studentId);
				actorName = actionFormat.department;
				actorState = departmentPrivateState;
				break;
			case "Participate In Course":
				action = new CourseParticipate(actionFormat.studentId, actionFormat.course, actionFormat.grades);
				actorName = actionFormat.course;
				actorState = coursePrivateState;
				break;
			case "Unregister":
				action = new Unregister(actionFormat.studentId, actionFormat.course);
				actorName = actionFormat.course;
				actorState = coursePrivateState;
				break;
			case "Close Course":
				action = new CloseCourse(actionFormat.course);
				actorName = actionFormat.department;
				actorState = departmentPrivateState;
				break;
			case "Add Spaces":
				action = new AddCoursePlaces(actionFormat.course, Integer.parseInt(actionFormat.newSpaces));
				actorName = actionFormat.course;
				actorState = coursePrivateState;
				break;
			case "Administrative Check":
				action = new CheckAdministrativeObligations(actionFormat.department, actionFormat.studentsId, actionFormat.computer, actionFormat.conditions);
				actorName = actionFormat.department;
				actorState = departmentPrivateState;
				break;
			case "Register With Preferences":
				action = new RegisterWithPreferences(actionFormat.studentId, actionFormat.preferences, actionFormat.grades);
				actorName = actionFormat.studentId;
				actorState = studentPrivateState;
				break;
		}

		actorThreadPool.submit(new SchedulerPhaseAction(actionName, action, actorName), actorName, actorState);
	}

	/**
	* attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	* 
	* @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	*/
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		actorThreadPool = myActorThreadPool;
	}
	
	/**
	* shut down the simulation
	* returns list of private states
	*/
	public static HashMap<String,PrivateState> end() throws InterruptedException {
		actorThreadPool.shutdown();
		return (HashMap<String, PrivateState>) actorThreadPool.getActors();
	}
	
	static InputFileFormat input;
	public static void main(@NotNull String [] args) throws IOException, InterruptedException {
    	// mvn clean install exec:java -Dexec.mainClass="bgu.atd.a1.sim.Simulator" -Dexec.args="input-example.txt"

		String fileToRead = args[0];
		System.out.println(fileToRead);

		Gson gson = new Gson();
		try (Reader reader = new FileReader(fileToRead)) {
			input = gson.fromJson(reader, InputFileFormat.class);
		}

		System.out.println(input.threads);

		actorThreadPool = new ActorThreadPool(input.threads);

		System.out.println("after actorThreadPool");

		start();

		System.out.println("after calling start");

		Map<String, PrivateState> simulatorResult;
		simulatorResult = end();
		FileOutputStream outputFile = new FileOutputStream("result.ser");
		ObjectOutputStream oos = new ObjectOutputStream(outputFile);
		oos.writeObject(simulatorResult);
		oos.close();

		System.out.println("the end");
	}
}
