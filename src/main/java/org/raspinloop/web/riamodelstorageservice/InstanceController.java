package org.raspinloop.web.riamodelstorageservice;

import org.raspinloop.server.model.IComponent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class InstanceController {

  @RequestMapping(value = "/api/storage/instance/{instanceId}", //
          method = RequestMethod.GET, //
          produces = { MediaType.APPLICATION_JSON_VALUE, //
                  MediaType.APPLICATION_XML_VALUE })
  @ResponseBody
  public IComponent getInstance(@PathVariable("instanceId") Long componentId) {
	  return null;
  }

  @RequestMapping(value = "/api/storage/instance/{instanceId}", //
          method = RequestMethod.DELETE)
  public void deleteInstance(@PathVariable("instanceId") Long id) {

  }
  
  @RequestMapping(value = "/api/storage/instance/{instanceId}", //
          method = RequestMethod.PUT)
  public void updateInstance(@PathVariable("instanceId") Long id) {

  }
  
  @RequestMapping(value = "/api/storage/instance/create", //
          method = RequestMethod.POST, //
          produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  public void createInstance(@RequestBody IComponent component) {

  }

}