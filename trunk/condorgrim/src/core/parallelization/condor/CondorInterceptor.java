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
 * 
 * @author Joaqu�n P�rez Fuentes y Marcos Steimbach
 *
 */
public class CondorInterceptor implements MethodInterceptor, Serializable {

	//LA CLASE COMPLETA QUE SE EJECUTA, QUE EXTIENDE ABSTRACTMFGS
	protected MFGS ownerApp = null;
	
	private static final long serialVersionUID = 134223443754436288L;

	public Object invoke(MethodInvocation invoke) throws Throwable {
		
		ServiceCallInfo info = new ServiceCallInfo(
				invoke.getMethod().getName(), invoke.getArguments());
		
		CondorExecutionRequest req = new CondorExecutionRequest((AbstractMFGS)ownerApp, info);
		System.out.println("Enviando a nameserver: "
				+ NetworkConfigurator.CONDOR_SERVER + ":"
				+ NetworkConfigurator.CONDOR_SERVER_PORT);
		
		CondorClient client = new CondorClient(NetworkConfigurator.CONDOR_SERVER,
				NetworkConfigurator.CONDOR_SERVER_PORT);
		if (req != null){
			CondorExecutionResult result = client.execute(req);
			//setStateDependencies(result.getTarget(), getOwnerApp());
			if (result != null)
				return result.getResult();
		}
		return null;
	}

	public void setOwnerApp(MFGS ownerApp) {
		this.ownerApp = ownerApp;
	}

	public MFGS getOwnerApp() {
		return ownerApp;
	}

	protected void setStateDependencies(Object source, Object target)
			throws Exception {
		BeanUtils.copyProperties(source, target);
	}

	// Serializa la aplicacion y la setea como propiedad al objeto peer
	protected void setOwnerAgent(Object peer) throws Exception {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		GZIPOutputStream zStream = new GZIPOutputStream(bStream);
		SerializerObjectOutputStream oStream = new SerializerObjectOutputStream(
				zStream);
		oStream.writeObject(getOwnerApp());
		oStream.flush();
		zStream.finish();
		byte[] agentData = (byte[]) bStream.toByteArray();
		BeanUtilsBean.getInstance().copyProperty(peer, "serializedApp",
				agentData);
		oStream.close();
	}

}