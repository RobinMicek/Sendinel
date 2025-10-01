import { m } from "@/paraglide/messages";

export enum SenderTypesEnum {
    SMTP = "SMTP",
}

export type SenderConfigurationFieldType = string | number | boolean | undefined;

// Required fields need to have default value, otherwise the backend will reject them
type SenderConfigurationField<T extends SenderConfigurationFieldType> = {
    translation: string
    required: boolean;
    defaultValue: T
    type: T;
    isSensitive: boolean;
};

type SenderTypeMeta = {
    displayName: string;
    configuration: Record<string, SenderConfigurationField<SenderConfigurationFieldType>>;
};

export const senderTypesMeta: Record<SenderTypesEnum, SenderTypeMeta> = {
    [SenderTypesEnum.SMTP]: {
        displayName: "Simple Mail Transfer Protocol",
        configuration: {
            smtpHost: { translation: m.smtp_host(), required: true, type: "" as string, defaultValue: "smtp.example.com", isSensitive: false },
            smtpPort: { translation: m.smtp_port(), required: true, type: 0 as number, defaultValue: 465, isSensitive: false },
            username: { translation: m.username(), required: false, type: "" as string, defaultValue: "", isSensitive: false },
            password: { translation: m.password(), required: false, type: "" as string, defaultValue: "", isSensitive: true },
            fromAddress: { translation: m.from_address(), required: true, type: "" as string, defaultValue: "no-reply@acme.com", isSensitive: false },
            startTls: { translation: m.start_tls(), required: true, type: false as boolean, defaultValue: false, isSensitive: false },
            ssl: { translation: m.ssl(), required: true, type: false as boolean, defaultValue: false, isSensitive: false },
            timeout: { translation: m.timeout_ms(), required: false, type: 0 as number, defaultValue: 5000, isSensitive: false },
        },
    },
};

export function createEmptySenderConfiguration(schema: Record<string, SenderConfigurationField<SenderConfigurationFieldType>>): Record<string, SenderConfigurationFieldType> {
    return Object.fromEntries(
        Object.entries(schema).map(([key, field]) => {            
            return [key, field.defaultValue];
        })
    );
}

export function isSenderConfigurationComplete(schema: Record<string, SenderConfigurationField<SenderConfigurationFieldType>>, config: Partial<Record<string, SenderConfigurationFieldType>>): boolean {
    return Object.entries(schema).every(([key, field]) => {       
        const value = config[key];
        return value !== undefined;
    });
}

// Used to migrate older sender configuration to current schema, otherwise you may not be able to update it (missing values)
export function mergeSenderConfiguration(schema: Record<string, SenderConfigurationField<SenderConfigurationFieldType>>, existingConfig: Partial<Record<string, SenderConfigurationFieldType>>): Record<string, SenderConfigurationFieldType> {
    return Object.fromEntries(
        Object.entries(schema).map(([key, field]) => {
            // Use existing value if present (including falsy ones like "")
            // but if it's undefined, fall back to default
            const value = existingConfig[key] !== undefined ? existingConfig[key] : field.defaultValue;
            return [key, value];
        })
    );
}
