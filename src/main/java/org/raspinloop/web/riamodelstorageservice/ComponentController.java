package org.raspinloop.web.riamodelstorageservice;

import org.raspinloop.server.model.IComponent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class ComponentController {

  @RequestMapping(value = "/api/storage/component/{name}", //
          method = RequestMethod.GET, //
          produces = { MediaType.APPLICATION_JSON_VALUE, //
                  MediaType.APPLICATION_XML_VALUE })
  @ResponseBody
  public IComponent getComponent(@PathVariable("name") Long componentId) {
	  return null;
  }

  @RequestMapping(value = "/api/storage/component/{name}", //
          method = RequestMethod.DELETE)
  public void deleteGraphModel(@PathVariable("name") Long id) {

  }
  
  @RequestMapping(value = "/api/storage/component/{name}", //
          method = RequestMethod.PUT)
  public void updateGraphModel(@PathVariable("name") Long id) {

  }

}