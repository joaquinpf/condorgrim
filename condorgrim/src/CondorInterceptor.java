

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.BeanUtils;

import utils.NetworkConfigurator;
import core.MFGS;
import core.mobility.io.SerializerObjectOutputStream;
import core.policy.ServiceCallInfo;

/**
 * 
 * @author Joaquín Pérez Fuentes y Marcos Steimbach
 *
 */
public class CondorInterceptor implements MethodInterceptor {

	//LA CLASE COMPLETA QUE SE EJECUTA, QUE EXTIENDE ABSTRACTMFGS
	protected MFGS ownerApp = null;

	public Object invoke(MethodInvocation invoke) throws Throwable {
		// Carga la clase Ibis asociada a la aplicaciÃ³n JGRIM
		/*
		SatinObject peer = (SatinObject) Class.forName(
				getOwnerApp().getClass().getName() + "Peer").newInstance();
		System.out.println("Creando Ibis peer: " + peer.getClass().getName());
		setStateDependencies(getOwnerApp(), peer);
		setOwnerAgent(peer);
		*/
		
		ServiceCallInfo info = new ServiceCallInfo(
				invoke.getMethod().getName(), invoke.getArguments());
		
		CondorExecutionRequest req = new CondorExecutionRequest((AbstractMFGS)ownerApp, info);
		System.out.println("Enviando a nameserver: "
				+ NetworkConfigurator.IBIS_SERVER + ":"
				+ NetworkConfigurator.IBIS_SERVER_PORT);
		
		CondorClient client = new CondorClient(NetworkConfigurator.IBIS_SERVER,
				NetworkConfigurator.IBIS_SERVER_PORT);
		CondorExecutionResult result = client.execute(req);
		//setStateDependencies(result.getTarget(), getOwnerApp());
		return result.getResult();
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