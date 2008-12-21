package core.parallelization.condor;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class CondorExecutionResult. Encapsulates the result of a Condor
 * execution.
 * 
 * @author Joaquín Pérez Fuentes y Marcos Steimbach
 */
public class CondorExecutionResult implements Serializable {

	/** The result, a CondorPackedOutput. */
	private Object result;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 134987332777536288L;

	/** The target. */
	private AbstractMFGS target;

	/**
	 * Instantiates a new CondorExecutionResult.
	 * 
	 * @param target the target
	 * @param result the result
	 */
	public CondorExecutionResult(AbstractMFGS target, Object result) {
		this.target = target;
		this.result = result;
	}

	/**
	 * Gets the result.
	 * 
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * Gets the target.
	 * 
	 * @return the target
	 */
	public AbstractMFGS getTarget() {
		return target;
	}
}