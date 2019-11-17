package org.raspinloop.web.riamodelstorageservice.graphqls;

import java.util.Set;

import org.raspinloop.web.riamodelstorageservice.db.Port;
import org.raspinloop.web.riamodelstorageservice.db.PortGroup;

import com.coxautodev.graphql.tools.GraphQLResolver;

@org.springframework.stereotype.Component
public class PortGroupResolver implements GraphQLResolver<PortGroup> {
   
    public String getName(PortGroup pg) {
    	return pg.getName();
    }
    
    public String getSvg(PortGroup pg) {
    	return pg.getSvg();
    }
    
	public Set<Port> getPortGroups(PortGroup pg) {
        return pg.getPorts();
    }
}
