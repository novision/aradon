package net.ion.radon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.ion.framework.util.Debug;
import net.ion.radon.core.Aradon;
import net.ion.radon.core.RadonLogService;
import net.ion.radon.core.SectionService;
import net.ion.radon.core.TestAradon;
import net.ion.radon.core.TestBaseAradon;
import net.ion.radon.impl.section.PathInfo;

import org.junit.Test;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.resource.ResourceException;
import org.restlet.service.LogService;

public class TestAradonMethod extends TestBaseAradon {

	@Test
	public void testReload() throws Exception {
		Aradon aradon = testAradon();
		Request request = new Request(Method.GET, "riap://component/another/test");

		SectionService another = aradon.getChildService("another");
		assertEquals(3, another.getChildren().size());

		try {
			Response response = aradon.handle(request);
			assertEquals(404, response.getStatus().getCode());
		} catch (ResourceException ignore) {

		}

		another.attach(PathInfo.HELLO);
		Response response = aradon.handle(request);
		assertEquals(200, response.getStatus().getCode());

		assertEquals(4, another.getChildren().size());
	}

	@Test
	public void testDetach() throws Exception {
		Aradon aradon = testAradon();

		Request request = new Request(Method.GET, "riap://component/another/hello");

		SectionService another = aradon.getChildService("another");
		assertEquals(3, another.getChildren().size());
		Response response = aradon.handle(request);
		assertEquals(200, response.getStatus().getCode());

		aradon.detach("another");

		try {
			SectionService removed = aradon.getChildService("another");
			fail();
		} catch (IllegalArgumentException ignore) {
		}
	}

	@Test
	public void testSectionDetach() throws Exception {
		Aradon aradon = testAradon();

		SectionService another = aradon.getChildService("another");

		assertEquals(3, another.getChildren().size());
		another.detach("hello");
		assertEquals(2, another.getChildren().size());

		Request request = new Request(Method.GET, "riap://component/another/hello");
		Response response = new Response(request);
		aradon.handle(request, response);
		assertEquals(404, response.getStatus().getCode());

		request = new Request(Method.GET, "riap://component/another/ghello");
		response = aradon.handle(request);
		assertEquals(200, response.getStatus().getCode());
	}

	@Test
	public void testGetName() throws Exception {
		Aradon aradon = testAradon();

		assertEquals("aradon", aradon.getName());
		assertEquals("another", aradon.getChildService("another").getName());
	}

	@Test
	public void testGetPath() throws Exception {
		Aradon aradon = testAradon();

		assertEquals("/", aradon.getNamePath());
		assertEquals("/another/", aradon.getChildService("another").getNamePath());

	}

	@Test
	public void testLoggerService() throws Exception {
		Aradon aradon = testAradon();

		final LogService los = aradon.getLogService();
		los.start();

		Request request = new Request(Method.GET, "riap://component/another/hello");
		Response response = aradon.handle(request);

		assertTrue(los instanceof RadonLogService);

		Debug.debug(((RadonLogService) los).recentLog(aradon));
	}
}