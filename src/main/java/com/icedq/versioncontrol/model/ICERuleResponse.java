package com.icedq.versioncontrol.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ICERuleResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlElement private ICERule rule;
	@XmlElement private ICERuleClass rclass;
	
	public ICERuleResponse() {
	}
	
	public ICERuleResponse(ICERule rule, ICERuleClass rclass) {
		this.rule = rule;
		this.rclass = rclass;
	}
	public ICERule getRule() {
		return rule;
	}
	public void setRule(ICERule rule) {
		this.rule = rule;
	}
	public ICERuleClass getRClass() {
		return rclass;
	}
	public void setRClass(ICERuleClass rclass) {
		this.rclass = rclass;
	}

	@Override
	public String toString() {
		return "ICERuleResponse [rule=" + rule.toString() + ", rclass=" + rclass.toString() + "]";
	}	
	
}
