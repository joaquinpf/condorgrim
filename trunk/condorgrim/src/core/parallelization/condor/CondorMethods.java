package core.parallelization.condor;

import java.util.Hashtable;

public interface CondorMethods {

	public Object doRun(byte[] executable, Hashtable<String, String> properties);

}