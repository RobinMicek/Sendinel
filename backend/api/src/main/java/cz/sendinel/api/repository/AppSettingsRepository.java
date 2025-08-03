package cz.sendinel.api.repository;

import cz.sendinel.api.entity.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSettingsRepository extends JpaRepository<AppSettings, Integer> {
    AppSettings findById(long id);
    boolean existsById(long id);
}
