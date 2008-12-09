package core.parallelization.condor;

import java.util.Hashtable;

public interface CondorMethods {

	public long doRun(byte[] executable, Hashtable<String, String> properties);

}