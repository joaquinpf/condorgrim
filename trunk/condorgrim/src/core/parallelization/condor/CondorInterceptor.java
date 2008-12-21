package core.parallelization.condor;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPOutputStream;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.BeanUtils;

import utils.NetworkConfigurator;

//import utils.NetworkConfigurator;
import core.MFGS;
import core.mobility.io.SerializerObjectOutputStream;
import core.policy.ServiceCallInfo;

/**
 * The Class CondorInterceptor.
 * 
 * @author Joaquín Pérez Fuentes y Marcos Steimbach
 */
public class CondorInterceptor implements MethodInterceptor, Serializable {

	/** The owner app. */
	protected MFGS ownerApp = null;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 134223443754436288L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept
	 * .MethodInvocation)
	 */
	public Object invoke(MethodInvocation invoke) throws Throwable {

		ServiceCallInfo info = new ServiceCallInfo(
				invoke.getMethod().getName(), invoke.getArguments());

		CondorExecutionRequest req = new CondorExecutionRequest(
				(AbstractMFGS) ownerApp, info);
		System.out.println("Enviando a nameserver: "
				+ NetworkConfigurator.CONDOR_SERVER + ":"
				+ NetworkConfigurator.CONDOR_SERVER_PORT);

		CondorClient client = new CondorClient(
				NetworkConfigurator.CONDOR_SERVER,
				NetworkConfigurator.CONDOR_SERVER_PORT);
		if (req != null) {
			CondorExecutionResult result = client.execute(req);
			setStateDependencies(result.getTarget(), getOwnerApp());
			if (result != null)
				return result.getResult();
		}
		return null;
	}

	/**
	 * Sets the owner app.
	 * 
	 * @param ownerApp the new owner app
	 */
	public void setOwnerApp(MFGS ownerApp) {
		this.ownerApp = ownerApp;
	}

	/**
	 * Gets the owner app.
	 * 
	 * @return the owner app
	 */
	public MFGS getOwnerApp() {
		return ownerApp;
	}

	/**
	 * Sets the state dependencies.
	 * 
	 * @param source the source
	 * @param target the target
	 * 
	 * @throws Exception the exception
	 */
	protected void setStateDependencies(Object source, Object target)
			throws Exception {
		BeanUtils.copyProperties(source, target);
	}

	/**
	 * Sets the owner agent.
	 * 
	 * @param peer the new owner agent
	 * 
	 * @throws Exception the exception
	 */
	protected void setOwnerAgent(Object peer) throws Exception {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		GZIPOutputStream zStream = new GZIPOutputStream(bStream);
		SerializerObjectOutputStream oStream = new SerializerObjectOutputStream(
				zStream);
		oStream.writeObject(getOwnerApp());
		oStream.flush();
		zStream.finish();
		byte[] agentData = bStream.toByteArray();
		BeanUtilsBean.getInstance().copyProperty(peer, "serializedApp",
				agentData);
		oStream.close();
	}

}