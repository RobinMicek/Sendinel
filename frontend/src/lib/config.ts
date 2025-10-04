export const APP_NAME = "Sendinel"
export const APP_VERSION_NUMBER = "0.0.1-SNAPSHOT"

export const DEFAULT_DATATABLE_PAGE_SIZE = 10

// Without trailing slash
const BACKEND_API = window.RUNTIME_CONFIG?.["API_BASE_URL"] ?? "http://localhost:5000"
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
    subject: "Order Confirmation [#{{order.id}}]",
    htmlRaw: `<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" />
  <title>Order Confirmation</title>
  <style>
    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
    .container { width: 100%; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; }
    .header { text-align: center; padding-bottom: 20px; border-bottom: 1px solid #ddd; }
    .content { padding: 20px 0; }
    .item-table { width: 100%; border-collapse: collapse; }
    .item-table th, .item-table td { text-align: left; padding: 8px; border-bottom: 1px solid #eee; }
    .summary-table { width: 100%; max-width: 300px; margin-left: auto; margin-top: 20px; }
    .summary-table td { padding: 5px; }
    .footer { text-align: center; font-size: 12px; color: #777; padding-top: 20px; border-top: 1px solid #ddd; }
  </style>
</head>
<body>
  <div class="container">
    <div class="header">
      <h2>Thank You for Your Order!</h2>
    </div>
    <div class="content">
      <p>Hi {{customer.firstName}},</p>
      <p>We've received your order and are getting it ready for you. Here are the details:</p>
      
      <h3>Order #{{order.id}} ({{order.date}})</h3>

      <table class="item-table">
        <thead>
          <tr>
            <th>Product</th>
            <th>Quantity</th>
            <th>Price</th>
          </tr>
        </thead>
        <tbody>
          {{#each order.items}}
          <tr>
            <td>
              <strong>{{this.name}}</strong>
              {{#if this.sku}}<br/><small>SKU: {{this.sku}}</small>{{/if}}
            </td>
            <td>{{this.quantity}}</td>
            <td>{{this.price}}</td>
          </tr>
          {{/each}}
        </tbody>
      </table>

      <table class="summary-table">
        <tr>
          <td>Subtotal:</td>
          <td style="text-align: right;">{{order.summary.subtotal}}</td>
        </tr>
        <tr>
          <td>Shipping:</td>
          <td style="text-align: right;">{{order.summary.shipping}}</td>
        </tr>
        {{#if order.summary.tax}}
        <tr>
          <td>Tax:</td>
          <td style="text-align: right;">{{order.summary.tax}}</td>
        </tr>
        {{/if}}
        <tr>
          <td><strong>Total:</strong></td>
          <td style="text-align: right;"><strong>{{order.summary.total}}</strong></td>
        </tr>
      </table>

      <hr style="border: 0; border-top: 1px solid #eee; margin: 20px 0;"/>

      <h4>Shipping Address</h4>
      <p>
        {{order.shippingAddress.street}}<br/>
        {{order.shippingAddress.city}}, {{order.shippingAddress.state}} {{order.shippingAddress.zip}}<br/>
        {{order.shippingAddress.country}}
      </p>

      <h4>Payment Method</h4>
      <p>{{order.paymentMethod}}</p>
    </div>
    <div class="footer">
      <p>If you have any questions, visit our support page: <a href="{{company.supportUrl}}">Contact Us</a></p>
      <p>© {{company.name}}</p>
    </div>
  </div>
</body>
</html>`,
    textRaw: `Thank You for Your Order!

Hi {{customer.firstName}},

We've received your order and are getting it ready for you. Here are the details:

===================================
Order Confirmation
===================================

Order Number: {{order.id}}
Order Date: {{order.date}}

--- Items ---
{{#each order.items}}
- {{this.name}}
  Quantity: {{this.quantity}}
  Price: {{this.price}}
{{#if this.sku}}  SKU: {{this.sku}}{{/if}}

{{/each}}
--- Summary ---
Subtotal: {{order.summary.subtotal}}
Shipping: {{order.summary.shipping}}
{{#if order.summary.tax}}Tax:      {{order.summary.tax}}{{/if}}
Total:    {{order.summary.total}}

===================================

--- Shipping Address ---
{{order.shippingAddress.street}}
{{order.shippingAddress.city}}, {{order.shippingAddress.state}} {{order.shippingAddress.zip}}
{{order.shippingAddress.country}}

--- Payment Method ---
{{order.paymentMethod}}

If you have any questions, please visit our support page at {{company.supportUrl}}

Thank you,
The {{company.name}} Team`,
    schema: {"type": "object", "title": "Order Confirmation", "$schema": "http://json-schema.org/draft-07/schema#", "required": ["customer", "order", "company"], "properties": {"order": {"type": "object", "required": ["id", "date", "shippingAddress", "items", "summary"], "properties": {"id": {"type": "string"}, "date": {"type": "string", "format": "date"}, "items": {"type": "array", "items": {"type": "object", "required": ["name", "quantity", "price"], "properties": {"sku": {"type": "string"}, "name": {"type": "string"}, "price": {"type": "number"}, "imageUrl": {"type": "string", "format": "uri"}, "quantity": {"type": "integer"}}}}, "summary": {"type": "object", "required": ["subtotal", "total"], "properties": {"tax": {"type": "number"}, "total": {"type": "number"}, "shipping": {"type": "number"}, "subtotal": {"type": "number"}}}, "paymentMethod": {"type": "string"}, "shippingAddress": {"type": "object", "required": ["street", "city", "zip", "country"], "properties": {"zip": {"type": "string"}, "city": {"type": "string"}, "state": {"type": "string"}, "street": {"type": "string"}, "country": {"type": "string"}}}}}, "company": {"type": "object", "required": ["name"], "properties": {"name": {"type": "string"}, "supportUrl": {"type": "string", "format": "uri"}}}, "customer": {"type": "object", "required": ["firstName"], "properties": {"lastName": {"type": "string"}, "firstName": {"type": "string"}}}}, "description": "Schema for order confirmation email data"}
}