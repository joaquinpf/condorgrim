package core.parallelization.condor;

import java.io.Serializable;

/**
 * 
 * @author Joaquín Pérez Fuentes y Marcos Steimbach
 *
 */
public class CondorExecutionResult implements Serializable {

	//TODO meter lo que sea que devuelva condor como respuesta
	private Object result;

	private static final long serialVersionUID = 134987332777536288L;
	
	private AbstractMFGS target;

	public CondorExecutionResult(AbstractMFGS target, Object result) {
		this.target = target;
		this.result = result;
	}
	
	public Object getResult() {
		return result;
	}

	public AbstractMFGS getTarget() {
		return target;
	}
}