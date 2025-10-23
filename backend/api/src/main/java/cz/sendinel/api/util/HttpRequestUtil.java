package cz.sendinel.api.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class HttpRequestUtil {

    public static String  getClientIp(HttpServletRequest request) {
        String header = request.getHeader("X-Forwarded-For");
        if (header != null && !header.isEmpty()) {
            return header.split(",")[0].trim();
        }
        header = request.getHeader("X-Real-IP");
        if (header != null && !header.isEmpty()) {
            return header;
        }
        return request.getRemoteAddr();
    }

}
