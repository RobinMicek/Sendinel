package cz.sendinel.api.filter;

import cz.sendinel.api.entity.Client;
import cz.sendinel.api.service.ClientTokenService;
import cz.sendinel.api.util.GrantedAuthorityUtil;
import cz.sendinel.shared.enums.UserRolesEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final ClientTokenService clientTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader("X-API-KEY");

        if (apiKey == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "API Key is missing");
            return;
        }

        Client client = clientTokenService.getClientByToken(apiKey).orElse(null);
        if (client == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Client not found");
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(
                client.getId().toString(),
                null,
                GrantedAuthorityUtil.getAllAuthorities(UserRolesEnum.CLIENT.name()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
