package net.ion.nradon.handler;

import net.ion.nradon.HttpControl;
import net.ion.nradon.HttpHandler;
import net.ion.nradon.HttpRequest;
import net.ion.nradon.HttpResponse;

import java.nio.charset.Charset;

public class StringHttpHandler implements HttpHandler {

	private final String contentType;
	private final String body;
	private final Charset charset;

	public StringHttpHandler(String contentType, String body) {
		this(contentType, body, Charset.forName("UTF-8"));
	}

	public StringHttpHandler(String contentType, String body, Charset charset) {
		this.contentType = contentType;
		this.charset = charset;
		this.body = body;
	}

	public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) {
		response.charset(charset).header("Content-Type", contentType + "; charset=" + charset.name()).header("Content-Length", body.length()).content(body).end();
	}

}
