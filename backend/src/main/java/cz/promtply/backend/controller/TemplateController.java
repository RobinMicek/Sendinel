package cz.promtply.backend.controller;

import cz.promtply.backend.dto.PageResponseDto;
import cz.promtply.backend.dto.template.TemplateBasicsResponseDto;
import cz.promtply.backend.dto.template.TemplateRequestDto;
import cz.promtply.backend.dto.template.TemplateResponseDto;
import cz.promtply.backend.entity.Template;
import cz.promtply.backend.service.TemplateService;
import cz.promtply.backend.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/template")
@RequiredArgsConstructor
public class TemplateController extends BaseUserLoggedInController {

    private final TemplateService templateService;

    @GetMapping
    public ResponseEntity<PageResponseDto<TemplateResponseDto>> getTemplates(Pageable pageable) {
        Page<Template> templatePage = templateService.getTemplates(pageable);

        // Map Page<Template> to Page<TemplateResponseDto>
        Page<TemplateResponseDto> dtoPage = templatePage.map(template -> MapperUtil.toDto(template, TemplateResponseDto.class));

        return ResponseEntity.ok(MapperUtil.fromPage(dtoPage));
    }

    // For dropdowns
    @GetMapping("/list")
    public ResponseEntity<List<TemplateBasicsResponseDto>> getSendersList() {
        List<Template> templates = templateService.getAllTemplates();

        // Map List<Template> to List<TemplateBasicsResponseDto>
        List<TemplateBasicsResponseDto> response = templates.stream()
                .map(template -> MapperUtil.toDto(template, TemplateBasicsResponseDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateResponseDto> getTemplate(@PathVariable UUID id) {
        Template template = templateService.getTemplateById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Template does not exist")
        );

        return ResponseEntity.ok(MapperUtil.toDto(template, TemplateResponseDto.class));
    }

    @PostMapping
    public ResponseEntity<TemplateResponseDto> createClient(@Valid @RequestBody TemplateRequestDto templateRequestDto) {
        Template template = templateService.createTemplateFromDto(templateRequestDto, getLoggedInUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.toDto(template, TemplateResponseDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemplateResponseDto> updateTemplate(@PathVariable UUID id, @Valid @RequestBody TemplateRequestDto templateRequestDto) {
        Template template = templateService.updateTemplateFromDto(id, templateRequestDto, getLoggedInUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.toDto(template, TemplateResponseDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable UUID id) {
        templateService.deleteTemplate(id, getLoggedInUser());

        return ResponseEntity.noContent().build();
    }

}
