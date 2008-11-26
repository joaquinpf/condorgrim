
import java.io.Serializable;

public class CondorExecutionResult implements Serializable {

	private Object result;
	
	public CondorExecutionResult(Object result) {
		this.result = result;
	}
	
}