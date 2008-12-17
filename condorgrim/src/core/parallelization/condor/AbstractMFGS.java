package core.parallelization.condor;
import java.util.Enumeration;
import java.util.Hashtable;

import condorAPI.Cluster;
import condorAPI.Condor;
import condorAPI.CondorException;
import condorAPI.Event;
import condorAPI.Handler;
import condorAPI.JobDescription;
import core.MFGS;

public abstract class AbstractMFGS extends MFGS {

	protected CondorMethods selfdependency = null;

	private static final long serialVersionUID = 1342799743754436288L;

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
			byte[] toRun = BinaryManipulator.decompressByteArray(executable);
			BinaryManipulator.writeByteArray("executable", toRun);
		
			//TODO Ejecucion de condor, no se lo que hace setDebug a ciencia cierta
			Condor.setDebug(true);
			Condor condor = new Condor();
			
			JobDescription jd = new JobDescription();
			Enumeration<String> auxEnum = properties.keys();
			String key;		
			try {
				//Agregar el ejecutable, log y salidas stdout y stderr
				String path = "";  //this.getClass().getResource("executable").getPath();
				jd.addAttribute("executable", path);
				jd.addAttribute("output", "stdout.txt");
				jd.addAttribute("error", "stderr.txt");
				jd.addAttribute("log_xml", "True");
				jd.addAttribute("log", "log.txt");
				
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
				cluster.waitFor();
				
			} catch (CondorException e1) {
				e1.printStackTrace();
				String result = "Execution failed with CondorExeption";
				System.out.println(result);	
				return result;
			}
			
			byte[] ret = BinaryManipulator.readByteArray("stdout.txt");
			ret = BinaryManipulator.compressByteArray(ret);
			System.out.println("Execution complete");	
			return ret;
		}
		String result = "Se recibieron parametros vacios (ejecutable y/o propiedades) en AbstractMFGS.doRun( ... )";
		System.out.println(result);
		return result;
	}

	public abstract void run();

}
