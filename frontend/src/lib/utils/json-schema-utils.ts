export function generateExampleFromSchema(schema: any): any {
  if (!schema) return null;

  // If schema is a JSON string, parse it
  if (typeof schema === 'string') {
    try {
      schema = JSON.parse(schema);
    } catch (e) {
      // Not valid JSON string, continue
    }
  }

  // Use schema.example if provided
  if (schema.example !== undefined) {
    return schema.example;
  }

  const type = schema.type;

  switch (type) {
    case 'object': {
      const obj: any = {};
      if (schema.properties) {
        for (const [key, propSchema] of Object.entries(schema.properties)) {
          obj[key] = generateExampleFromSchema(propSchema);
        }
      }
      return obj;
    }

    case 'array': {
      if (schema.items) {
        return [generateExampleFromSchema(schema.items)];
      }
      return [];
    }

    case 'string': {
      // Handle common string formats
      if (schema.format) {
        switch (schema.format) {
          case 'date-time':
            return new Date().toISOString();
          case 'date':
            return new Date().toISOString().split('T')[0];
          case 'uri':
          case 'url':
            return 'https://example.com';
          case 'email':
            return 'user@example.com';
          case 'uuid':
            return '123e4567-e89b-12d3-a456-426614174000';
          default:
            return 'string';
        }
      }
      return 'string';
    }

    case 'number':
    case 'integer':
      return 0;

    case 'boolean':
      return false;

    case 'null':
      return null;

    default:
      return null;
  }
}
