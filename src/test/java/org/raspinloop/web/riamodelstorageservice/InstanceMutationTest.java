package org.raspinloop.web.riamodelstorageservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.raspinloop.web.riamodelstorageservice.db.Instance;
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
class InstanceMutationTest {

	@Autowired
	private GraphQLTestTemplate graphQLTestTemplate;

	@MockBean
	private ComponentStorageService componentService;

	@MockBean
	private InstanceStorageService instanceService;	

	@MockBean
	private PackageStorageService packageService;

	
	@Test
	void testDeleteNotExistingInstance() throws Exception {

		// given
		Mockito.when(instanceService.deleteInstance(Mockito.any())).thenReturn(false);

		// then
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("inst", 3);
		GraphQLResponse response = graphQLTestTemplate.perform("deleteInstance.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertFalse(response.get("$.data.deleteInstance", Boolean.class));
	}

	@Test
	void testDeleteInstance() throws Exception {

		// given

		Mockito.when(instanceService.deleteInstance(Mockito.eq(3L)))
				.thenReturn(true);
		// then
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("inst", 3);
		GraphQLResponse response = graphQLTestTemplate.perform("deleteInstance.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertTrue(response.get("$.data.deleteInstance", Boolean.class));
		Mockito.verify(instanceService).deleteInstance(Mockito.eq(3L));
	}

	@Test
	void testUpdateNotExistingComponent() throws Exception {

		// given... nothing... ;-)
		//Use ArgumentCaptor to capture the parameter value
		ArgumentCaptor<String> param = ArgumentCaptor.forClass(String.class);
		
		Mockito.when(instanceService.updateInstance(
					Mockito.eq(3L),
					Mockito.eq("orGate_inst"), 
					Mockito.eq("Modelica.Electrical.Digital.Gates.OrGate"), 
					param.capture())).thenAnswer((invocation) -> 
					{
						Instance inst = new Instance();
						inst.setId(3L);
						inst.setInstanceName("orGate_inst");
						inst.setParameters(param.getValue());
						inst.setComponentRef(TestEntity.createOrGate());
						return inst;
					});
		
		// then
		ObjectNode instInput = new ObjectMapper().createObjectNode();
		instInput.put("name", "orGate_inst");
		instInput.put("id", 3);
		instInput.put("componentId", "Modelica.Electrical.Digital.Gates.OrGate");
		instInput.put("parameters", "{ \"parameters\": []}");

		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.set("inst", instInput);
		GraphQLResponse response = graphQLTestTemplate.perform("updateInstance.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertEquals((Long)3L, response.get("$.data.updateInstance.id", Long.class));
		assertEquals("orGate_inst" , response.get("$.data.updateInstance.name"));
		assertEquals("Modelica.Electrical.Digital.Gates.OrGate" , response.get("$.data.updateInstance.component.componentId"));
	}
}
