/*
 * 
 */
package test.parallelization.condor;

import java.util.Hashtable;

import core.parallelization.condor.AbstractMFGS;
import core.parallelization.condor.BinaryManipulator;
import core.parallelization.condor.CondorMethods;
import core.parallelization.condor.CondorPackedOutput;

// TODO: Auto-generated Javadoc
/**
 * The Class CondorTest.
 * 
 * @author Joaquín Pérez Fuentes y Marcos Steimbach
 */
public class CondorTest extends AbstractMFGS {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 13498730928756288L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see core.parallelization.condor.AbstractMFGS#run()
	 */
	@Override
	public void run() {
		byte[] b = BinaryManipulator.readByteArray("x264.exe");
		b = BinaryManipulator.compressByteArray(b);
		CondorMethods selfDep = getselfdependency();
		CondorPackedOutput result;
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put("universe", "vanilla");
		if (selfDep != null) {
			result = (CondorPackedOutput) selfDep.doRun(b, h);
			System.out.println(result.getDescription());
		}
	}
}
