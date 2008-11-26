import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;


public class CondorServer {


	public Object runCondorJob(String executable, Hashtable<String, String> properties){
		//TODO mandar el trabajo a condor
		
		return null;
	}
	
	public Object execute(CondorExecutionRequest req) throws Exception {
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

	public void start(int port) throws Exception {
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
					}
				}
			}
		}
	}

	// args[0] socket donde escucha por aplicaciones ibis
	public static void main(String[] args) throws Exception {
		int default_port = 9999;
		if (args.length > 0)
			try {
				default_port = Integer.parseInt(args[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		CondorServer server = new CondorServer();
		server.start(default_port);
	}

}