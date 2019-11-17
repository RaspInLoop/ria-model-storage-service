package org.raspinloop.web.riamodelstorageservice.graphqls;

import org.raspinloop.web.riamodelstorageservice.db.Port;

import com.coxautodev.graphql.tools.GraphQLResolver;

@org.springframework.stereotype.Component
public class PortResolver implements GraphQLResolver<Port> {

    public String getDescription(Port pg) {
    	return pg.getDescription();
    }
    
    public String getPortId(Port pg) {
    	return pg.getPortId();
    }
    
    public int getX(Port pg) {
    	return pg.getX();
    }
    
    public int getY(Port pg) {
    	return pg.getY();
    }   
}
