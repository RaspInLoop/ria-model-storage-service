package org.raspinloop.web.riamodelstorageservice;

import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.Instance;

public class TestEntity {

	public static Component createOrGate() {
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
		return orGate;
	}

	public static Instance createOrGateInstance(Long withId) {
		Instance orGateInst = new Instance();
		orGateInst.setId(withId);
		orGateInst.setInstanceName("orGate_Inst");
		orGateInst.setParameters("{\"parameters\":[]}");
		orGateInst.setComponentRef(createOrGate());
		return orGateInst;
	}

}
