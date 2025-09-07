<script lang="ts">
	import type { JsonSchemaNode } from "@/types/json-schema-node";
	import JsonSchemaNodeView from "./json-schema-node-view.svelte";

	export let schema: any = {}
	export let readonly: boolean = false
    
	let jsonNodeSchema: JsonSchemaNode = schemaToJsonNode(schema)

	export function schemaToJsonNode(schema: any, name: string = "root", required: boolean = true): JsonSchemaNode {
		if (!schema || typeof schema !== "object") {
			throw new Error("Invalid schema");
		}

		if (schema.type === "object") {
			const props = schema.properties || {};
			const requiredFields: string[] = schema.required || [];

			return {
				name,
				type: "object",
				required,
				children: Object.entries(props).map(([key, value]: [string, any]) =>
					schemaToJsonNode(value, key, requiredFields.includes(key))
				)
			};
		}

		if (schema.type === "array") {
			return {
				name,
				type: "list",
				required,
				children: schema.items
					? [schemaToJsonNode(schema.items, "items", true)]
					: []
			};
		}

        if (schema.type === "integer") {
			return {
				name,
				type: "number",
				required
			};
		}
		
        if (["string", "number", "boolean"].includes(schema.type)) {
			return {
				name,
				type: schema.type as "string" | "number" | "boolean",
				required
			};
		}

		throw new Error(`Unsupported schema type: ${schema.type}`);
	}

	export function jsonNodeToSchema(node: JsonSchemaNode, isRoot = true): any {
        switch (node.type) {
            case "object": {
                const properties: Record<string, any> = {};
                const required: string[] = [];

                (node.children || []).forEach((child) => {
                    properties[child.name] = jsonNodeToSchema(child, false);
                    if (child.required) {
                        required.push(child.name);
                    }
                });

                const resultSchema: any = { type: "object", properties };
                if (required.length > 0) {
                    resultSchema.required = required;
                }
                return resultSchema;
            }

            case "list": {
                // A list/array schema only has one items property.
                // This items property should be the schema for the elements of the array.
                const items = node.children && node.children.length > 0
                    ? jsonNodeToSchema(node.children[0], false)
                    : {};
                return { type: "array", items };
            }

            case "string":
            case "number":
            case "boolean":
                return { type: node.type };

            default:
                throw new Error(`Unsupported node type: ${node.type}`);
        }
    }

    $: schema = jsonNodeToSchema(jsonNodeSchema);
</script>

<JsonSchemaNodeView bind:node={jsonNodeSchema} readonly={readonly} />