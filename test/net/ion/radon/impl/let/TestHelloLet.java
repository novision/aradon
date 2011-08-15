package net.ion.radon.impl.let;

import junit.framework.TestCase;
import net.ion.framework.util.Debug;
import net.ion.radon.client.AradonClient;
import net.ion.radon.client.AradonClientFactory;
import net.ion.radon.core.Aradon;
import net.ion.radon.core.SectionService;
import net.ion.radon.core.config.XMLConfig;
import net.ion.radon.core.let.InnerRequest;
import net.ion.radon.core.let.InnerResponse;
import net.ion.radon.impl.section.PathInfo;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Representation;

public class TestHelloLet extends TestCase {

	private Aradon aradon;
	public void setUp() throws Exception {
		this.aradon = new Aradon();
		aradon.init(XMLConfig.BLANK) ;
		
		SectionService section = aradon.attach("section", XMLConfig.BLANK); // base section..
		section.attach(PathInfo.create("test", "/hello", HelloWorldLet.class)) ;
	}
	
	@Override
	protected void tearDown() throws Exception {
		aradon.stop() ;
		
		super.tearDown();
	}

	public void testHello() throws Exception {
		Request request = new Request(Method.GET, "riap://component/section/hello");
		Response response = aradon.handle(request);

		assertEquals(Status.SUCCESS_OK, response.getStatus());
	}

	public void testGet() throws Exception {
		Request request = new Request(Method.GET, "riap://component/section/hello?abcd=test");
		Response response = aradon.handle(request);

		assertEquals("test", getInnerRequest().getFormParameter().get("abcd"));

	}

	public void testHello2() throws Exception {
		SectionService section = aradon.attach("test", XMLConfig.BLANK);
		section.attach(PathInfo.create("hello", "/hello", "", "", HelloWorldLet.class));

		Request request = new Request(Method.GET, "riap://component/test/hello");
		Response response = aradon.handle(request);

		Debug.debug(response.getEntityAsText());

	}

	public void testRun() throws Exception {
		aradon.startServer(9010) ;
		
		AradonClient ac = AradonClientFactory.create("http://localhost:9010") ;
		Representation repr = ac.createRequest("/section/hello").get() ;
		
		Debug.debug(repr, repr.getText()) ;
	}
	
	
	
	private InnerRequest getInnerRequest(){
		return ((InnerResponse)Response.getCurrent()).getInnerRequest() ;		
	}
}
