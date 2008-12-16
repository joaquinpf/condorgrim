package test.parallelization.condor;

import java.util.Hashtable;

import core.parallelization.condor.AbstractMFGS;
import core.parallelization.condor.CondorMethods;

public class CondorTest extends AbstractMFGS {

	private static final long serialVersionUID = 13498730928756288L;

	@Override
	public void run() {
		byte[] b = (new String("pepe")).getBytes();
		CondorMethods selfDep = getselfdependency();
		String result;
		if (selfDep != null) {
			result = (String) selfDep.doRun(b,new Hashtable<String, String>());
			System.out.println(result);
		}
	}
}

