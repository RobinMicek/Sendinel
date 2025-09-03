export enum SenderTypesEnum {
    SMTP = "SMTP"
}

type SenderTypeMeta = {
    displayName: string;
};

export const senderTypesMeta: Record<SenderTypesEnum, SenderTypeMeta> = {
    [SenderTypesEnum.SMTP]: {
        displayName: "Simple Mail Transfer Protocol"
    }
};