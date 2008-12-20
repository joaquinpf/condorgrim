package core.parallelization.condor;

import java.io.Serializable;

public class CondorPackedOutput implements Serializable {

	private static final long serialVersionUID = -6422657838202191254L;
	private byte[] stdout = null;
	private byte[] stderr = null;
	private byte[] log = null;
	private String description;
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setStderr(byte[] stderr) {
		this.stderr = stderr;
	}
	public byte[] getStderr() {
		return stderr;
	}
	public void setStdout(byte[] stdout) {
		this.stdout = stdout;
	}
	public byte[] getStdout() {
		return stdout;
	}
	public void setLog(byte[] log) {
		this.log = log;
	}
	public byte[] getLog() {
		return log;
	}
}
