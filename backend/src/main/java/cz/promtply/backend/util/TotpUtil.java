package cz.promtply.backend.util;

import com.warrenstrange.googleauth.GoogleAuthenticator;

public class TotpUtil {

    private static final GoogleAuthenticator googleAuth = new GoogleAuthenticator();

    public static boolean verifyTotp(String secret, int code) {
        return googleAuth.authorize(secret, code);
    }
}
