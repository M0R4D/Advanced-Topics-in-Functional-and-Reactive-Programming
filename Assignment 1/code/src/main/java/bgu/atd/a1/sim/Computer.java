package bgu.atd.a1.sim;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Computer {

	@SerializedName("Type")
	public String computerType;

	@SerializedName("Sig Fail")
	public long failSig;

	@SerializedName("Sig Success")
	private long successSig;
	
	public Computer(String computerType) {
		// { "Type":"A", "Sig Success": "1234666", "Sig Fail": "999283" }
		this.computerType = computerType;
	}
	
	/**
	 * this method checks if the courses' grades fulfill the conditions
	 * @param courses
	 * 							courses that should be pass
	 * @param coursesGrades
	 * 							courses' grade
	 * @return a signature if coursesGrades grades meet the conditions
	 */
	public long checkAndSign(List<String> courses, Map<String, Integer> coursesGrades){
		for (String course : courses) {
			if(!coursesGrades.containsKey(course) || coursesGrades.get(course) < 56)
				return this.failSig;
		}
		return this.successSig;
	}
}
