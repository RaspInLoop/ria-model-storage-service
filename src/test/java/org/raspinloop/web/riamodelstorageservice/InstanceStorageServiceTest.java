package org.raspinloop.web.riamodelstorageservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.Instance;
import org.raspinloop.web.riamodelstorageservice.db.InstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(ServiceTestConfiguration.class)
public class InstanceStorageServiceTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private InstanceStorageService service;
	
	@Autowired
	private InstanceRepository instanceRepo;

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
		Instance inst = new Instance();
		inst.setComponentRef(orGate);
		inst.setInstanceName("test Instance");
		inst.setParameters("Custom instance param");
		Long id = entityManager.persist(inst).getId();
		entityManager.flush();

		// when
		Instance found = service.getInstance(id);

		// then
		assertThat(found.getInstanceName()).isEqualTo("test Instance");
		assertThat(found.getComponentRef().getSvgContent()).isEqualTo("<svg>content</svg>");
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
		entityManager.persist(orGate);
		
		Instance inst = new Instance();
		inst.setComponentRef(orGate);
		inst.setInstanceName("test Instance");
		inst.setParameters("Custom instance param");
		Long id = entityManager.persist(inst).getId();
		entityManager.flush();
	
		// when
		service.deleteInstance(id);
		
		// then
		assertFalse(instanceRepo.findById(id).isPresent());	
		
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
		entityManager.persist(orGate);
		
		Instance inst = new Instance();
		inst.setComponentRef(orGate);
		inst.setInstanceName("test Instance");
		inst.setParameters("Custom instance param");
		Long id = entityManager.persist(inst).getId();
		entityManager.flush();
		
		// when
		service.updateInstance(id, "new name",  "Modelica.Electrical.Digital.Gates.OrGate", "updated params");
		
		// then

		Optional<Instance> updatedOpt = instanceRepo.findById(id);
		assertTrue(updatedOpt.isPresent());
		assertThat(updatedOpt.get().getParameters()).isEqualTo("updated params");
	}

	@Test
	public void createTest() {
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
		
		
		// when
		Long id = service.createInstance("Modelica.Electrical.Digital.Gates.OrGate", "new Instance");
		
		// then
		Instance found = service.getInstance(id);
		assertThat(found.getComponentRef().getSvgContent()).isEqualTo("<svg>content</svg>");
		assertThat(found.getInstanceName()).isEqualTo("new Instance");
	}
}
