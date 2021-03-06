package net.ion.nradon;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import net.ion.framework.util.Debug;
import net.ion.framework.util.IOUtil;
import net.ion.framework.util.InstanceCreationException;
import net.ion.nradon.config.RadonConfiguration;
import net.ion.radon.Options;
import net.ion.radon.core.config.AradonConstant;
import net.ion.radon.core.config.XMLConfig;

import org.apache.commons.configuration.ConfigurationException;

public class RadonServer {
	private Options options;
	private Radon radon;

	public RadonServer(Options options) throws ConfigurationException, InstanceCreationException, IOException {
		this.options = options;
		this.radon = createRadon(options.getInt("port", 0)) ;
	}

	public Radon start() throws Exception {
		int settedPort = getPort() ;
		if (settedPort > 0 && useAlreadyPortNum(settedPort)) {
			Debug.warn(settedPort + " port is occupied");
			throw new IllegalArgumentException(settedPort + " port is occupied");
		}

		radon.start();

		final RadonServer as = this;
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				as.stop();
			}
		});

		Debug.warn("RadonServer started : " + getPort());
		return radon;

	}
	
	private Radon createRadon(int portNum) throws InstanceCreationException{
		XMLConfig xmlConfig = XMLConfig.create(options.getString("config", AradonConstant.DEFAULT_CONFIG_PATH));
		if (portNum <= 1024) {
			return RadonConfiguration.newBuilder(xmlConfig).createRadon() ;
		} else {
			return RadonConfiguration.newBuilder(portNum, xmlConfig).createRadon() ;
		}
	}
	
	private boolean useAlreadyPortNum(int portNum) {
		Socket s = null;
		try {
			s = new Socket(InetAddress.getLocalHost(), portNum);
			s.setSoTimeout(400);
			s.close();
			return true;
		} catch (UnknownHostException e) {
			e.getStackTrace();
			return false;
		} catch (IOException e) {
			e.getStackTrace();
			return false;
		} finally {
			IOUtil.closeQuietly(s);
		}
	}

	public void stop() {
		if (radon != null) {
			try {
				radon.stop() ;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int getPort() {
		return radon.getConfig().getPort() ;
	}

	public Radon getRadon() {
		return radon;
	}

}
