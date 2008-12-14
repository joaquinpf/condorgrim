package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

public class NetworkConfigurator {

	public static String LOCAL_HOST = "localhost";

	public static String CONDOR_SERVER = "localhost";

	public static int CONDOR_SERVER_PORT = 9618;

	public static int CONTINUATION_SERVER_PORT = 9000;

	public static int CLASS_SERVER_PORT = 9001;

	/**
	 * Lee las propiedades de configuracion de la red desde un archivo pasado
	 * como parametro y luego llama al metodo configure(Properties props) para
	 * realizar el seteo de las opciones de configuracion correspondientes
	 * previamente leidas.
	 * @param filename filename
	 * @throws IOException
	 */
	public static void configure(String filename) throws IOException {
		Properties nProperties = new Properties();
		nProperties.load(new FileInputStream(filename));
		configure(nProperties);
	}

	/**
	 * Realiza el seteo de las propiedades de la red pasadas como parametro.
	 * @param props Properties
	 */
	public static void configure(Properties props) {
		Logger log = Logger.getLogger(NetworkConfigurator.class);
		if (props.containsKey("http.proxyHost"))
			System.getProperties().put("http.proxyHost",
					props.get("http.proxyHost"));
		if (props.containsKey("http.proxyPort"))
			System.getProperties().put("http.proxyPort",
					props.get("http.proxyPort"));
		if (props.containsKey("http.nonProxyHosts"))
			System.getProperties().put("http.nonProxyHosts",
					props.get("http.nonProxyHosts"));
		if (props.containsKey("condor.server"))
			CONDOR_SERVER = (String) props.get("condor.server");
		LOCAL_HOST = (String) props.get("local.host");
		try {
			CONTINUATION_SERVER_PORT = Integer.parseInt((String) props
					.get("mfgs.server.port"));
		} catch (NumberFormatException e) {
			log.error("Invalid format in property: mfgs.server.port");
		}
		try {
			CLASS_SERVER_PORT = Integer.parseInt((String) props
					.get("mfgs.classes.server.port"));
		} catch (NumberFormatException e) {
			log.error("Invalid format in property: mfgs.classes.server.port");
		}
		try {
			CONDOR_SERVER_PORT = Integer.parseInt((String) props
					.get("condor.server.port"));
		} catch (NumberFormatException e) {
			log.error("Invalid format in property: condor.server.port");
		}
	}

	/**
	 * Devuelve la direccion del host pasado por parametro como una URL.
	 * @param fullUrl
	 * @return
	 * @throws Exception
	 */
	public static String parseHostAddr(String fullUrl) throws Exception {
		URL url = new URL(fullUrl);
		String host = url.getHost();
		return InetAddress.getByName(host).getHostAddress();
	}

}
