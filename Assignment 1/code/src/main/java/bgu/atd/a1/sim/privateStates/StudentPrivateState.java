package bgu.atd.a1.sim.privateStates;

import java.util.HashMap;


import bgu.atd.a1.PrivateState;

/**
 * this class describe student private state
 */
public class StudentPrivateState extends PrivateState{

	private HashMap<String, Integer> grades;
	private long signature;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public StudentPrivateState() {
		this.grades = new HashMap<String, Integer>();
		this.signature = 0;
	}

	public HashMap<String, Integer> getGrades() {
		return this.grades;
	}

	public long getSignature() {
		return this.signature;
	}
}
