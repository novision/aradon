package net.ion.radon.client;

import net.ion.radon.server.TestAradonClientFactory;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { TestAradonClientFactory.class, TestAradonClient.class, TestRequestParameter.class,  TestRequestHeader.class, TestRequestCookie.class, TestSerialRequest.class, TestJsonRequest.class, TestMultiPartRequest.class, TestClientCache.class, TestAsyncClient.class })
public class TestSuiteClient {

}
