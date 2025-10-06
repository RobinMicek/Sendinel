package cz.sendinel.api.service;

import cz.sendinel.api.dto.template.TemplateRequestDto;
import cz.sendinel.api.entity.Template;
import cz.sendinel.api.entity.TemplateTag;
import cz.sendinel.api.entity.User;
import cz.sendinel.api.model.templateexport.TemplateExportMetaData;
import cz.sendinel.api.model.templateexport.TemplateExportTemplateData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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
    Page<Template> getTemplates(Pageable pageable, Specification<Template> specification);
    Page<Template> getTemplates(Pageable pageable, Specification<Template> specification, TemplateTag tag);

    Template updateTemplate(UUID id, Template template);
    Template updateTemplateFromDto(UUID id, TemplateRequestDto templateRequestDto, User updatedBy);
    void deleteTemplate(Template template);
    void deleteTemplate(UUID id);
    void deleteTemplate(UUID id, User deletedBy);

    File createExport(List<UUID> ids) throws IOException;
    File createExport(List<TemplateExportTemplateData> templates, TemplateExportMetaData metadata) throws IOException;
    void importData(File importFile, boolean overwriteExisting, User importedBy) throws IOException;
}
