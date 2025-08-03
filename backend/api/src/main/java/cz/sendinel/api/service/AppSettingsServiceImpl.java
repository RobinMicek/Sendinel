package cz.sendinel.api.service;

import cz.sendinel.api.dto.appsettings.AppSettingsRequestDto;
import cz.sendinel.api.entity.AppSettings;
import cz.sendinel.api.entity.User;
import cz.sendinel.api.repository.AppSettingsRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AppSettingsServiceImpl implements AppSettingsService {

    private final AppSettingsRepository appSettingsRepository;

    @Override
    public AppSettings getAppSettings() {
        return appSettingsRepository.findById(1L);
    }

    @Override
    public AppSettings updateAppSettings(AppSettings appSettings) {
        AppSettings savedAppSettings = appSettingsRepository.findById(1L);

        savedAppSettings.setTrackOpenedEmails(appSettings.isTrackOpenedEmails());
        savedAppSettings.setAllowTemplateImports(appSettings.isAllowTemplateImports());
        savedAppSettings.setDisplayNewVersionAlert(appSettings.isDisplayNewVersionAlert());
        savedAppSettings.setUseGravatar(appSettings.isUseGravatar());

        savedAppSettings.setUpdatedBy(appSettings.getUpdatedBy());
        savedAppSettings.setUpdatedOn(Instant.now());

        return appSettingsRepository.save(savedAppSettings);
    }

    @Override
    public AppSettings updateAppSettingsFromDto(AppSettingsRequestDto appSettingsRequestDto, User updatedBy) {
        AppSettings appSettings = new AppSettings();
        appSettings.setTrackOpenedEmails(appSettingsRequestDto.isTrackOpenedEmails());
        appSettings.setAllowTemplateImports(appSettingsRequestDto.isAllowTemplateImports());
        appSettings.setDisplayNewVersionAlert(appSettingsRequestDto.isDisplayNewVersionAlert());
        appSettings.setUseGravatar(appSettingsRequestDto.isUseGravatar());

        appSettings.setUpdatedBy(updatedBy);

        return updateAppSettings(appSettings);
    }

    // Create default App Settings on startup row if it does not exist
    @PostConstruct
    public void initializeSettingsIfMissing() {
        if (!appSettingsRepository.existsById(1L)) {
            AppSettings appSettings = new AppSettings();
            appSettings.setTrackOpenedEmails(false);
            appSettings.setAllowTemplateImports(true);
            appSettings.setDisplayNewVersionAlert(true);
            appSettings.setUseGravatar(true);
            appSettings.setUpdatedBy(null);
            appSettings.setUpdatedOn(Instant.now());
            appSettingsRepository.save(appSettings);
        }
    }
}
