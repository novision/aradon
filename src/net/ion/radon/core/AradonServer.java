package net.ion.radon.core;

import java.io.IOException;

import net.ion.framework.util.Debug;
import net.ion.framework.util.InstanceCreationException;
import net.ion.framework.util.NumberUtil;
import net.ion.radon.Options;
import net.ion.radon.core.config.AradonConstant;

import org.apache.commons.configuration.ConfigurationException;
import org.restlet.data.Protocol;

public class AradonServer {
	private Options options;
	private final Aradon aradon;

	public AradonServer(Options options) throws ConfigurationException, InstanceCreationException, IOException {
		this.options = options;
		this.aradon = newAradon();
		this.aradon.start() ;
	}

	public Aradon start() throws Exception {

		// Now, let's start the component! Note that the HTTP server connector is also automatically started.
		aradon.startServer(settedPort());

		final AradonServer as = this;
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				as.stop();
			}
		});

		Debug.warn("AradonServer started : ");
		return aradon;
	}

	public void stop() {
		if (aradon != null) {
			aradon.destorySelf();
		}
	}

	private int settedPort(){
		return options.getInt("port", 0);
	}
	
	public int getPort(){
		return NumberUtil.toIntWithMark(aradon.getServiceContext().getAttributeObject(AradonConstant.CONFIG_PORT), 9000) ;
	}
	
	private Aradon newAradon() throws ConfigurationException, InstanceCreationException {
		Aradon newAradon = Aradon.create(options.getString("config", AradonConstant.DEFAULT_CONFIG_PATH)) ;

		newAradon.getClients().add(Protocol.HTTP);
		newAradon.getClients().add(Protocol.HTTPS);
		newAradon.getClients().add(Protocol.FILE);
		newAradon.getClients().add(Protocol.RIAP);

		return newAradon;
	}

	public static void main(String[] args) throws Exception {
		AradonServer as = new AradonServer(new Options(args));
		Aradon aradon = as.start();

	}

	public Aradon getAradon() {
		return aradon;
	}

}
