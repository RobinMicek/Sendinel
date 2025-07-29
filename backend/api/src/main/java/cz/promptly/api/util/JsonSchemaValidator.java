package cz.promptly.api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonSchemaValidator {

    public boolean validate(String jsonSchema, JsonNode object) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Parse schema
            JsonNode jsonSchemaNode = mapper.readTree(jsonSchema);
            JSONObject rawSchema = new JSONObject(mapper.writeValueAsString(jsonSchemaNode));

            // Parse data
            JSONObject rawJson = new JSONObject(mapper.writeValueAsString(object));

            // Validate
            Schema schema = SchemaLoader.load(rawSchema);
            schema.validate(rawJson);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
