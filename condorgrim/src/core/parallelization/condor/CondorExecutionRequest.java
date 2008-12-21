package core.parallelization.condor;

import java.io.Serializable;
import core.policy.ServiceCallInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class CondorExecutionRequest. Encapsulates a request to a CondorServer.
 * 
 * @author Joaquín Pérez Fuentes y Marcos Steimbach
 */
public class CondorExecutionRequest implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1342799743777536288L;

	/** Target. */
	private AbstractMFGS target;

	/** Invocation properties. */
	private ServiceCallInfo info;

	/**
	 * Class constructor
	 * 
	 * @param itarget target
	 * @param iinfo Invocation properties
	 */
	public CondorExecutionRequest(final AbstractMFGS itarget,
			final ServiceCallInfo iinfo) {
		this.target = itarget;
		this.info = iinfo;
	}

	/**
	 * Gets the target.
	 * 
	 * @return the target
	 */
	public final AbstractMFGS getTarget() {
		return target;
	}

	/**
	 * Gets the invocation properties.
	 * 
	 * @return ServiceCallInfo
	 */
	public final ServiceCallInfo getInfo() {
		return info;
	}
}
