import java.util.Hashtable;

import core.MFGS;

public abstract class AbstractMFGS extends MFGS {

	protected CondorMethods selfdependency = null;
	
	public CondorMethods getselfdependency() {
		return selfdependency;
	}

	public void setselfdependency(CondorMethods selfdependency) {
		this.selfdependency = selfdependency;
	}

	public Object doRun(byte[] executable, Hashtable<String, String> properties) {
		//TODO CUERPO DE LA EJECUCION, se debe grabar el archivo a disco.
		return null;
	}

	public abstract void run();

}
