package org.raspinloop.web.riamodelstorageservice.graphqls;

import java.util.Set;

import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.PortGroup;

import com.coxautodev.graphql.tools.GraphQLResolver;

@org.springframework.stereotype.Component
public class ComponentResolver implements GraphQLResolver<Component> {
		
    public long getId(Component comp) {
    	return comp.getId();
    }
    
    public String getComponentId(Component comp) {
    	return comp.getComponentId();
    }
    
    public String getDescription(Component comp) {
    	return comp.getDescription();
    }

    public String getSvgContent(Component comp) {
    	return comp.getSvgContent();
    }
    
    public String getSvgIcon(Component comp) {
    	return comp.getSvgIcon();
    }

    public String getHtmlDocumentation(Component comp) {
    	return comp.getHtmlDocumentation();
    }
    
    public String getParameters(Component comp) {
    	return comp.getParameters();
    }
    
	public Set<PortGroup> getPortGroups(Component comp) {
        return comp.getPortGroups();
    }
}
