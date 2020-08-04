package com.icedq.versioncontrol.gitFlow;

import java.util.HashMap;
import java.util.Map;

public class RuleMetadata {

	private Map<String, RuleVersionData> ruleMetaData;

	public RuleMetadata() {
		ruleMetaData = new HashMap<String, RuleVersionData>();
	}

	public Map<String, RuleVersionData> getRuleMetaData() {
		return ruleMetaData;
	}

	public void setRuleMetaData(Map<String, RuleVersionData> ruleMetaData) {
		this.ruleMetaData = ruleMetaData;
	}

	public boolean isEmpty() {
		return ruleMetaData.isEmpty();
	}
	
	@Override
	public String toString() {
		return "RuleMetadata [ruleMetaData=" + ruleMetaData + "]";
	}

	public static class RuleVersionData {

		private String version;
		private String objectId;
		private String fileName;

		public RuleVersionData() {

		}

		public RuleVersionData(String version, String objectId, String fileName) {
			this.version = version;
			this.objectId = objectId;
			this.fileName = fileName;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getObjectId() {
			return objectId;
		}

		public void setObjectId(String objectId) {
			this.objectId = objectId;
		}
		
		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		@Override
		public String toString() {
			return "RuleVersionData [version=" + version + ", objectId=" + objectId + ", fileName=" + fileName + "]";
		}				
	}
	
}
