package bgu.atd.a1.sim.privateStates;

import java.util.LinkedList;
import java.util.List;


import bgu.atd.a1.PrivateState;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState{

	private Integer availableSpots;
	private Integer registered;
	private List<String> regStudents;
	private List<String> prequisites;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState() {
		this.availableSpots = 0;
		this.registered = 0;
		this.regStudents = new LinkedList<String>();
		this.prequisites = new LinkedList<String>();
	}

	public Integer getAvailableSpots() {
		return this.availableSpots;
	}

	public Integer getRegistered() {
		return this.registered;
	}

	public List<String> getRegStudents() {
		return this.regStudents;
	}

	public void addPlaces(int newPlaces) {
		this.availableSpots += newPlaces;
	}

	public List<String> getPrequisites() {
		return this.prequisites;
	}

	public void registerStudent(String student){
		this.availableSpots--;
		this.registered++;
		regStudents.add(student);
	}

	public void unregisterStudent(String student){
		this.availableSpots++;
		this.registered--;
		regStudents.remove(student);
	}

	public void closeCourse() {
		this.availableSpots = -1;
	}
}
