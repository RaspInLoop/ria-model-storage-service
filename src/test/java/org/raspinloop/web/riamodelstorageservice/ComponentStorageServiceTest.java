package org.raspinloop.web.riamodelstorageservice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(ServiceTestConfiguration.class)
public class ComponentStorageServiceTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ComponentStorageService service;
	
	@Autowired
	private ComponentRepository repo;

	@Test
	public void findTest() {
		// given
		Component orGate = new Component();
		orGate.setDescription("info=\"<html>\r\n"
				+ "<p>The OrGate model has a multiple valued (n) input vector, and a single valued output. It is composed by a Basic Or and an InertialDelaySensitive. Its parameters are the delay parameters (rise and fall inertial delay time, and initial value).</p>\r\n"
				+ "</html>\", revisions=\r\n" + "         \"<html>\r\n" + "<ul>\r\n"
				+ "<li><i>September 15, 2004</i> vector approach used for all fixed numbers of inputs\r\n"
				+ "by Christoph Clauss<br>\r\n" + "</li>\r\n" + "<li><i>October 22, 2003</i>\r\n"
				+ "by Teresa Schlegel<br>\r\n" + "initially modelled.</li>\r\n" + "</ul>\r\n" + "</html>\"");
		orGate.setComponentId("Modelica.Electrical.Digital.Gates.OrGate");
		orGate.setSvgContent("<svg>content</svg>");
		orGate.setSvgIcon("<svg>icon</svg>");
		entityManager.persist(orGate);
		entityManager.flush();

		// when
		Component found = service.getComponent("Modelica.Electrical.Digital.Gates.OrGate");

		// then
		assertThat(found.getSvgContent()).isEqualTo("<svg>content</svg>");
	}
	
	@Test
	public void deleteTest() {
		// given
		Component orGate = new Component();
		orGate.setDescription("info=\"<html>\r\n"
				+ "<p>The OrGate model has a multiple valued (n) input vector, and a single valued output. It is composed by a Basic Or and an InertialDelaySensitive. Its parameters are the delay parameters (rise and fall inertial delay time, and initial value).</p>\r\n"
				+ "</html>\", revisions=\r\n" + "         \"<html>\r\n" + "<ul>\r\n"
				+ "<li><i>September 15, 2004</i> vector approach used for all fixed numbers of inputs\r\n"
				+ "by Christoph Clauss<br>\r\n" + "</li>\r\n" + "<li><i>October 22, 2003</i>\r\n"
				+ "by Teresa Schlegel<br>\r\n" + "initially modelled.</li>\r\n" + "</ul>\r\n" + "</html>\"");
		orGate.setComponentId("Modelica.Electrical.Digital.Gates.OrGate");
		orGate.setSvgContent("<svg>content</svg>");
		orGate.setSvgIcon("<svg>icon</svg>");
		Long existingId = entityManager.persist(orGate).getId();
		entityManager.flush();		
		
		
		// when
		service.deleteComponent("Modelica.Electrical.Digital.Gates.OrGate");
		
		// then
		assertFalse(repo.findById(existingId).isPresent());	
		
	}
	
	@Test
	public void updateTest() {
		// given
		Component orGate = new Component();
		orGate.setDescription("info=\"<html>\r\n"
				+ "<p>The OrGate model has a multiple valued (n) input vector, and a single valued output. It is composed by a Basic Or and an InertialDelaySensitive. Its parameters are the delay parameters (rise and fall inertial delay time, and initial value).</p>\r\n"
				+ "</html>\", revisions=\r\n" + "         \"<html>\r\n" + "<ul>\r\n"
				+ "<li><i>September 15, 2004</i> vector approach used for all fixed numbers of inputs\r\n"
				+ "by Christoph Clauss<br>\r\n" + "</li>\r\n" + "<li><i>October 22, 2003</i>\r\n"
				+ "by Teresa Schlegel<br>\r\n" + "initially modelled.</li>\r\n" + "</ul>\r\n" + "</html>\"");
		orGate.setComponentId("Modelica.Electrical.Digital.Gates.OrGate");
		orGate.setSvgContent("<svg>content</svg>");
		orGate.setSvgIcon("<svg>icon</svg>");
		Long existingId = entityManager.persist(orGate).getId();
		entityManager.flush();
		
		Component newOrGate = new Component();
		newOrGate.setDescription("shorter desc");
		newOrGate.setComponentId("Modelica.Electrical.Digital.Gates.OrGate");
		newOrGate.setSvgContent("<svg>content</svg>");
		newOrGate.setSvgIcon("<svg>icon</svg>");
		// when
		service.updateComponent(newOrGate);
		
		// then

		Optional<Component> updatedOpt = repo.findById(existingId);
		assertTrue(updatedOpt.isPresent());
		assertThat(updatedOpt.get().getDescription()).isEqualTo("shorter desc");
	}

	@Test
	public void updateNewTest() {
		// given
		Component found = service.getComponent("Modelica.Electrical.Digital.Gates.OrGate");
		assertThat(found).isNull();
		
		Component newOrGate = new Component();
		newOrGate.setDescription("shorter desc");
		newOrGate.setComponentId("Modelica.Electrical.Digital.Gates.OrGate");
		newOrGate.setSvgContent("<svg>content</svg>");
		newOrGate.setSvgIcon("<svg>icon</svg>");
		// when
		service.updateComponent(newOrGate);
		
		// then
		found = service.getComponent("Modelica.Electrical.Digital.Gates.OrGate");
		assertThat(found.getDescription()).isEqualTo("shorter desc");
	}
}
