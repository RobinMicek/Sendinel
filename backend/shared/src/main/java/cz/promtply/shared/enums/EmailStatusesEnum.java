package cz.promtply.shared.enums;

public enum EmailStatusesEnum {
    CREATED,
    ENQUEUED,

    INVALID_CONFIGURATION,
    UNAUTHORIZED,

    FAILED,
    SENT,

    DEFERRED,
    BOUNCED,
    DELIVERED,

    OPENED;
}
