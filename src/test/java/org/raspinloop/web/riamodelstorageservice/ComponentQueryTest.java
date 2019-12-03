package org.raspinloop.web.riamodelstorageservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.Port;
import org.raspinloop.web.riamodelstorageservice.db.PortGroup;
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
class ComponentQueryTest {

	@Autowired
	private GraphQLTestTemplate graphQLTestTemplate;

	@MockBean
	private ComponentStorageService componentService;

	@MockBean
	private InstanceStorageService instanceService;
	
	@MockBean
	private PackageStorageService packageService;	
	
	@Test
	void testGetUnknownComponent() throws Exception {

		// then
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("comp", "Modelica.NOT.EXISTING");
		GraphQLResponse response = graphQLTestTemplate.perform("component.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertNull(response.get("$.data.component"));
	}

	@Test
	void testGetComponent() throws Exception {

		// given
		Component orGate = TestEntity.createOrGate();

		Mockito.when(componentService.getComponent(Mockito.eq("Modelica.Electrical.Digital.Gates.OrGate")))
				.thenReturn(orGate);

		// then
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("comp", "Modelica.Electrical.Digital.Gates.OrGate");
		GraphQLResponse response = graphQLTestTemplate.perform("component.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertEquals("Modelica.Electrical.Digital.Gates.OrGate", response.get("$.data.component.componentId"));
	}

	@Test
	void testGetAllComponents() throws Exception {

		// given
		Component orGate = new Component();
		orGate.setComponentId("Modelica.Electrical.Digital.Gates.OrGate");
		Component andGate = new Component();
		andGate.setComponentId("Modelica.Electrical.Digital.Gates.AndGate");
		Component xorGate = new Component();
		xorGate.setComponentId("Modelica.Electrical.Digital.Gates.XorGate");
		ArrayList<Component> all = new ArrayList<>();
		all.add(orGate);
		all.add(andGate);
		all.add(xorGate);
		Mockito.when(componentService.getComponents())
				.thenReturn(all);
	

		// then
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("comp", "Modelica.Electrical.Digital.Gates.OrGate");
		GraphQLResponse response = graphQLTestTemplate.perform("allComponent.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertFalse( response.get("$.data.components[?(@.componentId=='Modelica.Electrical.Digital.Gates.OrGate')]", Collection.class).isEmpty());
		assertFalse( response.get("$.data.components[?(@.componentId=='Modelica.Electrical.Digital.Gates.OrGate')]", Collection.class).isEmpty());
		assertFalse( response.get("$.data.components[?(@.componentId=='Modelica.Electrical.Digital.Gates.OrGate')]", Collection.class).isEmpty());
	}
	
	
	@Test
	void testGetWithChildComponent() throws Exception {

		Component orGate = TestEntity.createOrGate();

		Set<PortGroup> portGroups = new HashSet<PortGroup>();
		PortGroup pg = new PortGroup();
		pg.setId(1L);
		pg.setName("PortGroup AB");
		pg.setSvg("<svg/>");
		Set<Port> ports = new HashSet<Port>();
		Port portA = new Port();
		portA.setDescription("Port A Desc");
		portA.setId(1L);
		portA.setPortId("Modelica.portA");
		portA.setX(1);
		portA.setY(2);
		ports.add(portA);
		Port portB = new Port();
		portB.setDescription("Port B Desc");
		portB.setId(2L);
		portB.setPortId("Modelica.portB");
		portB.setX(3);
		portB.setY(4);
		ports.add(portB);
		pg.setPorts(ports);
		portGroups.add(pg);
		orGate.setPortGroups(portGroups);		
		
		Mockito.when(componentService.getComponent(Mockito.eq("Modelica.Electrical.Digital.Gates.OrGate")))
				.thenReturn(orGate);

		// then
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("comp", "Modelica.Electrical.Digital.Gates.OrGate");
		GraphQLResponse response = graphQLTestTemplate.perform("componentWithChild.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertEquals("Modelica.Electrical.Digital.Gates.OrGate", response.get("$.data.component.componentId"));
		assertEquals("Modelica.portA", response.get("$.data.component.portGroups[0].ports[0].portId"));
		assertEquals("Modelica.portB", response.get("$.data.component.portGroups[0].ports[1].portId"));
				
	}
}
