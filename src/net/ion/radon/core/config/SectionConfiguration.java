package net.ion.radon.core.config;

import java.util.List;
import java.util.Map;

import net.ion.framework.util.InstanceCreationException;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.MapUtil;
import net.ion.radon.core.filter.IRadonFilter;

import org.apache.commons.configuration.ConfigurationException;


public class SectionConfiguration extends LetConfiguration<SectionConfiguration>{

	private final String name ;
	private final List<PathConfiguration> pconfigs ;
	
	SectionConfiguration(String name, List<PathConfiguration> pconfigs, Map<String, AttributeValue> attributes, List<IRadonFilter> prefilters, List<IRadonFilter> afterfilters) {
		super(attributes, prefilters, afterfilters);
		this.name = name;
		this.pconfigs = pconfigs;
	}

	public final static SectionConfiguration createBlank(String name){
		return new SectionConfiguration(name, ListUtil.<PathConfiguration>newList(), MapUtil.<String, AttributeValue>newMap(), ListUtil.<IRadonFilter>newList(), ListUtil.<IRadonFilter>newList()) ;
	}
	
	public final static SectionConfiguration create(String name, List<PathConfiguration> paths){
		return new SectionConfiguration(name, paths, MapUtil.<String, AttributeValue>newMap(), ListUtil.<IRadonFilter>newList(), ListUtil.<IRadonFilter>newList()) ;
	}
	
	public List<PathConfiguration> pathConfiguration() {
		return pconfigs;
	}

	public String name() {
		return name;
	}

	public PathConfiguration path(String name) {
		for (PathConfiguration pconfig : pconfigs) {
			if (pconfig.name().equalsIgnoreCase(name)) return pconfig ;
		}
		throw new IllegalArgumentException("not found section :" + name) ;
	}

	SectionConfiguration addPathConfiguration(PathConfiguration pconfig){
		pconfigs.add(pconfig) ;
		
		return this ;
	}

	public void removePath(String name) {
		PathConfiguration[] pcs = pconfigs.toArray(new PathConfiguration[0]) ;
		for (PathConfiguration pc : pcs) {
			if (name.equals(pc.name())) pconfigs.remove(pc) ;
		}
	}
	
	public static SectionsConfiguration fromLoad(XMLConfig sconfig) throws ConfigurationException, InstanceCreationException {
		return ConfigurationBuilder.load(sconfig).build().aradon().sections() ;
	}
	
	public String toString(){
		return this.getClass().getName() + "[" + name() + "]" ;
	}
}
