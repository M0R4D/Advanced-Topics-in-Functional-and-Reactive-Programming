package bgu.atd.a1.sim;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InputFileFormat {
    @SerializedName("threads")
    public int threads;

    @SerializedName("Computers")
    public List<Computer> computers;

    @SerializedName("Phase 1")
    public List<ActionTypesFormat> phase1;

    @SerializedName("Phase 2")
    public List<ActionTypesFormat> phase2;

    @SerializedName("Phase 3")
    public List<ActionTypesFormat> phase3;
}