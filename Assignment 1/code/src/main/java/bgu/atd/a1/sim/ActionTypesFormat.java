package bgu.atd.a1.sim;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActionTypesFormat {

    @SerializedName("Action")
    public String action;

    @SerializedName("Department")
    public String department;

    @SerializedName("Course")
    public String course;

    @SerializedName("Space")
    public String space;

    @SerializedName("Prerequisites")
    public List<String> prerequisites;

    @SerializedName("Student")
    public String studentId;

    @SerializedName("Grade")
    public List<String> grades;

    @SerializedName("Number")
    public String newSpaces;

    @SerializedName("Preferences")
    public List<String> preferences;

    @SerializedName("Computer")
    public String computer;

    @SerializedName("Students")
    public List<String> studentsId;

    @SerializedName("Conditions")
    public List<String> conditions;
}
