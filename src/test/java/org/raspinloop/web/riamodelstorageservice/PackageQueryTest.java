package org.raspinloop.web.riamodelstorageservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.raspinloop.web.riamodelstorageservice.db.Package;
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
class PackageQueryTest {

	@Autowired
	private GraphQLTestTemplate graphQLTestTemplate;

	@MockBean
	private ComponentStorageService componentService;

	@MockBean
	private InstanceStorageService instanceService;	

	@MockBean
	private PackageStorageService packageService;
	
	@Test
	void testGetUnknownPackage() throws Exception {

		// then
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("pack", "pwet.pouet.bleh");
		GraphQLResponse response = graphQLTestTemplate.perform("package.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertNull(response.get("$.data.package"));
	}

	@Test
	void testGetPackage() throws Exception {

		// given
		Package gate = new Package();
		gate.setId(3L);
		gate.setDescription("info=\"<html>\r\n"
				+ "<p>The OrGate model has a multiple valued (n) input vector, and a single valued output. It is composed by a Basic Or and an InertialDelaySensitive. Its parameters are the delay parameters (rise and fall inertial delay time, and initial value).</p>\r\n"
				+ "</html>\", revisions=\r\n" + "         \"<html>\r\n" + "<ul>\r\n"
				+ "<li><i>September 15, 2004</i> vector approach used for all fixed numbers of inputs\r\n"
				+ "by Christoph Clauss<br>\r\n" + "</li>\r\n" + "<li><i>October 22, 2003</i>\r\n"
				+ "by Teresa Schlegel<br>\r\n" + "initially modelled.</li>\r\n" + "</ul>\r\n" + "</html>\"");
		gate.setPackageId("Modelica.Electrical.Digital.Gates");
		gate.setSvgIcon("<svg>icon</svg>");

		Mockito.when(packageService.getPackage(Mockito.eq("Modelica.Electrical.Digital.Gates")))
				.thenReturn(gate);

		// then
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("pack", "Modelica.Electrical.Digital.Gates");
		GraphQLResponse response = graphQLTestTemplate.perform("package.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertEquals((Long)3L, response.get("$.data.package.id", Long.class));
		assertEquals("<svg>icon</svg>", response.get("$.data.package.svgIcon"));			
	}			
}
