export type JsonSchemaNode = {
    name: string;
    type: "string" | "number" | "boolean" | "list" | "object"
    required: boolean;
    children?: JsonSchemaNode[];
};