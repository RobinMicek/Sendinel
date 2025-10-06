package cz.sendinel.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.sendinel.api.dto.template.TemplateRequestDto;
import cz.sendinel.api.entity.Template;
import cz.sendinel.api.entity.TemplateTag;
import cz.sendinel.api.entity.User;
import cz.sendinel.api.exceptions.AlreadyExistsException;
import cz.sendinel.api.exceptions.ResourceNotFoundException;
import cz.sendinel.api.model.templateexport.TemplateExportMetaData;
import cz.sendinel.api.model.templateexport.TemplateExportTemplateData;
import cz.sendinel.api.repository.TemplateRepository;
import cz.sendinel.shared.config.Constants;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final TemplateTagService templateTagService;
    private final ObjectMapper objectMapper;

    @Value("${app.app-version}")
    private String appVersion;

    private final Flyway flyway;

    @Override
    public Template createTemplate(Template template) {
        template.setCreatedOn(Instant.now());
        template.setUpdatedOn(Instant.now());

        // !!! DOES NOT AUTOMATICALLY GENERATE IDs, NEED TO CALL .setId() ON CREATION !!!
        template.setId(UUID.randomUUID());

        return templateRepository.save(template);
    }

    @Override
    public Template createTemplateFromDto(TemplateRequestDto templateRequestDto, User createdBy) {
        Template template = new Template();
        template.setName(templateRequestDto.getName());
        template.setDescription(templateRequestDto.getDescription());
        template.setSubject(templateRequestDto.getSubject());
        template.setSchema(templateRequestDto.getSchema());
        template.setTextRaw(templateRequestDto.getTextRaw());
        template.setHtmlRaw(templateRequestDto.getHtmlRaw());
        template.setReplyTo(templateRequestDto.getReplyTo());

        template.setCreatedBy(createdBy);
        template.setUpdatedBy(createdBy);

        // Tags
        Set<TemplateTag> tags = templateRequestDto.getTags().stream()
                .map(dto -> templateTagService.createTemplateTagFromDto(dto, createdBy))
                .collect(Collectors.toSet());
        template.setTags(tags);

        return createTemplate(template);
    }

    @Override
    public Optional<Template> getTemplateById(UUID id) {
        return templateRepository.findByIdAndDeletedOnIsNull(id);
    }

    @Override
    public List<Template> getAllTemplates() {
        return templateRepository.findAllByDeletedOnIsNull();
    }

    @Override
    public Page<Template> getTemplates(Pageable pageable) {
        return templateRepository.findAllByDeletedOnIsNull(pageable);
    }

    @Override
    public Page<Template> getTemplates(Pageable pageable, Specification<Template> specification) {
        return templateRepository.findAll(specification, pageable);
    }

    @Override
    public Page<Template> getTemplates(Pageable pageable, Specification<Template> specification, TemplateTag tag) {
        specification = specification.and((root, query, cb) -> {
            Join<Template, TemplateTag> tagJoin = root.join("tags", JoinType.INNER);
            return cb.equal(tagJoin.get("id"), tag.getId());
        });

        return templateRepository.findAll(specification, pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Template updateTemplate(UUID id, Template template) {
        Template existingTemplate = templateRepository.findByIdAndDeletedOnIsNull(id).orElseThrow(
                () -> new ResourceNotFoundException("Template not found with id " + id)
        );

        existingTemplate.setName(template.getName());
        existingTemplate.setDescription(template.getDescription());
        existingTemplate.setSubject(template.getSubject());
        existingTemplate.setSchema(template.getSchema());
        existingTemplate.setTextRaw(template.getTextRaw());
        existingTemplate.setHtmlRaw(template.getHtmlRaw());
        existingTemplate.setReplyTo(template.getReplyTo());

        existingTemplate.setUpdatedBy(template.getUpdatedBy());
        existingTemplate.setUpdatedOn(Instant.now());

        existingTemplate.setTags(template.getTags());

        Template savedTemplate = templateRepository.save(existingTemplate);

        // Remove unused template tags
        templateTagService.deleteOrphanedTags();

        return savedTemplate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Template updateTemplateFromDto(UUID id, TemplateRequestDto templateRequestDto, User updatedBy) {
        Template template = new Template();
        template.setName(templateRequestDto.getName());
        template.setDescription(templateRequestDto.getDescription());
        template.setSubject(templateRequestDto.getSubject());
        template.setSchema(templateRequestDto.getSchema());
        template.setTextRaw(templateRequestDto.getTextRaw());
        template.setHtmlRaw(templateRequestDto.getHtmlRaw());
        template.setReplyTo(templateRequestDto.getReplyTo());

        template.setUpdatedBy(updatedBy);

        // Tags
        Set<TemplateTag> tags = templateRequestDto.getTags().stream()
                .map(dto -> templateTagService.createTemplateTagFromDto(dto, updatedBy))
                .collect(Collectors.toSet());
        template.setTags(tags);

        return updateTemplate(id, template);
    }

    @Override
    public void deleteTemplate(Template template) {
        templateRepository.delete(template);
    }

    @Override
    public void deleteTemplate(UUID id) {
        Template template = templateRepository.findByIdAndDeletedOnIsNull(id).orElseThrow(() -> new ResourceNotFoundException("Template not found with id " + id));
        deleteTemplate(template);
    }

    @Override
    public void deleteTemplate(UUID id, User deletedBy) {
        Template template = templateRepository.findByIdAndDeletedOnIsNull(id).orElseThrow(() -> new ResourceNotFoundException("Template not found with id " + id));
        template.setUpdatedBy(deletedBy);

        deleteTemplate(template);
    }

    @Override
    public File createExport(List<UUID> ids) throws IOException {
        Instant exportedOn = Instant.now();
        String appVersion = this.appVersion;
        String dbVersion = flyway.info().current().getVersion().toString();

        TemplateExportMetaData metadata = new TemplateExportMetaData(exportedOn, appVersion, dbVersion);

        List<TemplateExportTemplateData> exportTemplates = new LinkedList<>();
        for (UUID id : ids) {
            Template template = getTemplateById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Template not found with id " + id)
            );

            exportTemplates.add(new TemplateExportTemplateData(
                    template.getId(),
                    template.getName(),
                    template.getDescription(),
                    template.getSubject(),
                    template.getSchema(),
                    template.getTextRaw(),
                    template.getHtmlRaw(),
                    template.getCreatedOn(),
                    template.getUpdatedOn(),
                    template.getDeletedOn()
            ));
        }

        return createExport(exportTemplates, metadata);
    }

    @Override
    public File createExport(List<TemplateExportTemplateData> templates, TemplateExportMetaData metadata) throws IOException {
        // Create a temp file for tar.gz output
        File tarGzFile = File.createTempFile("temp-export", ".tar.gz");
        tarGzFile.deleteOnExit();  // delete on JVM exit

        try (FileOutputStream fos = new FileOutputStream(tarGzFile);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             GzipCompressorOutputStream gzos = new GzipCompressorOutputStream(bos);
             TarArchiveOutputStream taos = new TarArchiveOutputStream(gzos)) {

            // Write templates
            for (TemplateExportTemplateData template : templates ) {
                // Serialize template to JSON string in memory
                byte[] jsonBytes = objectMapper.writeValueAsBytes(template);

                String entryName = template.getId() + Constants.EXPORT_FILE_PART_EXTENSION;
                TarArchiveEntry entry = new TarArchiveEntry(entryName);
                entry.setSize(jsonBytes.length);

                taos.putArchiveEntry(entry);
                taos.write(jsonBytes);
                taos.closeArchiveEntry();
            }

            // Write metadata
            // Serialize metadata to JSON string in memory
            byte[] jsonBytes = objectMapper.writeValueAsBytes(metadata);

            String entryName = "meta" + Constants.EXPORT_FILE_PART_EXTENSION;
            TarArchiveEntry entry = new TarArchiveEntry(entryName);
            entry.setSize(jsonBytes.length);

            taos.putArchiveEntry(entry);
            taos.write(jsonBytes);
            taos.closeArchiveEntry();
        }

        return tarGzFile;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importData(File importFile, boolean overwriteExisting, User importedBy) throws IOException {
        TemplateExportMetaData metadata = null;
        List<TemplateExportTemplateData> importedTemplates = new LinkedList<>();

        try (FileInputStream fis = new FileInputStream(importFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        GzipCompressorInputStream gzis = new GzipCompressorInputStream(bis);
        TarArchiveInputStream tais = new TarArchiveInputStream(gzis)) {

            TarArchiveEntry entry;
            while ((entry = tais.getNextTarEntry()) != null) {
                if (entry.isDirectory()) continue;

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = tais.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }

                byte[] jsonData = baos.toByteArray();
                String name = entry.getName();

                if (name.startsWith("meta" + Constants.EXPORT_FILE_PART_EXTENSION)) {
                    metadata = objectMapper.readValue(jsonData, TemplateExportMetaData.class);
                } else {
                    TemplateExportTemplateData importedTemplateData = objectMapper.readValue(jsonData, TemplateExportTemplateData.class);
                    importedTemplates.add(importedTemplateData);
                }
            }
        }

        if (metadata == null) {
            throw new ResourceNotFoundException("Import metadata not found");
        }

        for (TemplateExportTemplateData importedTemplate : importedTemplates) {
            Template newTemplate = new Template();
            newTemplate.setName(importedTemplate.getName());
            newTemplate.setDescription(importedTemplate.getDescription());
            newTemplate.setSubject(importedTemplate.getSubject());
            newTemplate.setSchema(importedTemplate.getSchema());
            newTemplate.setTextRaw(importedTemplate.getTextRaw());
            newTemplate.setHtmlRaw(importedTemplate.getHtmlRaw());
            newTemplate.setCreatedBy(importedBy);
            newTemplate.setUpdatedBy(importedBy);
            newTemplate.setCreatedOn(importedTemplate.getCreatedOn());
            newTemplate.setUpdatedOn(importedTemplate.getUpdatedOn());
            newTemplate.setDeletedOn(importedTemplate.getDeletedOn());

            boolean idExists = templateRepository.existsById(importedTemplate.getId());
            if (idExists) {
                if (!overwriteExisting) {
                    throw new AlreadyExistsException("Template " + importedTemplate.getId() + " already exists and overwrite existing is disabled");
                }

                // If emails exists and is deleted, undelete it first so it can be updated
                // (otherwise the update function will fail, because it cannot find deleted templates)
                templateRepository.findById(importedTemplate.getId()).ifPresent(template -> {
                    template.setDeletedOn(null);
                    templateRepository.save(template);
                });

                updateTemplate(importedTemplate.getId(), newTemplate);
            } else {
                newTemplate.setId(importedTemplate.getId());
                templateRepository.save(newTemplate);
            }

        }
    }
}
