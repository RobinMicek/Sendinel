package cz.promptly.api.controller.tracking;

import cz.promptly.shared.config.Constants;
import cz.promptly.api.controller.TrackingControllerBase;
import cz.promptly.api.service.EmailService;
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

    // Base64-encoded 1x1 transparent PNG
    private static final byte[] PIXEL_PNG = Base64.getDecoder().decode(
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR4nGNgYAAAAAMAASsJTYQAAAAASUVORK5CYII="
    );


    @GetMapping(value = "/open", produces = MediaType.IMAGE_PNG_VALUE)
    public void trackEmailOpen(@RequestParam("trackCode") String trackCode, HttpServletResponse response) throws IOException {
        if (!trackOpenedEmails) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            emailService.trackEmailOpened(trackCode);
        } catch (Exception ignored) {} // Ignore if fails

        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.setContentLength(PIXEL_PNG.length);
        response.getOutputStream().write(PIXEL_PNG);
    }


}
