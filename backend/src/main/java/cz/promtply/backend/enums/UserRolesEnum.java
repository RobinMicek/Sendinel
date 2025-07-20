package cz.promtply.backend.enums;

public enum UserRolesEnum {
    NON_TOTP, // When has jwt with isTotp=false - can only access totp routes

    USER,
    ADMIN,

    CLIENT; // For users authenticated via the client token - can only access routes for sending emails
}
