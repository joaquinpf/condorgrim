package core.parallelization.condor;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 
 * @author Joaquín Pérez Fuentes y Marcos Steimbach
 *
 */
public class CondorClient {
	
	/**
	 * Host condor.
	 */
	private String condorHost = null;
	
	/**
	 * Puerto del host condor.
	 */
	int port = 9999;

	/**
	 * Constructor de la clase.
	 * @param icondorHost Host condor
	 * @param iport Puerto del host condor
	 */
	public CondorClient(final String icondorHost, final int iport) {
		this.condorHost = icondorHost;
		this.port = iport;
	}

	/**
	 * Envia el pedido de ejecucion al server.
	 * @param req Pedido de ejecucion
	 * @return CondorExecutionResult resultante de la ejecucion
	 * @throws Exception Exeption
	 */
	public final CondorExecutionResult execute(final CondorExecutionRequest req)
			throws Exception {
		Socket socket = null;
		try {
			socket = new Socket(condorHost, port);
			ObjectOutputStream ostream = new ObjectOutputStream(socket
					.getOutputStream());
			ostream.writeObject(req);
			ostream.flush();
			ObjectInputStream istream = new ObjectInputStream(socket
					.getInputStream());
			return (CondorExecutionResult) istream.readObject();
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}