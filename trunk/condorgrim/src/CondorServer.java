import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

/**
 * 
 * @author Joaquín Pérez Fuentes y Marcos Steimbach
 *
 */
public class CondorServer {

	/**
	 * Envia el pedido de ejecucion a Condor y monitorea su completitud.
	 * @param executable Path del ejecutable a correr
	 * @param properties Propiedades a setear en Condor
	 * @return Respuesta de Condor
	 */
	public final Object runCondorJob(final String executable, 
			final Hashtable<String, String> properties) {
		//TODO mandar el trabajo a condor
		
		return null;
	}
	
	/**
	 * Ejecuta un pedido Condor. 
	 * Debe escribir el ejecutable serializado en disco.
	 * @param req El pedido de ejecucion encapsulado
	 * @return CondorExecutionResult resultante de la ejecucion
	 * @throws Exception Exeption
	 */
	public final Object execute(final CondorExecutionRequest req) 
			throws Exception {
		
		System.out.println("Calling method...");
		byte[] executable = req.getExecutable();
		Hashtable<String, String> properties = req.getProperties();
		
		//TODO ponerle nombre al ejecutable
		executable = BinaryManipulator.decompressByteArray(executable);
		BinaryManipulator.writeByteArray("executable.class", executable);
		
		Object result = runCondorJob("executable.class", properties);
		System.out.println("Calling done! result = " + result);
		return result;
	}
	
	/**
	 * Inicio del servidor, abre el puerto y escucha por pedidos Condor.
	 * Escribe el resultado de la ejecucion como un CondorExecutionResult
	 * @param port puerto a escuchar
	 * @throws Exception Exception
	 */
	public final void start(final int port) throws Exception {
		System.out.println("Opening server on : " + port);
		ServerSocket ss = new ServerSocket(port);
		System.out.println("Opened.");
		while (true) {
			Socket socket = null;
			try {
				System.out.println("Waiting for Condor execution requests...");
				socket = ss.accept();
				ObjectInputStream stream = new ObjectInputStream(socket
						.getInputStream());
				CondorExecutionRequest obj = (CondorExecutionRequest) stream
						.readObject();
				System.out.println("Condor execution request received!");
				Object callResult = execute(obj);
				CondorExecutionResult result = new CondorExecutionResult(callResult);
				ObjectOutputStream ostream = new ObjectOutputStream(socket
						.getOutputStream());
				ostream.writeObject(result);
				ostream.flush();
				ostream.close();
			} catch (InvocationTargetException e) {
				e.getCause().printStackTrace();
				throw e;
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param args args[0] socket donde escucha por aplicaciones Condor
	 * @throws Exception Exception
	 */
	public static void main(final String[] args) throws Exception {
		int defaultPort = 9999;
		if (args.length > 0) {
			try {
				defaultPort = Integer.parseInt(args[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		CondorServer server = new CondorServer();
		server.start(defaultPort);
	}

}