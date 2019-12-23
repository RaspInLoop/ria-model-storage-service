package org.raspinloop.web.riamodelstorageservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.Package;
import org.raspinloop.web.riamodelstorageservice.db.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(ServiceTestConfiguration.class)
public class PackageStorageServiceTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private PackageStorageService service;
	
	@Autowired
	private PackageRepository repo;

	@Test
	public void findTest() {
		// given
		Package gate = new Package();
		gate.setDescription("info=\"<html>\r\n"
				+ "<p>The OrGate model has a multiple valued (n) input vector, and a single valued output. It is composed by a Basic Or and an InertialDelaySensitive. Its parameters are the delay parameters (rise and fall inertial delay time, and initial value).</p>\r\n"
				+ "</html>\", revisions=\r\n" + "         \"<html>\r\n" + "<ul>\r\n"
				+ "<li><i>September 15, 2004</i> vector approach used for all fixed numbers of inputs\r\n"
				+ "by Christoph Clauss<br>\r\n" + "</li>\r\n" + "<li><i>October 22, 2003</i>\r\n"
				+ "by Teresa Schlegel<br>\r\n" + "initially modelled.</li>\r\n" + "</ul>\r\n" + "</html>\"");
		gate.setPackageId("Modelica.Electrical.Digital.Gates");
		gate.setSvgIcon("<svg>icon</svg>");
		entityManager.persist(gate);
		entityManager.flush();

		// when
		Package found = service.getPackage("Modelica.Electrical.Digital.Gates");

		// then
		assertThat(found.getSvgIcon()).isEqualTo("<svg>icon</svg>");
	}
	
	@Test
	public void deleteTest() {
		// given
		Package gate = new Package();
		gate.setDescription("info=\"<html>\r\n"
				+ "<p>The OrGate model has a multiple valued (n) input vector, and a single valued output. It is composed by a Basic Or and an InertialDelaySensitive. Its parameters are the delay parameters (rise and fall inertial delay time, and initial value).</p>\r\n"
				+ "</html>\", revisions=\r\n" + "         \"<html>\r\n" + "<ul>\r\n"
				+ "<li><i>September 15, 2004</i> vector approach used for all fixed numbers of inputs\r\n"
				+ "by Christoph Clauss<br>\r\n" + "</li>\r\n" + "<li><i>October 22, 2003</i>\r\n"
				+ "by Teresa Schlegel<br>\r\n" + "initially modelled.</li>\r\n" + "</ul>\r\n" + "</html>\"");
		gate.setPackageId("Modelica.Electrical.Digital.Gates");
		gate.setSvgIcon("<svg>icon</svg>");
		Long existingId = entityManager.persist(gate).getId();
		entityManager.flush();		
		
		
		// when
		service.deletePackage("Modelica.Electrical.Digital.Gates");
		
		// then
		assertFalse(repo.findById(existingId).isPresent());	
		
	}
	
	@Test
	public void updateTest() {
		// given
		Package gate = new Package();
		gate.setDescription("info=\"<html>\r\n"
				+ "<p>The OrGate model has a multiple valued (n) input vector, and a single valued output. It is composed by a Basic Or and an InertialDelaySensitive. Its parameters are the delay parameters (rise and fall inertial delay time, and initial value).</p>\r\n"
				+ "</html>\", revisions=\r\n" + "         \"<html>\r\n" + "<ul>\r\n"
				+ "<li><i>September 15, 2004</i> vector approach used for all fixed numbers of inputs\r\n"
				+ "by Christoph Clauss<br>\r\n" + "</li>\r\n" + "<li><i>October 22, 2003</i>\r\n"
				+ "by Teresa Schlegel<br>\r\n" + "initially modelled.</li>\r\n" + "</ul>\r\n" + "</html>\"");
		gate.setPackageId("Modelica.Electrical.Digital.Gates");
		gate.setSvgIcon("<svg>icon</svg>");
		Long existingId = entityManager.persist(gate).getId();
		entityManager.flush();
		
		Package newGate = new Package();
		newGate.setDescription("shorter desc");
		newGate.setPackageId("Modelica.Electrical.Digital.Gates");
		newGate.setSvgIcon("<svg>icon</svg>");
		// when
		service.updatePackage(newGate);
		
		// then

		Optional<Package> updatedOpt = repo.findById(existingId);
		assertTrue(updatedOpt.isPresent());
		assertThat(updatedOpt.get().getDescription()).isEqualTo("shorter desc");
	}

	@Test
	public void updateNewTest() {
		// given
		Package found = service.getPackage("Modelica.Electrical.Digital.Gates");
		assertThat(found).isNull();
		
		Package gate = new Package();
		gate.setDescription("shorter desc");
		gate.setPackageId("Modelica.Electrical.Digital.Gates");
		gate.setSvgIcon("<svg>icon</svg>");
		// when
		service.updatePackage(gate);
		
		// then
		found = service.getPackage("Modelica.Electrical.Digital.Gates");
		assertThat(found.getDescription()).isEqualTo("shorter desc");
	}
	
	@Test
	public void subPackageTest() {
		// given
		Package found = service.getPackage("Modelica");
		assertThat(found).isNull();
		
		
		Package gate = new Package();	
		gate.setPackageId("Modelica.Electrical.Digital.Gates");	
		entityManager.persist(gate);
		Package digital = new Package();	
		digital.setPackageId("Modelica.Electrical.Digital");
		digital.getPackages().add(gate);
		entityManager.persist(digital);
		Package electrical = new Package();	
		electrical.setPackageId("Modelica.Electrical");
		electrical.getPackages().add(digital);
		entityManager.persist(electrical);
		Package modelica = new Package();	
		modelica.setPackageId("Modelica");	
		modelica.getPackages().add(electrical);
		entityManager.persist(modelica);		
		
		// when
		found = service.getPackage("Modelica");
		
		// then
		assertThat(found.getPackageId()).isEqualTo("Modelica");
		
		found = found.getPackages().iterator().next();
		assertThat(found.getPackageId()).isEqualTo("Modelica.Electrical");
		
		found = found.getPackages().iterator().next();
		assertThat(found.getPackageId()).isEqualTo("Modelica.Electrical.Digital");
		
		found = found.getPackages().iterator().next();
		assertThat(found.getPackageId()).isEqualTo("Modelica.Electrical.Digital.Gates");
	}
	

	@Test
	public void linkPackageTest() {
		Package gate = new Package();	
		gate.setPackageId("Modelica.Electrical.Digital.Gates");	
		entityManager.persist(gate);
		
		Package digital = new Package();	
		digital.setPackageId("Modelica.Electrical.Digital");		
		entityManager.persist(digital);
		
		Package electrical = new Package();	
		electrical.setPackageId("Modelica.Electrical");		
		entityManager.persist(electrical);
		
		Package modelica = new Package();	
		modelica.setPackageId("Modelica");			
		entityManager.persist(modelica);
		
		// when
		service.linkPackage(modelica, electrical);
		service.linkPackage(electrical, digital);
		service.linkPackage(digital, gate);

		// then
		Package found = service.getPackage("Modelica");
		assertThat(found.getPackageId()).isEqualTo("Modelica");
		
		found = found.getPackages().iterator().next();
		assertThat(found.getPackageId()).isEqualTo("Modelica.Electrical");
		
		found = found.getPackages().iterator().next();
		assertThat(found.getPackageId()).isEqualTo("Modelica.Electrical.Digital");
		
		found = found.getPackages().iterator().next();
		assertThat(found.getPackageId()).isEqualTo("Modelica.Electrical.Digital.Gates");
	}
	
	@Test
	public void linkComponentTest() {
		//given
		Component orGate = new Component();
		orGate.setDescription("OR short desc");
		orGate.setComponentId("Modelica.Electrical.Digital.Gates.OrGate");
		orGate.setSvgContent("<svg>OR content</svg>");
		orGate.setSvgIcon("<svg>OR icon</svg>");
		entityManager.persist(orGate);
		
		Component andGate = new Component();
		andGate.setDescription("AND short desc");
		andGate.setComponentId("Modelica.Electrical.Digital.Gates.AndGate");
		andGate.setSvgContent("<svg>AND content</svg>");
		andGate.setSvgIcon("<svg>AND icon</svg>");
		entityManager.persist(andGate);
		
		Package gate = new Package();	
		gate.setPackageId("Modelica.Electrical.Digital.Gates");	
		entityManager.persist(gate);
		
		entityManager.flush();
		//and ensure 
		Package found = service.getPackage("Modelica.Electrical.Digital.Gates");
		assertThat(found.getComponents()).isEmpty();
		
		//when 
		service.linkComponent(found, "Modelica.Electrical.Digital.Gates.OrGate");
		service.linkComponent(found, "Modelica.Electrical.Digital.Gates.AndGate");
		
		//then
		assertThat(found.getComponents()).contains(orGate, andGate);
	}
}
