package cz.promptly.api.controller.internal;

import cz.promptly.api.controller.InternalControllerBase;
import cz.promptly.api.dto.appsettings.AppSettingsRequestDto;
import cz.promptly.api.dto.appsettings.AppSettingsResponseDto;
import cz.promptly.api.entity.AppSettings;
import cz.promptly.api.service.AppSettingsService;
import cz.promptly.api.util.MapperUtil;
import cz.promptly.shared.config.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.INTERNAL_API_ROUTE_PREFIX + "/app-settings")
@RequiredArgsConstructor
public class AppSettingsController extends InternalControllerBase {

    private final AppSettingsService appSettingsService;

    @GetMapping
    @PreAuthorize("hasAuthority('APP_SETTINGS_READ')")
    public ResponseEntity<AppSettingsResponseDto> getAppSettings() {
        return ResponseEntity.ok(MapperUtil.toDto(appSettingsService.getAppSettings(), AppSettingsResponseDto.class));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('APP_SETTINGS_UPDATE')")
    public ResponseEntity<AppSettingsResponseDto> updateAppSettings(@Valid @RequestBody AppSettingsRequestDto appSettingsRequestDto) {
        AppSettings appSettings = appSettingsService.updateAppSettingsFromDto(appSettingsRequestDto, getLoggedInUser());

        return ResponseEntity.ok().body(MapperUtil.toDto(appSettings, AppSettingsResponseDto.class));
    }
}
