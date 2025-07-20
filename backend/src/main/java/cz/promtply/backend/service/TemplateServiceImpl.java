package cz.promtply.backend.service;

import cz.promtply.backend.dto.template.TemplateRequestDto;
import cz.promtply.backend.entity.Template;
import cz.promtply.backend.entity.User;
import cz.promtply.backend.exceptions.ResourceNotFoundException;
import cz.promtply.backend.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;

    @Override
    public Template createTemplate(Template template) {
        template.setCreatedOn(Instant.now());
        template.setUpdatedOn(Instant.now());

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
        template.setMarkdownRaw(templateRequestDto.getMarkdownRaw());
        template.setPreferMarkdown(templateRequestDto.isPreferMarkdown());
        template.setReplyTo(templateRequestDto.getReplyTo());

        template.setCreatedBy(createdBy);
        template.setUpdatedBy(createdBy);

        return createTemplate(template);
    }

    @Override
    public Optional<Template> getTemplateById(UUID id) {
        return templateRepository.findById(id);
    }

    @Override
    public List<Template> getAllTemplates() {
        return templateRepository.findAll();
    }

    @Override
    public Page<Template> getTemplates(Pageable pageable) {
        return templateRepository.findAll(pageable);
    }

    @Override
    public Template updateTemplate(UUID id, Template template) {
        Template existingTemplate = templateRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Template not found with id " + id)
        );

        existingTemplate.setName(template.getName());
        existingTemplate.setDescription(template.getDescription());
        existingTemplate.setSubject(template.getSubject());
        existingTemplate.setSchema(template.getSchema());
        existingTemplate.setTextRaw(template.getTextRaw());
        existingTemplate.setHtmlRaw(template.getHtmlRaw());
        existingTemplate.setMarkdownRaw(template.getMarkdownRaw());
        existingTemplate.setPreferMarkdown(template.isPreferMarkdown());
        existingTemplate.setReplyTo(template.getReplyTo());

        existingTemplate.setUpdatedBy(template.getUpdatedBy());
        existingTemplate.setUpdatedOn(Instant.now());

        return templateRepository.save(existingTemplate);
    }

    @Override
    public Template updateTemplateFromDto(UUID id, TemplateRequestDto templateRequestDto, User updatedBy) {
        Template template = new Template();
        template.setName(templateRequestDto.getName());
        template.setDescription(templateRequestDto.getDescription());
        template.setSubject(templateRequestDto.getSubject());
        template.setSchema(templateRequestDto.getSchema());
        template.setTextRaw(templateRequestDto.getTextRaw());
        template.setHtmlRaw(templateRequestDto.getHtmlRaw());
        template.setMarkdownRaw(templateRequestDto.getMarkdownRaw());
        template.setPreferMarkdown(templateRequestDto.isPreferMarkdown());

        template.setUpdatedBy(updatedBy);

        return updateTemplate(id, template);
    }

    @Override
    public void deleteTemplate(Template template) {
        templateRepository.delete(template);
    }

    @Override
    public void deleteTemplate(UUID id) {
        Template template = templateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Template not found with id " + id));
        deleteTemplate(template);
    }

    @Override
    public void deleteTemplate(UUID id, User deletedBy) {
        Template template = templateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Template not found with id " + id));
        template.setUpdatedBy(deletedBy);

        deleteTemplate(template);
    }
}
