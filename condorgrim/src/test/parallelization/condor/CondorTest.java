package test.parallelization.condor;

import core.parallelization.condor.AbstractMFGS;
import core.parallelization.condor.CondorMethods;

public class CondorTest extends AbstractMFGS {

	private static final long serialVersionUID = 13498730928756288L;

	@Override
	public void run() {
		CondorMethods selfDep = getselfdependency();
		if (selfDep != null)
			selfDep.doRun(null, null);
	}
}

