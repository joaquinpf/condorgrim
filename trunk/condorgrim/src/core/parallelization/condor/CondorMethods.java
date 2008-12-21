package core.parallelization.condor;

import java.util.Hashtable;

/**
 * The Interface CondorMethods.
 * 
 * @author Joaquín Pérez Fuentes y Marcos Steimbach
 */
public interface CondorMethods {

	/**
	 * Do run. The main condor execution method. Handles job generation,
	 * submitting and catching results. It generates a CondorPackedOutput
	 * response with the outputs of stdout, stderr, log and a brief description
	 * fitting the execution.
	 * 
	 * @param executable the executable to be run
	 * @param properties the properties to be set for the condor job. Properties
	 *            "executable", "log", "output", "error" and "log_xml" will be
	 *            overriden to fit standard practices for the method
	 * 
	 * @return the object
	 */
	public Object doRun(byte[] executable, Hashtable<String, String> properties);

}