package org.raspinloop.web.riamodelstorageservice.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Port {

	private @Id @GeneratedValue Long id;
	String portId;
	int x;
	int y;
	String description;

	public Port() {
	}

}
