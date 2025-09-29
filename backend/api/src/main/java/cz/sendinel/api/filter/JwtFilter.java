package cz.sendinel.api.filter;

import cz.sendinel.api.exceptions.ResourceNotFoundException;
import cz.sendinel.api.service.UserService;
import cz.sendinel.api.util.GrantedAuthorityUtil;
import cz.sendinel.api.util.JwtUtil;
import cz.sendinel.shared.enums.UserRolesEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            try {

                Claims claims = jwtUtil.parseToken(token);
                Boolean totp = claims.get("totp", Boolean.class);

                UserRolesEnum role = userService.getUserById(UUID.fromString(claims.getSubject())).orElseThrow(
                    () -> new ResourceNotFoundException("User not fount with id " + claims.getSubject())
                ).getRole();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        GrantedAuthorityUtil.getAllAuthorities(totp ? role : UserRolesEnum.NON_TOTP)
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JwtException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
