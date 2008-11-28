import java.io.Serializable;
import java.util.Hashtable;

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
	 * Ejecutable a correr.
	 */
	private byte[] executable;
	
	/**
	 * Propiedades a setear en la ejecucion del trabajo en condor.
	 */
	private Hashtable<String, String> properties;
	
	/**
	 * Constructor de la clase.
	 * @param iproperties Propiedades a setear en la ejecucion
	 * del trabajo en condor
	 * @param toRun Ejecutable a correr
	 */
	public CondorExecutionRequest(final byte[] toRun, 
			final Hashtable<String, String> iproperties) {
		this.executable = toRun;
		this.properties = iproperties;
	}
	
	/**
	 * Retorna el ejecutable.
	 * @return byte[]
	 */
	public final byte[] getExecutable() {
		return executable;
	}
	
	/**
	 * Retorna las propiedades a setear en la ejecucion.
	 * @return Hashtable de propiedades
	 */
	public final Hashtable<String, String> getProperties() {
		return properties;
	}
}
