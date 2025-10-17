import type { JwtToken } from "@/types/dtos/jwt";

export function isJwtExpired(token: string): boolean {
  try {
    const payload = getJwtPayload(token);
    const now = Math.floor(Date.now() / 1000);
    return payload && payload.exp ? payload.exp < now : false;
  } catch {
    // If parsing fails, treat token as expired
    return true;
  }
}

export function getJwtPayload(token: string): JwtToken | null {
  if (!token || typeof token !== "string") return null;

  const parts = token.split(".");
  if (parts.length !== 3) return null;

  const payloadBase64 = parts[1];
  try {
    const payloadJson = atob(payloadBase64.replace(/-/g, "+").replace(/_/g, "/"));
    const payload = JSON.parse(payloadJson);
    return payload as JwtToken;
  } catch {
    return null;
  }
}