package test.parallelization.condor;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import utils.NetworkConfigurator;

public class LaunchCondorTest {

	public static void main(String[] args) throws Exception {
		NetworkConfigurator.configure("configuration/network.properties");
		PropertyConfigurator.configure("configuration/log4j.properties");
		ApplicationContext appContext = new FileSystemXmlApplicationContext(
				"configuration/simple-condor.xml");
		Runnable service = (Runnable) appContext.getBean("service");
		service.run();
	}

}
