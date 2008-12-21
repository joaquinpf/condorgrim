package core.parallelization.condor;

import java.io.Serializable;

/**
 * The Class CondorPackedOutput. A wrapper for the various files generated in
 * the condor execution
 * 
 * @author Joaquín Pérez Fuentes y Marcos Steimbach
 */
public class CondorPackedOutput implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6422657838202191254L;

	/** The stdout. */
	private byte[] stdout = null;

	/** The stderr. */
	private byte[] stderr = null;

	/** The log. */
	private byte[] log = null;

	/** The description. */
	private String description;

	/**
	 * Sets the description.
	 * 
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the stderr.
	 * 
	 * @param stderr the new stderr
	 */
	public void setStderr(byte[] stderr) {
		this.stderr = stderr;
	}

	/**
	 * Gets the stderr.
	 * 
	 * @return the stderr
	 */
	public byte[] getStderr() {
		return stderr;
	}

	/**
	 * Sets the stdout.
	 * 
	 * @param stdout the new stdout
	 */
	public void setStdout(byte[] stdout) {
		this.stdout = stdout;
	}

	/**
	 * Gets the stdout.
	 * 
	 * @return the stdout
	 */
	public byte[] getStdout() {
		return stdout;
	}

	/**
	 * Sets the log.
	 * 
	 * @param log the new log
	 */
	public void setLog(byte[] log) {
		this.log = log;
	}

	/**
	 * Gets the log.
	 * 
	 * @return the log
	 */
	public byte[] getLog() {
		return log;
	}
}
