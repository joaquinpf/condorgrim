package core.parallelization.condor;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import utils.FileUtils;

import condorAPI.Cluster;
import condorAPI.Condor;
import condorAPI.CondorException;
import condorAPI.Event;
import condorAPI.Handler;
import condorAPI.Job;
import condorAPI.JobDescription;
import core.MFGS;

public abstract class AbstractMFGS extends MFGS {

	protected CondorMethods selfdependency = null;

	private static final long serialVersionUID = 1342799743754436288L;
	private static final String STDOUT = "/temp/stdout.txt";
	private static final String STDERR = "/temp/stderr.txt";
	private static final String LOG = "/temp/log.txt";

	public CondorMethods getselfdependency() {
		return selfdependency;
	}

	public void setselfdependency(CondorMethods selfdependency) {
		this.selfdependency = selfdependency;
	}

	public Object doRun(byte[] executable, Hashtable<String, String> properties) {
		if (executable != null && properties != null){
			//TODO CUERPO DE LA EJECUCION, 

			//Se debe grabar el archivo a disco, nombre generico.
			File f = new File("temp/");
			if(!f.exists()){
				f.mkdir();
			}
			byte[] toRun = BinaryManipulator.decompressByteArray(executable);
			BinaryManipulator.writeByteArray("temp/executable", toRun);
		
			//TODO Ejecucion de condor, no se lo que hace setDebug a ciencia cierta
			Condor.setDebug(true);
			Condor condor = new Condor();
			
			JobDescription jd = new JobDescription();
			Enumeration<String> auxEnum = properties.keys();
			String key;		
			try {
				//Agregar el ejecutable, log y salidas stdout y stderr
				jd.addAttribute("executable", "temp/executable");
				jd.addAttribute("output", STDOUT);
				jd.addAttribute("error", STDERR);
				jd.addAttribute("log_xml", "True");
				jd.addAttribute("log", LOG);
				
				//Agregar el resto de las propiedades
				while(auxEnum.hasMoreElements()){
					key = auxEnum.nextElement();
					if(key != "executable" && key != "output" && key != "error" && 
							key != "log" && key != "log_xml"){
						jd.addAttribute(key, properties.get(key));
					}
				}
				jd.addQueue();

				jd.setHandlerOnSuccess(new Handler(){
					public void handle(Event e){
						System.err.println("success " + e);
					}
				});

				Cluster cluster = condor.submit(jd);
				System.out.println("Job submitted to Condor");
				Job j = cluster.getJob(0);
				while (!j.isCompleted()){
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
				FileUtils.cleanUpDirectory("temp/");
				return c;
			} 
			String result = "Execution complete";
			System.out.println(result);	
			CondorPackedOutput c = new CondorPackedOutput();
			c.setDescription(result);
			c.setStderr(BinaryManipulator.readByteArray(STDERR));
			c.setStdout(BinaryManipulator.readByteArray(STDOUT));
			c.setLog(BinaryManipulator.readByteArray(LOG));
			FileUtils.cleanUpDirectory("temp/");
			return c;
		}
		String result = "Se recibieron parametros vacios (ejecutable y/o propiedades) en AbstractMFGS.doRun( ... )";
		System.out.println(result);
		CondorPackedOutput c = new CondorPackedOutput();
		c.setDescription(result);
		return c;
	}

	public abstract void run();

}
