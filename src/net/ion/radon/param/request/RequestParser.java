package net.ion.radon.param.request;

import java.util.Map;

import net.ion.framework.parse.gson.JsonElement;
import net.ion.framework.parse.gson.JsonObject;
import net.ion.radon.param.MyParameter;

public class RequestParser {

	private MyParameter param ;
	private RequestParser(MyParameter param) {
		this.param = param ;
	}

	public RequestParser(String pname, Object value) {
		// TODO Auto-generated constructor stub
	}

	public static RequestParser create(MyParameter param) {
		return new RequestParser(param) ;
	}

	public final static AradonRequest parse(String param){
		return parse(MyParameter.create(param)) ;
	}
	
	
	public final static AradonRequest parse(MyParameter param) {
		Object[] reqs = param.getParams("aradon") ;
		AradonRequest arequest = AradonRequest.create() ; 

		for (int i=0 ; i < reqs.length ; i++) {
			arequest.append(SingleRequest.create((Map)reqs[i])) ;
		}
		return arequest ;
	}

}
