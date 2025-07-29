package cz.promptly.api.util;

import cz.promptly.shared.enums.UserRolesEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GrantedAuthorityUtil {

    public static List<GrantedAuthority> getAllAuthorities(String userRole) {
        UserRolesEnum role = UserRolesEnum.valueOf(userRole);

        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));

        role.getAllPermissions().forEach(permission ->
                authorities.add(new SimpleGrantedAuthority(permission.name()))
        );

        return authorities;
    }

}
