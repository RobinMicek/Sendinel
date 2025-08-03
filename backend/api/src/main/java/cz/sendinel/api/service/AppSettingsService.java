package cz.sendinel.api.service;

import cz.sendinel.api.dto.appsettings.AppSettingsRequestDto;
import cz.sendinel.api.entity.AppSettings;
import cz.sendinel.api.entity.User;

public interface AppSettingsService {
    AppSettings getAppSettings();
    AppSettings updateAppSettings(AppSettings appSettings);
    AppSettings updateAppSettingsFromDto(AppSettingsRequestDto appSettingsRequestDto, User updatedBy);
}
