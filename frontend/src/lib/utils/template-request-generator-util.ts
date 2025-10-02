import { BACKEND_EXTERNAL_API_BASE_URL } from "@/config";
import type { TemplateResponse } from "@/types/dtos/template";
import { generateExampleFromSchema } from "./json-schema-utils";
import { m } from "@/paraglide/messages";

export class TemplateRequestGenerator {
    url: string;
    method: string;
    headers: Record<string, string>;
    body: Record<string, any>;

    constructor(template: TemplateResponse) {
        this.url = BACKEND_EXTERNAL_API_BASE_URL + "/client-api/email";
        this.method = "POST";
        this.headers = {
            "Content-Type": "application/json",
            "X-API-KEY": `<${m.client_token()}>`
        };
        this.body = {
            toAddress: "john.doe@acme.com",
            template: template.id,
            priority: "NORMAL",
            templateVariables: generateExampleFromSchema(template.schema)
        };
    }

    generateCurl(): string {
        let curlCommand = `curl -X ${this.method} \\\n  "${this.url}"`;

        for (const [key, value] of Object.entries(this.headers)) {
            curlCommand += ` \\\n  -H "${key}: ${value}"`;
        }

        if (this.body && ["POST", "PUT", "PATCH"].includes(this.method)) {
            // Pretty-print JSON body without escaping
            const bodyString = JSON.stringify(this.body, null, 2);

            curlCommand += ` \\\n  -d '${bodyString}'`;
        }

        return curlCommand;
    }

 

    generatePython(): string {
        const headersString = JSON.stringify(this.headers, undefined, 2);
        const bodyString = JSON.stringify(this.body, undefined, 2)
            // replace JSON booleans and null with Python equivalents
            .replace(/\btrue\b/g, "True")
            .replace(/\bfalse\b/g, "False")
            .replace(/\bnull\b/g, "None");

        return `
import requests

url = '${this.url}'
headers = ${headersString}
data = ${bodyString}

response = requests.${this.method.toLowerCase()}(url, headers=headers, json=data)
print(response.status_code)
print(response.text)
    `.trim();
}


    generateTypeScript(): string {
        const headersString = JSON.stringify(this.headers, undefined, 2);
        const bodyString = JSON.stringify(this.body, undefined, 2);

        return `
    import fetch from 'node-fetch';

    const url = '${this.url}';
    const headers = ${headersString};
    const data = ${bodyString};

    const response = await fetch(url, {
        method: '${this.method}',
        headers,
        body: JSON.stringify(body)
    });

    const data = await response.json();
    console.log('Status:', response.status);
    console.log('Response:', data);
            `.trim();
    }
}
