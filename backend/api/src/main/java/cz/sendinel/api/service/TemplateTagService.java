package cz.sendinel.api.service;

import cz.sendinel.api.dto.template.tags.TemplateTagRequestDto;
import cz.sendinel.api.entity.TemplateTag;
import cz.sendinel.api.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TemplateTagService {
    List<TemplateTag> getAllTags();
    Optional<TemplateTag> getTag(UUID id);
    Optional<TemplateTag> getTagByName(String name);

    TemplateTag createTag(TemplateTag tag);
    TemplateTag createTemplateTagFromDto(TemplateTagRequestDto templateTagRequestDto, User createdBy);

    void deleteTag(TemplateTag tag);
    void deleteTag(UUID id);
    void deleteOrphanedTags();
}
