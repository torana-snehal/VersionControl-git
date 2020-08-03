package com.icedq.versioncontrol.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ICERule {
	
	private String id;
	private String name;
	private String desc;
	private Date creationDate;
	private Date updatedDate;
	
	private static ICERule instance;
	public static ICERule getInstance() {
		if(instance == null) {
			instance = new ICERule();
		}
		return instance;
	}
	
	public ICERule() {
		
	}
	
	public ICERule(String id, String name, String desc, Date creationDate, Date updatedDate) {
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.creationDate = creationDate;
		this.updatedDate = updatedDate;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public String toString() {
		return "ICERule [id=" + id + ", name=" + name + ", desc=" + desc + ", creationDate=" + creationDate
				+ ", updatedDate=" + updatedDate + "]";
	}
	
}
