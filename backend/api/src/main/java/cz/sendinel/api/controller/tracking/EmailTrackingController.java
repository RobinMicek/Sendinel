package cz.sendinel.api.controller.tracking;

import cz.sendinel.api.controller.TrackingControllerBase;
import cz.sendinel.api.service.AppSettingsService;
import cz.sendinel.api.service.EmailService;
import cz.sendinel.api.util.HttpRequestUtil;
import cz.sendinel.shared.config.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping(Constants.TRACKING_API_ROUTE_PREFIX + "/email")
@RequiredArgsConstructor
public class EmailTrackingController extends TrackingControllerBase {

    private final EmailService emailService;
    private final AppSettingsService appSettingsService;

    // Base64-encoded 1x1 transparent PNG
    private static final byte[] PIXEL_PNG = Base64.getDecoder().decode(
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR4nGNgYAAAAAMAASsJTYQAAAAASUVORK5CYII="
    );


    @GetMapping(value = "/open", produces = MediaType.IMAGE_PNG_VALUE)
    public void trackEmailOpen(@RequestParam("trackCode") String trackCode, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!appSettingsService.getAppSettings().isTrackOpenedEmails()) {
            return;
        }

        try {
            String clientIp = HttpRequestUtil.getClientIp(request);
            String clientUserAgent = request.getHeader("User-Agent");
            String clientLanguage = request.getHeader("Accept-Language");
            String clientOrigin = request.getHeader("Origin");

            String note = String.format("IP: %s | User Agent: %s | Language: %s | Origin: %s",  clientIp, clientUserAgent, clientLanguage, clientOrigin);

            emailService.trackEmailOpened(trackCode, note);
        } catch (Exception ignored) {} // Ignore if fails

        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.setContentLength(PIXEL_PNG.length);
        response.getOutputStream().write(PIXEL_PNG);
    }


}
