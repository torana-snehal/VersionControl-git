package com.icedq.versioncontrol.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ICERuleClass {
	
	private String classId;
	private String prop;
	private String value;
	
	public ICERuleClass() {
		
	}
	
	public ICERuleClass(String classId, String prop, String value) {
		this.classId = classId;
		this.prop = prop;
		this.value = value;
	}

	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	
	public String getProp() {
		return prop;
	}
	public void setProp(String prop) {
		this.prop = prop;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ICERuleClass [classId=" + classId + ", prop=" + prop + ", value=" + value + "]";
	}
}
