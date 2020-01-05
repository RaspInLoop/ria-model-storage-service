package org.raspinloop.web.riamodelstorageservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.ComponentRepository;
import org.raspinloop.web.riamodelstorageservice.db.Port;
import org.raspinloop.web.riamodelstorageservice.db.PortGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

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
	public void findWithChildTest() {
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
		
		
		
		Port p1 = persitPort(1);
		Port p2 = persitPort(2);
		Port p3 = persitPort(3);
		Port p4 = persitPort(4);
		
		PortGroup pg1 =new PortGroup();
		pg1.setName("PG1");
		pg1.setSvg("<svg>pg1</svg>");
		pg1.setPorts(new HashSet<Port>(Arrays.asList(p1,p2)));
		entityManager.persist(pg1);
		
		PortGroup pg2 =new PortGroup();
		pg2.setName("PG2");
		pg2.setSvg("<svg>pg2</svg>");
		pg2.setPorts(new HashSet<Port>(Arrays.asList(p3,p4)));
		entityManager.persist(pg2);
					
		orGate.setPortGroups(new HashSet<PortGroup>(Arrays.asList(pg1,pg2)));
		
		entityManager.persist(orGate);
		entityManager.flush();

		// when
		Component found = service.getComponent("Modelica.Electrical.Digital.Gates.OrGate");

		// then
		assertThat(found.getSvgContent()).isEqualTo("<svg>content</svg>");
		assertThat(found.getPortGroups().size()).isEqualTo(2);
		assertThat(found.getPortGroups()).extracting(PortGroup::getName).containsExactlyInAnyOrder("PG1", "PG2");		
		assertThat(found.getPortGroups()).extracting(PortGroup::getPorts).extracting(Set<Port>::size).containsExactly(2,2);

	}

	private Port persitPort(int i) {
		Port p = new Port();
		p.setPortId("p"+i);
		p.setX(1000+i);
		p.setY(2000+i);
		p.setDescription("Port ");
		p = entityManager.persist(p);
		return p;
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
		Set<PortGroup> portGroups =new HashSet<>();
		PortGroup pg1 =new PortGroup();
		pg1.setName("PG1");
		pg1.setSvg("<svg>pg1</svg>");
		Set<Port> portsPG1 = new HashSet<>();
		Port p1 = new Port();
		p1.setPortId("pg1.p1");
		p1.setX(1001);
		p1.setY(2001);
		p1.setDescription("pg1, Port 1");
		portsPG1.add(p1);
		Port p2 = new Port();
		p2.setPortId("pg1.p2");
		p2.setX(1002);
		p2.setY(2002);
		p2.setDescription("pg1, Port 2");
		portsPG1.add(p2);
		pg1.setPorts(portsPG1);
		portGroups.add(pg1);
		PortGroup pg2 =new PortGroup();
		pg2.setName("PG2");
		pg2.setSvg("<svg>pg2</svg>");
		Set<Port> portsPG2 = new HashSet<>();
		Port p3 = new Port();
		p3.setPortId("pg2.p3");
		p3.setX(1003);
		p3.setY(2003);
		p3.setDescription("pg2, Port 3");
		portsPG2.add(p3);
		Port p4 = new Port();
		p4.setPortId("pg2.p4");
		p4.setX(1004);
		p4.setY(2004);
		p4.setDescription("pg2, Port 4");
		portsPG2.add(p4);
		pg2.setPorts(portsPG2);
		portGroups.add(pg2);
		newOrGate.setPortGroups(portGroups);
		// when
		service.updateComponent(newOrGate);
		
		// then
		found = service.getComponent("Modelica.Electrical.Digital.Gates.OrGate");
		assertThat(found.getDescription()).isEqualTo("shorter desc");
		assertThat(found.getPortGroups().size()).isEqualTo(2);
		assertThat(found.getPortGroups()).extracting(PortGroup::getName).containsExactlyInAnyOrder("PG1", "PG2");
		
		assertThat(found.getPortGroups()).extracting(PortGroup::getPorts).extracting(Set<Port>::size).containsExactly(2,2);
		}
}
