package org.raspinloop.web.riamodelstorageservice.graphqls;

import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.Instance;

import com.coxautodev.graphql.tools.GraphQLResolver;

import lombok.RequiredArgsConstructor;

@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class InstanceResolver implements GraphQLResolver<Instance> {			

	
    public long getId(Instance inst) {
    	return inst.getId();
    }
    
    public String getParameters(Instance inst) {
    	return inst.getParameters();
    }
    
    public String getName(Instance inst) {
    	return inst.getInstanceName();
    }
    
    public Component getComponent(Instance inst) {
    	return inst.getComponentRef();    	
    }
}
