
import java.io.Serializable;

/**
 * 
 * @author Joaqu�n P�rez Fuentes y Marcos Steimbach
 *
 */
public class CondorExecutionResult implements Serializable {

	//TODO meter lo que sea que devuelva condor como respuesta
	private Object result;
	
	public CondorExecutionResult(Object result) {
		this.result = result;
	}
	
}