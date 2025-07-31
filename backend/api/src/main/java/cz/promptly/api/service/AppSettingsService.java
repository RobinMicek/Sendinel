package cz.promptly.api.service;

import cz.promptly.api.dto.appsettings.AppSettingsRequestDto;
import cz.promptly.api.entity.AppSettings;
import cz.promptly.api.entity.User;

public interface AppSettingsService {
    AppSettings getAppSettings();
    AppSettings updateAppSettings(AppSettings appSettings);
    AppSettings updateAppSettingsFromDto(AppSettingsRequestDto appSettingsRequestDto, User updatedBy);
}
