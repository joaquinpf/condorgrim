package core.parallelization.condor;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import utils.FileUtils;
import utils.NetworkConfigurator;
import condorAPI.Cluster;
import condorAPI.Condor;
import condorAPI.CondorException;
import condorAPI.Event;
import condorAPI.Handler;
import condorAPI.Job;
import condorAPI.JobDescription;
import core.MFGS;

/**
 * The Class AbstractMFGS. This class handles Condor specific code, from submit
 * description creation to running the job. It's meant to be extended by every
 * application needing Condor capabilities, and to be a straight forward way of
 * providing them
 * 
 * @author Joaquín Pérez Fuentes y Marcos Steimbach
 */
public abstract class AbstractMFGS extends MFGS {

	/** The selfdependency. */
	protected CondorMethods selfdependency = null;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1342799743754436288L;

	/** The Constant STDOUT, path to the output stream output. */
	private static final String STDOUT = "/temp/stdout";

	/** The Constant STDERR, path to the error stream output. */
	private static final String STDERR = "/temp/stderr";

	/** The Constant LOG, path to the log file. */
	private static final String LOG = "/temp/log";

	private static final String EXECUTABLE = "temp/executable";

	/**
	 * Gets the selfdependency.
	 * 
	 * @return the selfdependency
	 */
	public CondorMethods getselfdependency() {
		return selfdependency;
	}

	/**
	 * Sets the selfdependency.
	 * 
	 * @param selfdependency the new selfdependency
	 */
	public void setselfdependency(CondorMethods selfdependency) {
		this.selfdependency = selfdependency;
	}

	/**
	 * Do run. The main condor execution method. Handles job generation,
	 * submitting and catching results. It generates a CondorPackedOutput
	 * response with the outputs of stdout, stderr, log and a brief description
	 * fitting the execution.
	 * 
	 * @param executable the executable to be run
	 * @param properties the properties to be set for the condor job. Properties
	 *            "executable", "log", "output", "error" and "log_xml" will be
	 *            overriden to fit standard practices for the method
	 * 
	 * @return the object
	 */
	public Object doRun(byte[] executable, Hashtable<String, String> properties) {
		// If parameters are not null
		if (executable != null && properties != null) {

			// We have to be careful not to overwrite files if there are
			// concurrent executions
			String hash = Integer.toString(this.hashCode());

			String localStdout = STDOUT + hash;
			String localStderr = STDERR + hash;
			String localLog = LOG + hash;
			String localExecutable = EXECUTABLE + hash;

			// Writing executable file to disk with a generic name.
			File f = new File("temp/");
			if (!f.exists()) {
				f.mkdir();
			}
			byte[] toRun = BinaryManipulator.decompressByteArray(executable);
			BinaryManipulator.writeByteArray(localExecutable, toRun);

			// Condor init
			Condor.setDebug(true);
			Condor condor = new Condor();

			JobDescription jd = new JobDescription();
			Enumeration<String> auxEnum = properties.keys();
			String key;
			try {
				// Add executable, stderr, stdout and logging to job description
				jd.addAttribute("executable", localExecutable);
				jd.addAttribute("output", localStdout);
				jd.addAttribute("error", localStderr);
				jd.addAttribute("log_xml", "True");
				jd.addAttribute("log", localLog);

				// Add user specific settings
				while (auxEnum.hasMoreElements()) {
					key = auxEnum.nextElement();
					if (key != "executable" && key != "output"
							&& key != "error" && key != "log"
							&& key != "log_xml") {
						jd.addAttribute(key, properties.get(key));
					}
				}
				jd.addQueue();

				jd.setHandlerOnSuccess(new Handler() {
					public void handle(Event e) {
						System.err.println("success " + e);
					}
				});

				Cluster cluster = condor.submit(jd);
				System.out.println("Job submitted to Condor");
				// Wait for condor job to finish
				Job j = cluster.getJob(0);
				while (!j.isCompleted()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}

			} catch (CondorException e1) {
				e1.printStackTrace();
				String result = "Execution failed with CondorExeption";
				System.out.println(result);
				CondorPackedOutput c = new CondorPackedOutput();
				c.setDescription(result);
				FileUtils.cleanUpDirectory("temp/", hash);
				return c;
			}
			String result = "Execution complete";
			System.out.println(result);
			CondorPackedOutput c = new CondorPackedOutput();
			c.setDescription(result);
			c.setStderr(BinaryManipulator.readByteArray(STDERR));
			c.setStdout(BinaryManipulator.readByteArray(STDOUT));
			c.setLog(BinaryManipulator.readByteArray(LOG));
			FileUtils.cleanUpDirectory("temp/", hash);
			return c;
		}
		String result = "Null parameters recieved in AbstractMFGS.doRun(...) "
				+ "(either executable or properties)";
		System.out.println(result);
		CondorPackedOutput c = new CondorPackedOutput();
		c.setDescription(result);
		return c;
	}

	/**
	 * The run() method. It's meant to be the hook method for condor execution
	 * in JGRIM. Users should load their desired Condor settings and executable
	 * using BinaryManipulator and then call "selfdependency.doRun(...)"
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public abstract void run();

	/**
	 * The main method. Loads specific configuration files and launches condor
	 * execution based on user code in the implementation of run().
	 * 
	 * @param args the arguments
	 * 
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		NetworkConfigurator.configure("configuration/network.properties");
		PropertyConfigurator.configure("configuration/log4j.properties");
		ApplicationContext appContext = new FileSystemXmlApplicationContext(
				"configuration/simple-condor.xml");
		Runnable service = (Runnable) appContext.getBean("service");
		service.run();
	}
}
