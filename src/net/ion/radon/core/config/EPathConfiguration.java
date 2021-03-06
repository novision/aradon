package net.ion.radon.core.config;

import java.util.List;
import java.util.Map;

import net.ion.nradon.AbstractEventSourceResource;
import net.ion.nradon.filter.XRadonFilter;
import net.ion.radon.core.EnumClass.IMatchMode;

public class EPathConfiguration  extends LetConfiguration<EPathConfiguration> implements IPathConfiguration{

	private final String sectionName ;
	private final String name ;
	private final Class<? extends AbstractEventSourceResource> handlerClz ;
	private List<String> urlPatterns ;
	private String description ;
	private IMatchMode matchMode ;
	private Map<String, AttributeValue> attributes ;
	
	EPathConfiguration(String sectionName, String name, Class<? extends AbstractEventSourceResource> handlerClz, List<String> urlPatterns, String description, IMatchMode matchMode, Map<String, AttributeValue> attributes, List<XRadonFilter> filters) {
		super(attributes, filters) ;
		this.sectionName = sectionName ;
		this.name = name ;
		this.handlerClz = handlerClz ;
		this.urlPatterns = urlPatterns ;
		this.description = description ;
		this.matchMode = matchMode ;
		this.attributes = attributes ;
	}

	
	public String name(){
		return name ;
	}
	
	public Class<? extends AbstractEventSourceResource> handlerClz(){
		return handlerClz ;
	} 
	
	public List<String> urlPatterns(){
		return urlPatterns ;
	}
	
	public String fullURLPattern(){
		String longURLPattern = "";
		for (String urlPattern : urlPatterns) {
			if (longURLPattern.length() <= urlPattern.length()) longURLPattern = urlPattern ;
		}
		return "/" + sectionName + longURLPattern ;
	}
	
	public String description(){
		return description ;
	}
	
	public IMatchMode matchMode(){
		return matchMode ;
	}
	
	public Map<String, AttributeValue> attributes(){
		return attributes ;
	}

	public IMatchMode imatchMode() {
		return matchMode;
	}

}
