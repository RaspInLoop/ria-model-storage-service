package org.raspinloop.web.riamodelstorageservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTest;
import com.graphql.spring.boot.test.GraphQLTestTemplate;

@RunWith(SpringRunner.class)
@GraphQLTest
class ComponentMutationTest {

	@Autowired
	private GraphQLTestTemplate graphQLTestTemplate;

	@MockBean
	private ComponentStorageService componentService;

	@MockBean
	private InstanceStorageService instanceService;	


	@Test
	void testDeleteNotExistingComponent() throws Exception {

		// given
		Mockito.when(componentService.deleteComponent(Mockito.anyString())).thenReturn(false);

		// then
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("comp", "Modelica.NOT.EXISTING");
		GraphQLResponse response = graphQLTestTemplate.perform("deleteComponent.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertFalse(response.get("$.data.deleteComponent", Boolean.class));
	}

	@Test
	void testDeleteComponent() throws Exception {

		// given

		Mockito.when(componentService.deleteComponent(Mockito.eq("Modelica.Electrical.Digital.Gates.OrGate")))
				.thenReturn(true);
		// then
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("comp", "Modelica.Electrical.Digital.Gates.OrGate");
		GraphQLResponse response = graphQLTestTemplate.perform("deleteComponent.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertTrue(response.get("$.data.deleteComponent", Boolean.class));
		Mockito.verify(componentService).deleteComponent(Mockito.eq("Modelica.Electrical.Digital.Gates.OrGate"));
	}

	@Test
	void testUpdateNotExistingComponent() throws Exception {

		// given... nothing... ;-)
		//Use ArgumentCaptor to capture the value
		ArgumentCaptor<Component> param = ArgumentCaptor.forClass(Component.class);
		Mockito.when(componentService.updateComponent(param.capture())).thenAnswer((invocation) -> param.getValue());
		
		// then
		ObjectNode compInput = new ObjectMapper().createObjectNode();
		compInput.put("componentId", "Modelica.Electrical.Digital.Gates.OrGate");
		compInput.put("description", "New or updated OrGate");
		compInput.put("svgIcon", "<svg/>");
		compInput.put("htmlDocumentation", "<html/>");
		compInput.put("parameters", "{ \"parameters\": []}");

		ObjectNode portGroupAB = new ObjectMapper().createObjectNode();
		portGroupAB.put("name", "PortGroup AB");
		portGroupAB.put("svg", "<svg>pg<svg/>");
		ObjectNode portA = new ObjectMapper().createObjectNode();
		portA.put("description", "Port A Desc");
		portA.put("portId", "Modelica.portA");
		portA.put("x", 1);
		portA.put("y", 2);
		ObjectNode portB = new ObjectMapper().createObjectNode();
		portB.put("description", "Port B Desc");
		portB.put("portId", "Modelica.portB");
		portB.put("x", 3);
		portB.put("y", 4);

		portGroupAB.putArray("ports").add(portA).add(portB);

		compInput.putArray("portGroups").add(portGroupAB);

		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.set("comp", compInput);
		GraphQLResponse response = graphQLTestTemplate.perform("updateComponent.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertEquals("Modelica.Electrical.Digital.Gates.OrGate", response.get("$.data.updateComponent.componentId"));
		assertEquals(1 , response.get("$.data.updateComponent.portGroups[0].ports[?(@.portId=='Modelica.portA')]", Collection.class).size());
		assertEquals(1 , response.get("$.data.updateComponent.portGroups[0].ports[?(@.portId=='Modelica.portB')]", Collection.class).size());		
		Mockito.verify(componentService).updateComponent(Mockito.any());
	}
}
