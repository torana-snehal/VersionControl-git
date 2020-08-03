package com.icedq.versioncontrol.fileOperations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.icedq.versioncontrol.gitFlow.RuleMetadata;
import com.icedq.versioncontrol.gitFlow.RuleMetadata.RuleVersionData;
import com.icedq.versioncontrol.model.ICERuleResponse;

public class FileOperations {

	private static FileOperations instance;

	public static FileOperations getInstance() {
		if (instance == null) {
			instance = new FileOperations();
		}
		return instance;
	}

	// write
	public void writeToFile(ICERuleResponse res, File file) {
		write(res, file);
	}

	public void writeToMetaDataFile(Map<String, RuleVersionData> data, File file) {
		write(data, file);
	}

	private void write(Object obj, File file) {
		try {

			ObjectMapper mapper = new ObjectMapper();
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			String data = ow.writeValueAsString(obj);
			
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(data);
			fileWriter.flush();
			fileWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
