package org.raspinloop.web.riamodelstorageservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.raspinloop.web.riamodelstorageservice.db.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTest;
import com.graphql.spring.boot.test.GraphQLTestTemplate;

@RunWith(SpringRunner.class)
@GraphQLTest
class PackageMutationTest {

	@Autowired
	private GraphQLTestTemplate graphQLTestTemplate;

	@MockBean
	private ComponentStorageService componentService;

	@MockBean
	private InstanceStorageService instanceService;	

	@MockBean
	private PackageStorageService packageService;

	@Test
	void testDeleteNotExistingPackage() throws Exception {

		// given
		Mockito.when(packageService.deletePackage(Mockito.anyString())).thenReturn(false);

		// then
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("pack", "whatever.the.name");
		GraphQLResponse response = graphQLTestTemplate.perform("deletePackage.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertFalse(response.get("$.data.deletePackage", Boolean.class));
	}

	@Test
	void testDeletePackage() throws Exception {

		// given
		Mockito.when(packageService.deletePackage(Mockito.eq("Modelica.Electrical.Digital.Gates")))
				.thenReturn(true);
		// then
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("pack", "Modelica.Electrical.Digital.Gates");
		GraphQLResponse response = graphQLTestTemplate.perform("deletePackage.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertTrue(response.get("$.data.deletePackage", Boolean.class));
		Mockito.verify(packageService).deletePackage(Mockito.eq("Modelica.Electrical.Digital.Gates"));
	}

	@Test
	void testUpdateNotExistingComponent() throws Exception {

		// given... nothing... ;-)
		//Use ArgumentCaptor to capture the parameter value
		ArgumentCaptor<Package> param = ArgumentCaptor.forClass(Package.class);
		
		Mockito.when(packageService.updatePackage(
					 
					param.capture())).thenAnswer((invocation) -> 
					{						
						Package pack = param.getValue();
						pack.setId(3L);						
						return pack;
					});
		
		// then
		ObjectNode packInput = new ObjectMapper().createObjectNode();
		packInput.put("description", "Gate package");
		packInput.put("packageId", "Modelica.Electrical.Digital.Gates");
		packInput.put("svgIcon", "<svg/>");	

		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.set("pack", packInput);
		GraphQLResponse response = graphQLTestTemplate.perform("updatePackage.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertEquals((Long)3L, response.get("$.data.updatePackage.id", Long.class));
		assertEquals("Modelica.Electrical.Digital.Gates" , response.get("$.data.updatePackage.packageId"));	
	}
	
	@Test
	void testUpdateSubPackage() throws Exception {

		// given... nothing... ;-)
		//Use ArgumentCaptor to capture the parameter value
		
		ArgumentCaptor<Package> param = ArgumentCaptor.forClass(Package.class);
		
		Mockito.when(packageService.updatePackage(
					 
					param.capture())).thenAnswer((invocation) -> 
					{						
						Package pack = new Package();
	
						if (param.getValue().getDescription() != null)
							pack.setDescription(param.getValue().getDescription());
						if (param.getValue().getSvgIcon() != null)
							pack.setSvgIcon(param.getValue().getSvgIcon());
						if (param.getValue().getComponents() != null)
							pack.setComponents(param.getValue().getComponents());
						if (param.getValue().getPackageId() != null)
							pack.setPackageId(param.getValue().getPackageId());
						if (param.getValue().getPackages() != null)
							pack.setPackages(param.getValue().getPackages());
						if (param.getValue().getParent() != null)
							pack.setParent(param.getValue().getParent());	
						
						pack.setId((long)pack.hashCode()); //trick to have a unique Id for each record 						
						return pack;
					});
		
		ArgumentCaptor<Package> storedParam = ArgumentCaptor.forClass(Package.class);
		ArgumentCaptor<Package> packageParam = ArgumentCaptor.forClass(Package.class);
		Mockito.when(packageService.linkPackage(				 
					storedParam.capture(),
					packageParam.capture()))
				.thenAnswer((invocation) -> 
				{						
					Package stored = storedParam.getValue();
					stored.getPackages().add(packageParam.getValue());
					packageParam.getValue().setParent(stored);
					return stored;
				});
		// then
		ObjectMapper mapper = new ObjectMapper();
		
		ObjectNode pack1 = mapper.createObjectNode();
		pack1.put("description", "Or Gate package");
		pack1.put("packageId", "Modelica.Electrical.Digital.Gates.Ors");
		pack1.put("svgIcon", "<svg id ='2' />");	
		
		ObjectNode pack2 = mapper.createObjectNode();
		pack2.put("description", "And Gate package");
		pack2.put("packageId", "Modelica.Electrical.Digital.Gates.Ands");
		pack2.put("svgIcon", "<svg id='2'/>");	
		
		

		ArrayNode packageArray =mapper.createArrayNode();
		packageArray.add(pack1);
		packageArray.add(pack2);
		ObjectNode packInput = mapper.createObjectNode();
		packInput.put("description", "Gate package");
		packInput.put("packageId", "Modelica.Electrical.Digital.Gates");
		packInput.put("svgIcon", "<svg/>");			
		packInput.set("packages", packageArray);	

		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.set("pack", packInput);
		GraphQLResponse response = graphQLTestTemplate.perform("updatePackage.graphql", variables);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertEquals("Modelica.Electrical.Digital.Gates" , response.get("$.data.updatePackage.packageId"));
		Mockito.verify(packageService, Mockito.times(3)).updatePackage(Mockito.any());
		Mockito.verify(packageService, Mockito.times(2)).linkPackage(Mockito.any(), Mockito.any());		
	}
}
