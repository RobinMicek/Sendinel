package cz.sendinel.shared.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum UserRolesEnum {
    // When has jwt with isTotp=false - can only access totp routes
    NON_TOTP(
            List.of(
                    UserPermissionsEnum.AUTH_TOTP_READ,
                    UserPermissionsEnum.AUTH_TOTP_CREATE
                    // Do not add UserPermissionsEnum.AUTH_TOTP_DELETE,
                    // user should be not able to delete TOTP if not verified first
            )
    ),

    USER(
            List.of(
                UserPermissionsEnum.AUTH_TOTP_DELETE,

                UserPermissionsEnum.APP_SETTINGS_READ,

                UserPermissionsEnum.USERS_READ,

                UserPermissionsEnum.SENDERS_READ,
                UserPermissionsEnum.SENDERS_CREATE,
                UserPermissionsEnum.SENDERS_UPDATE,
                UserPermissionsEnum.SENDERS_DELETE,

                UserPermissionsEnum.CLIENTS_READ,

                UserPermissionsEnum.CLIENT_TOKENS_READ,

                UserPermissionsEnum.TEMPLATES_READ,
                UserPermissionsEnum.TEMPLATES_CREATE,
                UserPermissionsEnum.TEMPLATES_UPDATE,
                UserPermissionsEnum.TEMPLATES_DELETE,

                UserPermissionsEnum.EMAILS_READ,

                UserPermissionsEnum.STATS_READ
            ),
            List.of(NON_TOTP)
    ),

    ADMIN(
            List.of(
                    UserPermissionsEnum.APP_SETTINGS_UPDATE,

                    UserPermissionsEnum.USERS_CREATE,
                    UserPermissionsEnum.USERS_UPDATE,
                    UserPermissionsEnum.USERS_DELETE,

                    UserPermissionsEnum.CLIENTS_CREATE,
                    UserPermissionsEnum.CLIENTS_UPDATE,
                    UserPermissionsEnum.CLIENTS_DELETE,

                    UserPermissionsEnum.CLIENT_TOKENS_CREATE,
                    UserPermissionsEnum.CLIENT_TOKENS_DELETE
            ),
            List.of(USER)
    ),

    // For users authenticated via the client token - can only access routes for sending emails
    CLIENT(
            List.of(
                    UserPermissionsEnum.EMAILS_CREATE
            )
    );

    private final List<UserPermissionsEnum> directPermissions;
    private final List<UserRolesEnum> inheritedRoles;

    UserRolesEnum(List<UserPermissionsEnum> directPermissions) {
        this(directPermissions, List.of());
    }

    public Set<UserPermissionsEnum> getAllPermissions() {
        Set<UserPermissionsEnum> all = new HashSet<>(directPermissions);
        for (UserRolesEnum inherited : inheritedRoles) {
            all.addAll(inherited.getAllPermissions());
        }
        return all;
    }
}
