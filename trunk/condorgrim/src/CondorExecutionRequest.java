import java.io.Serializable;
import java.util.Hashtable;


/**
 * 
 * @author Joaquín Alejandro Pérez Fuentes
 *
 */
public class CondorExecutionRequest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1342799743777536288L;
	
	/**
	 * 
	 */
	private byte[] executable;
	
	/**
	 * 
	 */
	private Hashtable<String, String> properties;
	
	/**
	 * 
	 * @param properties properties
	 * @param toRun toRun
	 */
	public CondorExecutionRequest(final byte[] toRun, 
			final Hashtable<String, String> properties) {
		this.executable = toRun;
		this.properties = properties;
	}
	
	/**
	 * 
	 * @return byte[]
	 */
	public final byte[] getExecutable() {
		return executable;
	}
	
	/**
	 * 
	 * @return Hashtable<String, String>
	 */
	public final Hashtable<String, String> getProperties() {
		return properties;
	}
}
