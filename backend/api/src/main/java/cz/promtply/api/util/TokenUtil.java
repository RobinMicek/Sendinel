package cz.promtply.api.util;

import cz.promtply.shared.config.Constants;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Component
public class TokenUtil {

    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 256-bit token
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String combineToken(UUID tokenId, String secret) {
        return tokenId.toString() + Constants.CLIENT_TOKEN_ID_DELIMITER + secret;
    }

    public static UUID extractTokenId(String fullToken) {
        return UUID.fromString(fullToken.split(Constants.CLIENT_TOKEN_ID_DELIMITER)[0]);
    }

    public static String extractSecret(String fullToken) {
        return fullToken.split(Constants.CLIENT_TOKEN_ID_DELIMITER)[1];
    }

}
