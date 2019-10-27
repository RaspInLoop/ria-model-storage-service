package org.raspinloop.web.riamodelstorageservice.db;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.raspinloop.server.model.ILink;

import lombok.Data;

@Data
@Entity
public class Model {

	private @Id @GeneratedValue Long id;
	
	String name;
	String description;
	String authorId;
	String creationDate;
	ArrayList<Component> componentsInstances;
	ArrayList<ILink> links;

	public Model() {
	}
	
}
