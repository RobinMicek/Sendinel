package cz.sendinel.api.util;

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

    private final ObjectMapper mapper = new ObjectMapper();

    public boolean validate(JsonNode jsonSchemaNode, JsonNode object) {
        try {
            // Convert schema JsonNode to JSONObject
            JSONObject rawSchema = new JSONObject(mapper.writeValueAsString(jsonSchemaNode));

            // Convert object JsonNode to JSONObject
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
