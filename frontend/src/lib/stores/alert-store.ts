import { writable } from 'svelte/store';

type AlertType = 'success' | 'error' | 'info';

interface Alert {
    id: number; // unique id for each alert
    title: string;
    description: string | null;
    type: AlertType;
    visible: boolean;
}

const alerts = writable<Alert[]>([]);

let nextId = 1;

export function triggerAlert(
    title: string,
    description: string | null = null,
    type: AlertType = 'info',
    duration = 5000
) {
    const id = nextId++;
    const newAlert: Alert = {
        id,
        title,
        description,
        type,
        visible: true,
    };

    alerts.update((all) => [...all, newAlert]);

    setTimeout(() => {
        // hide then remove alert after duration
        alerts.update((all) => all.filter((a) => a.id !== id));
    }, duration);
}

export default alerts;
