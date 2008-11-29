import java.io.Serializable;
import core.policy.ServiceCallInfo;


/**
 * 
 * @author Joaquín Pérez Fuentes y Marcos Steimbach
 *
 */
public class CondorExecutionRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1342799743777536288L;
	
	/**
	 * Clase target.
	 */
	private AbstractMFGS target;
	
	/**
	 * Propiedades de invocacion.
	 */
	private ServiceCallInfo info;
	
	/**
	 * Constructor de la clase.
	 * @param itarget Clase target
	 * @param iinfo Propiedades de invocacion
	 */
	public CondorExecutionRequest(final AbstractMFGS itarget, 
			final ServiceCallInfo iinfo) {
		this.target = itarget;
		this.info = iinfo;
	}
	
	/**
	 * Retorna la clase target.
	 * @return AbstractMFGS
	 */
	public final AbstractMFGS getTarget() {
		return target;
	}
	
	/**
	 * Retorna las propiedades de invocacion.
	 * @return ServiceCallInfo
	 */
	public final ServiceCallInfo getInfo() {
		return info;
	}
}
