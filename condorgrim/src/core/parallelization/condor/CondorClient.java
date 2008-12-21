package core.parallelization.condor;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The Class CondorClient.
 * 
 * @author Joaquín Pérez Fuentes y Marcos Steimbach
 */
public class CondorClient {

	/** Condor host. */
	private String condorHost = "localhost";

	/** Condor host port. */
	int port = 9500;

	/**
	 * Class constructor.
	 * 
	 * @param icondorHost Condor host
	 * @param iport Condor host port
	 */
	public CondorClient(final String icondorHost, final int iport) {
		this.condorHost = icondorHost;
		this.port = iport;
	}

	/**
	 * Send the CondorExecutionRequest to the server.
	 * 
	 * @param req Condor execution request
	 * 
	 * @return CondorExecutionResult Execution result
	 * 
	 * @throws Exception Exeption
	 */
	public final CondorExecutionResult execute(final CondorExecutionRequest req)
			throws Exception {
		Socket socket = null;
		try {
			socket = new Socket(condorHost, port);
			ObjectOutputStream ostream = new ObjectOutputStream(socket
					.getOutputStream());
			if (req != null) {
				ostream.writeObject(req);
				ostream.flush();
			}
			ObjectInputStream istream = new ObjectInputStream(socket
					.getInputStream());
			if (istream != null)
				return (CondorExecutionResult) istream.readObject();
			else
				return null;
		} catch (EOFException e) {
			System.out.println("No result in task execution.");
			e.printStackTrace();
		} catch (Exception ee) {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			ee.printStackTrace();
		}
		return null;
	}
}