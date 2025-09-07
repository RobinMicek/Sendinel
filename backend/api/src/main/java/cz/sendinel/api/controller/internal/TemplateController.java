package cz.sendinel.api.controller.internal;

import cz.sendinel.api.dto.template.export.TemplateImportRequestDto;
import cz.sendinel.api.service.AppSettingsService;
import cz.sendinel.shared.config.Constants;
import cz.sendinel.api.controller.InternalControllerBase;
import cz.sendinel.api.dto.PageResponseDto;
import cz.sendinel.api.dto.template.TemplateBasicsResponseDto;
import cz.sendinel.api.dto.template.TemplateRequestDto;
import cz.sendinel.api.dto.template.TemplateResponseDto;
import cz.sendinel.api.dto.template.export.TemplateExportRequestDto;
import cz.sendinel.api.entity.Template;
import cz.sendinel.api.service.TemplateService;
import cz.sendinel.api.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Constants.INTERNAL_API_ROUTE_PREFIX + "/template")
@RequiredArgsConstructor
public class TemplateController extends InternalControllerBase {

    private final TemplateService templateService;
    private final AppSettingsService appSettingsService;

    @GetMapping
    @PreAuthorize("hasAuthority('TEMPLATES_READ')")
    public ResponseEntity<PageResponseDto<TemplateResponseDto>> getTemplates(Pageable pageable) {
        Page<Template> templatePage = templateService.getTemplates(pageable);

        // Map Page<Template> to Page<TemplateResponseDto>
        Page<TemplateResponseDto> dtoPage = templatePage.map(template -> MapperUtil.toDto(template, TemplateResponseDto.class));

        return ResponseEntity.ok(MapperUtil.fromPage(dtoPage));
    }

    // For dropdowns
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('TEMPLATES_READ')")
    public ResponseEntity<List<TemplateBasicsResponseDto>> getSendersList() {
        List<Template> templates = templateService.getAllTemplates();

        // Map List<Template> to List<TemplateBasicsResponseDto>
        List<TemplateBasicsResponseDto> response = templates.stream()
                .map(template -> MapperUtil.toDto(template, TemplateBasicsResponseDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TEMPLATES_READ')")
    public ResponseEntity<TemplateResponseDto> getTemplate(@PathVariable UUID id) {
        Template template = templateService.getTemplateById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Template does not exist")
        );

        return ResponseEntity.ok(MapperUtil.toDto(template, TemplateResponseDto.class));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TEMPLATES_CREATE')")
    public ResponseEntity<TemplateResponseDto> createClient(@Valid @RequestBody TemplateRequestDto templateRequestDto) {
        Template template = templateService.createTemplateFromDto(templateRequestDto, getLoggedInUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.toDto(template, TemplateResponseDto.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TEMPLATES_UPDATE')")
    public ResponseEntity<TemplateResponseDto> updateTemplate(@PathVariable UUID id, @Valid @RequestBody TemplateRequestDto templateRequestDto) {
        Template template = templateService.updateTemplateFromDto(id, templateRequestDto, getLoggedInUser());

        return ResponseEntity.ok().body(MapperUtil.toDto(template, TemplateResponseDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TEMPLATES_DELETE')")
    public ResponseEntity<Void> deleteTemplate(@PathVariable UUID id) {
        templateService.deleteTemplate(id, getLoggedInUser());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/export")
    @PreAuthorize("hasAuthority('TEMPLATES_READ')")
    public ResponseEntity<InputStreamResource> exportTemplates(@Valid @RequestBody TemplateExportRequestDto templateExportRequestDto) throws IOException {
        File export = templateService.createExport(templateExportRequestDto.getIds());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(export));

        String filename = "templates_" + Instant.now().toString().replaceAll(":", "-") +  Constants.EXPORT_FILE_EXTENSION;
        String filenameEncoded = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + filenameEncoded)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(export.length())
                .body(resource);
    }

    @PostMapping("/import")
    @PreAuthorize("hasAuthority('TEMPLATES_CREATE')")
    public ResponseEntity<String> importTemplates(@Valid TemplateImportRequestDto templateImportRequestDto, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (!appSettingsService.getAppSettings().isAllowTemplateImports()) {
            throw new RuntimeException("File upload is not allowed");
        }

        // Save MultipartFile to a temporary file on disk
        File tempFile = File.createTempFile("import-", "-" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(tempFile);

        try {
            templateService.importData(tempFile, templateImportRequestDto.isOverwriteExisting(), getLoggedInUser());

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            // Delete the temp file after processing
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

}
