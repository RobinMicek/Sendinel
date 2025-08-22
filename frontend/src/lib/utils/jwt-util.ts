export function isJwtExpired(token: string): boolean {
  try {
    // JWT format: header.payload.signature
    const payloadBase64 = token.split('.')[1];
    if (!payloadBase64) return true;

    // Decode Base64 (URL-safe)
    const payloadJson = atob(payloadBase64.replace(/-/g, '+').replace(/_/g, '/'));
    const payload = JSON.parse(payloadJson);

    if (!payload.exp) return true; // No expiration = treat as expired

    const now = Math.floor(Date.now() / 1000);
    return payload.exp < now;
  } catch (err) {
    console.error('Invalid JWT', err);
    return true; // Treat invalid tokens as expired
  }
}