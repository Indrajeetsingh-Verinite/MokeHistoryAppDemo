package com.verinite.interestapp.utils;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;

import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion.VersionFlag;
import com.networknt.schema.ValidationMessage;
import com.verinite.interestapp.exception.ErrorCodes;
import com.verinite.interestapp.exception.InterestAppException;


@Component
public class JsonSchemaValidator {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public void validate(String jsonString) throws IOException {
		JsonNode jsonNode;

		File schemaFile = new ClassPathResource("JsonValidator.json").getFile();
		try (InputStream inputStream = new FileInputStream(schemaFile)) {
			JsonSchema jsonSchema;
			jsonNode = objectMapper.readTree(jsonString);
			jsonSchema = JsonSchemaFactory.getInstance(VersionFlag.V4).getSchema(inputStream);
			Set<ValidationMessage> validationResult = jsonSchema.validate(jsonNode);
			if (!validationResult.isEmpty()) {
				String errorMessages = validationResult.stream().map(ValidationMessage::getMessage)
						.collect(joining(" : "));
				throw new InterestAppException(ErrorCodes.JSON_SCHEMA_FAILED, errorMessages);
			}
		}
	}

}