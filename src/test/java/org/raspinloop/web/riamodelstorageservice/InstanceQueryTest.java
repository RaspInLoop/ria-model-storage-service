package org.raspinloop.web.riamodelstorageservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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
class InstanceQueryTest {

	@Autowired
	private GraphQLTestTemplate graphQLTestTemplate;

	@MockBean
	private ComponentStorageService componentService;

	@MockBean
	private InstanceStorageService instanceService;	

	@MockBean
	private PackageStorageService packageService;
	
	@Test
	void testGetUnknownInstance() throws Exception {

		// then
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("inst", 3);
		GraphQLResponse response = graphQLTestTemplate.perform("instance.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertNull(response.get("$.data.instance"));
	}

	@Test
	void testGetInstance() throws Exception {

		// given
		Instance orGateInst = TestEntity.createOrGateInstance(3L);

		Mockito.when(instanceService.getInstance(Mockito.eq(3L)))
				.thenReturn(orGateInst);

		// then
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("inst", 3);
		GraphQLResponse response = graphQLTestTemplate.perform("instance.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertEquals((Long)3L, response.get("$.data.instance.id", Long.class));
		assertEquals("orGate_Inst", response.get("$.data.instance.name"));			
	}			
}
