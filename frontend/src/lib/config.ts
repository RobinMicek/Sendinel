export const APP_NAME = "Sendinel"
export const APP_VERSION_NUMBER = "0.0.1-SNAPSHOT"

export const DEFAULT_DATATABLE_PAGE_SIZE = 10

// Without trailing slash
const BACKEND_API = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:5000"
export const BACKEND_INTERNAL_API_BASE_URL =  BACKEND_API + "/internal"
export const BACKEND_EXTERNAL_API_BASE_URL = BACKEND_API + "/external"

export const GITHUB_PROJECT_URL = "https://github.com/RobinMicek/sendinel"
export const GITHUB_LATEST_RELEASE_URL = GITHUB_PROJECT_URL + "/releases/latest"
export const GITHUB_API_LATEST_RELEASE_URL = "https://api.github.com/repos/RobinMicek/sendinel/releases/latest"
export const DOCUMENTATION_URL = "https://chat.openai.com"

export const APP_LATEST_RELEASE_CACHE_EXPIRATION = 24 * 60 * 60 * 1000 // 1 day
export const APP_SETTINGS_CACHE_EXPIRATION = 60 * 60 * 1000 // 1 hour
export const LAST_VISITED_PAGE_CACHE_EXPIRATION = 60 * 60 * 1000 // 1 hour

export const EXPORT_FILE_EXTENSION = ".export"

export const SAMPLE_TEMPLATE = {
    subject: "Acme - Users report [{{meta.createdAt}}]",
    htmlRaw: `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>User Summary Email</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f4f4;
      margin: 0;
      padding: 0;
      color: #333;
    }
    .container {
      max-width: 600px;
      margin: 20px auto;
      background: #ffffff;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0 2px 5px rgba(0,0,0,0.1);
    }
    h1 {
      color: #2c3e50;
    }
    h2 {
      color: #34495e;
      margin-top: 20px;
    }
    p {
      margin: 5px 0;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 10px;
    }
    th, td {
      text-align: left;
      padding: 8px;
      border-bottom: 1px solid #ddd;
    }
    th {
      background-color: #f9f9f9;
    }
    .footer {
      text-align: center;
      font-size: 12px;
      color: #888;
      margin-top: 20px;
    }
  </style>
</head>
<body>
  <div class="container">
    <h1>User Profile Summary</h1>
    
    {{#each users}}
      <h2>{{username}} ({{id}})</h2>
      <p><strong>Email:</strong> {{email}}</p>
      <p><strong>Status:</strong> {{#if isActive}}Active{{else}}Inactive{{/if}}</p>
      <p><strong>Roles:</strong> {{#each roles}}{{this}}{{#unless @last}}, {{/unless}}{{/each}}</p>

      <h3>Profile Information</h3>
      <p><strong>Name:</strong> {{profile.firstName}} {{profile.lastName}}</p>
      <p><strong>Birth Date:</strong> {{profile.birthDate}}</p>

      <h4>Address</h4>
      <p>{{profile.address.street}}, {{profile.address.city}}, {{profile.address.state}}, {{profile.address.postalCode}}, {{profile.address.country}}</p>

      <h4>Phone Numbers</h4>
      <ul>
        {{#each profile.phoneNumbers}}
          <li>{{type}}: {{number}}</li>
        {{/each}}
      </ul>

      <h3>Recent Activity Logs</h3>
      <table>
        <thead>
          <tr>
            <th>Timestamp</th>
            <th>Action</th>
            <th>IP Address</th>
          </tr>
        </thead>
        <tbody>
          {{#each activityLogs}}
            <tr>
              <td>{{timestamp}}</td>
              <td>{{action}}</td>
              <td>{{ipAddress}}</td>
            </tr>
          {{/each}}
        </tbody>
      </table>

      <hr style="margin: 30px 0;" />
    {{/each}}

    <div class="footer">
      <p>Total Users: {{meta.totalUsers}} | Active Users: {{meta.activeUsers}}</p>
      <p>Report generated on: {{meta.updatedAt}}</p>
    </div>
  </div>
</body>
</html>

`,
    textRaw: `User Profile Summary
====================

{{#each users}}
Username: {{username}} (ID: {{id}})
Email: {{email}}
Status: {{#if isActive}}Active{{else}}Inactive{{/if}}
Roles: {{#each roles}}{{this}}{{#unless @last}}, {{/unless}}{{/each}}

Profile Information
-------------------
Name: {{profile.firstName}} {{profile.lastName}}
Birth Date: {{profile.birthDate}}

Address:
{{profile.address.street}}
{{profile.address.city}}, {{profile.address.state}}, {{profile.address.postalCode}}
{{profile.address.country}}

Phone Numbers:
{{#each profile.phoneNumbers}}
- {{type}}: {{number}}
{{/each}}

Recent Activity Logs:
--------------------
{{#each activityLogs}}
- Timestamp: {{timestamp}}
  Action: {{action}}
  IP Address: {{ipAddress}}
{{/each}}

------------------------------------------------------------
{{/each}}

Summary:
--------
Total Users: {{meta.totalUsers}}
Active Users: {{meta.activeUsers}}
Report generated on: {{meta.updatedAt}}
`,
    schema: {"type": "object", "required": ["meta", "users"], "properties": {"meta": {"type": "object", "properties": {"createdAt": {"type": "string"}, "updatedAt": {"type": "string"}, "totalUsers": {"type": "number"}, "activeUsers": {"type": "number"}}}, "users": {"type": "array", "items": {"type": "object", "required": ["id", "email", "roles", "profile", "password", "username"], "properties": {"id": {"type": "string"}, "email": {"type": "string"}, "roles": {"type": "array", "items": {"type": "string"}}, "profile": {"type": "object", "required": ["lastName", "firstName"], "properties": {"address": {"type": "object", "required": ["city", "street", "country"], "properties": {"city": {"type": "string"}, "state": {"type": "string"}, "street": {"type": "string"}, "country": {"type": "string"}, "postalCode": {"type": "string"}}}, "lastName": {"type": "string"}, "birthDate": {"type": "string"}, "firstName": {"type": "string"}, "phoneNumbers": {"type": "array", "items": {"type": "object", "required": ["type", "number"], "properties": {"type": {"type": "string"}, "number": {"type": "string"}}}}}}, "isActive": {"type": "boolean"}, "password": {"type": "string"}, "username": {"type": "string"}, "createdAt": {"type": "string"}, "updatedAt": {"type": "string"}, "activityLogs": {"type": "array", "items": {"type": "object", "required": ["action", "timestamp"], "properties": {"action": {"type": "string"}, "ipAddress": {"type": "string"}, "timestamp": {"type": "string"}}}}}}}}}
}