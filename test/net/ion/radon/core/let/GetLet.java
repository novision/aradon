package net.ion.radon.core.let;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

public class GetLet extends AbstractLet{

	@Override
	public Representation myDelete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Representation myGet() throws Exception {
		return new StringRepresentation(GetLet.class.getCanonicalName());
	}

	@Override
	public Representation myPost(Representation entity) throws Exception {
		return new StringRepresentation(GetLet.class.getName());
	}

	@Override
	public Representation myPut(Representation entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}