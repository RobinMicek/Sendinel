package cz.promtply.api.controller.tracking;

import cz.promtply.api.config.Constants;
import cz.promtply.api.controller.TrackingControllerBase;
import cz.promtply.api.service.EmailService;
import cz.promtply.api.service.EmailStatusService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping(Constants.TRACKING_API_ROUTE_PREFIX + "/email")
@RequiredArgsConstructor
public class EmailTrackingController extends TrackingControllerBase {

    @Value("${app.tracking.track-opened-emails}")
    private boolean trackOpenedEmails;

    private final EmailService emailService;

    // Base64-encoded 1x1 transparent GIF
    private static final byte[] PIXEL_GIF = Base64.getDecoder().decode(
            "R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw=="
    );

    @GetMapping(value = "/open", produces = MediaType.IMAGE_GIF_VALUE)
    public void trackEmailOpen(@RequestParam("trackCode") String trackCode, HttpServletResponse response) throws IOException {
        if (!trackOpenedEmails) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            emailService.trackEmailOpened(trackCode);
        } catch (Exception ignored) {} // Ignore if fails

        response.setContentType(MediaType.IMAGE_GIF_VALUE);
        response.setContentLength(PIXEL_GIF.length);
        response.getOutputStream().write(PIXEL_GIF);
    }

}
