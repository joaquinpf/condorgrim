package test.parallelization.condor;

import core.parallelization.condor.AbstractMFGS;

public class CondorTest extends AbstractMFGS {

	@Override
	public void run() {
		getselfdependency().doRun(null, null);
	}

}
