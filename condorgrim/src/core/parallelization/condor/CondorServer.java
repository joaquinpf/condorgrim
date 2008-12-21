package core.parallelization.condor;

import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The Class CondorServer. Handles listening for execution requests, invoking
 * the correct method and sending back it's results.
 * 
 * @author Joaquín Pérez Fuentes y Marcos Steimbach
 */
public class CondorServer {

	/**
	 * Descubre el tipo del objeto pasado por parametro.
	 * 
	 * @param parameter Objeto del que se desea conocer su clase
	 * 
	 * @return Class tipo del objeto pasado por parametro
	 */
	private Class convertParameterClass(Object parameter) {
		if (parameter instanceof Integer)
			return Integer.TYPE;
		else if (parameter instanceof Byte)
			return Byte.TYPE;
		else if (parameter instanceof Short)
			return Short.TYPE;
		else if (parameter instanceof Character)
			return Character.TYPE;
		else if (parameter instanceof Long)
			return Long.TYPE;
		else if (parameter instanceof Float)
			return Float.TYPE;
		else if (parameter instanceof Double)
			return Double.TYPE;
		else if (parameter instanceof Boolean)
			return Boolean.TYPE;
		if (parameter != null)
			return parameter.getClass();
		else
			return null;
	}

	/**
	 * Executes a condor request.
	 * 
	 * @param req The encapsulated request as a CondorExecutionRequest
	 * 
	 * @return CondorExecutionResult execution result
	 * 
	 * @throws Exception Exeption
	 */
	public final Object execute(final CondorExecutionRequest req)
			throws Exception {
		System.out.println("Calling method...");
		AbstractMFGS obj = req.getTarget();
		Object[] args = req.getInfo().getArguments();
		Class[] types = new Class[args.length];
		for (int i = 0; i < types.length; i++) {
			types[i] = convertParameterClass(args[i]);
		}
		if (obj != null && req != null) {
			Method m = obj.getClass().getMethod(req.getInfo().getServiceName(),
					types);
			Object result = m.invoke(obj, args);
			System.out.println("Calling done! result = " + result);
			return result;
		} else
			return null;
	}

	/**
	 * Server start-up. Opens a socket and keeps listening for condor execution
	 * requests. After the execution is complete, it sends back it's result as a
	 * CondorExecutionResult
	 * 
	 * @param port port to listen to
	 * 
	 * @throws Exception Exception
	 */
	public final void start(final int port) throws Exception {
		System.out.println("Opening server on : " + port);
		ServerSocket ss = new ServerSocket(port);
		System.out.println("Opened.");
		Socket socket = null;
		while (true) {
			try {
				System.out.println("Waiting for Condor execution requests...");
				socket = ss.accept();
				ObjectInputStream stream = new ObjectInputStream(socket
						.getInputStream());
				CondorExecutionRequest obj = (CondorExecutionRequest) stream
						.readObject();
				System.out.println("Condor execution request received!");
				if (obj != null) {
					Object callResult = execute(obj);
					CondorExecutionResult result = new CondorExecutionResult(
							obj.getTarget(), callResult);
					ObjectOutputStream ostream = new ObjectOutputStream(socket
							.getOutputStream());
					ostream.writeObject(result);
					ostream.flush();
				} else
					System.out.println("Null object recieved for execution.");
			} catch (InvalidClassException e) {
				e.printStackTrace();
				throw e;
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
	 * The main method.
	 * 
	 * @param args args[0] socket where server listens for incoming condor
	 *            requests
	 * 
	 * @throws Exception Exception
	 */
	public static void main(final String[] args) throws Exception {
		int defaultPort = 9500;
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