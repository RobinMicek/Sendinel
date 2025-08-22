// UserRolesEnum.ts
import { UserPermissionsEnum } from "./user-permissions-enum";

export enum UserRolesEnum {
  NON_TOTP = "NON_TOTP",
  USER = "USER",
  ADMIN = "ADMIN",
  CLIENT = "CLIENT"
}

type RoleConfig = {
  directPermissions: UserPermissionsEnum[];
  inheritedRoles?: UserRolesEnum[];
};

const roleConfigs: Record<UserRolesEnum, RoleConfig> = {
  [UserRolesEnum.NON_TOTP]: {
    directPermissions: [
      UserPermissionsEnum.AUTH_TOTP_READ,
      UserPermissionsEnum.AUTH_TOTP_CREATE
      // intentionally no AUTH_TOTP_DELETE
    ]
  },

  [UserRolesEnum.USER]: {
    directPermissions: [
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

      UserPermissionsEnum.EMAILS_READ
    ],
    inheritedRoles: [UserRolesEnum.NON_TOTP]
  },

  [UserRolesEnum.ADMIN]: {
    directPermissions: [
      UserPermissionsEnum.APP_SETTINGS_UPDATE,

      UserPermissionsEnum.USERS_CREATE,
      UserPermissionsEnum.USERS_UPDATE,
      UserPermissionsEnum.USERS_DELETE,

      UserPermissionsEnum.CLIENTS_CREATE,
      UserPermissionsEnum.CLIENTS_UPDATE,
      UserPermissionsEnum.CLIENTS_DELETE,

      UserPermissionsEnum.CLIENT_TOKENS_CREATE,
      UserPermissionsEnum.CLIENT_TOKENS_DELETE
    ],
    inheritedRoles: [UserRolesEnum.USER]
  },

  [UserRolesEnum.CLIENT]: {
    directPermissions: [UserPermissionsEnum.EMAILS_CREATE]
  }
};

// Collect all permissions, including inherited
export function getAllPermissions(role: UserRolesEnum): Set<UserPermissionsEnum> {
  const visited = new Set<UserRolesEnum>();
  const permissions = new Set<UserPermissionsEnum>();

  function collect(r: UserRolesEnum) {
    if (visited.has(r)) return;
    visited.add(r);

    const config = roleConfigs[r];
    config.directPermissions.forEach(p => permissions.add(p));

    config.inheritedRoles?.forEach(inherited => collect(inherited));
  }

  collect(role);
  return permissions;
}

// Utility: Check if a role has a specific permission
export function hasPermission(
  role: UserRolesEnum,
  permission: UserPermissionsEnum
): boolean {
  return getAllPermissions(role).has(permission);
}

// Utility: Check if a role has ALL of a set of permissions
export function hasAllPermissions(
  role: UserRolesEnum,
  permissions: UserPermissionsEnum[]
): boolean {
  const rolePermissions = getAllPermissions(role);
  return permissions.every(p => rolePermissions.has(p));
}

// Utility: Check if a role has ANY of a set of permissions
export function hasAnyPermission(
  role: UserRolesEnum,
  permissions: UserPermissionsEnum[]
): boolean {
  const rolePermissions = getAllPermissions(role);
  return permissions.some(p => rolePermissions.has(p));
}
