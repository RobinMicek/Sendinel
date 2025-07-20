package cz.promtply.backend.service;

import cz.promtply.backend.dto.template.TemplateRequestDto;
import cz.promtply.backend.entity.Template;
import cz.promtply.backend.entity.User;
import cz.promtply.backend.models.templateexport.TemplateExportMetaData;
import cz.promtply.backend.models.templateexport.TemplateExportTemplateData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TemplateService {
    Template createTemplate(Template template);
    Template createTemplateFromDto(TemplateRequestDto templateRequestDto, User createdBy);
    Optional<Template> getTemplateById(UUID id);
    List<Template> getAllTemplates();
    Page<Template> getTemplates(Pageable pageable);
    Template updateTemplate(UUID id, Template template);
    Template updateTemplateFromDto(UUID id, TemplateRequestDto templateRequestDto, User updatedBy);
    void deleteTemplate(Template template);
    void deleteTemplate(UUID id);
    void deleteTemplate(UUID id, User deletedBy);

    File createExport(List<UUID> ids, boolean overwriteExisting) throws IOException;
    File createExport(List<TemplateExportTemplateData> templates, TemplateExportMetaData metadata) throws IOException;
    void importData(File importFile, User importedBy) throws IOException;
}
