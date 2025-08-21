export function getLocalStorageItem<T>(key: string, defaultValue: T): T {
    if (typeof localStorage === "undefined") return defaultValue;
    const item = localStorage.getItem(key);
    return item ? JSON.parse(item) : defaultValue;
}

export function setLocalStorageItem<T>(key: string, value: T) {
    if (typeof localStorage === "undefined") return;
    localStorage.setItem(key, JSON.stringify(value));
}

export function removeLocalStorageItem(key: string) {
    if (typeof localStorage === "undefined") return;
    localStorage.removeItem(key);
}