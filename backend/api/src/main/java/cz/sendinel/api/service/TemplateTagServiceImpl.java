package cz.sendinel.api.service;

import cz.sendinel.api.dto.template.tags.TemplateTagRequestDto;
import cz.sendinel.api.entity.TemplateTag;
import cz.sendinel.api.entity.User;
import cz.sendinel.api.repository.TemplateTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TemplateTagServiceImpl implements TemplateTagService {

    private final TemplateTagRepository templateTagRepository;

    @Override
    public List<TemplateTag> getAllTags() {
        return templateTagRepository.findAll();
    }

    @Override
    public Optional<TemplateTag> getTag(UUID id) {
        return templateTagRepository.findById(id);
    }

    @Override
    public Optional<TemplateTag> getTagByName(String name) {
        return templateTagRepository.findByName(name);
    }

    @Override
    public TemplateTag createTag(TemplateTag tag) {
        tag.setCreatedOn(Instant.now());

        return templateTagRepository.save(tag);
    }

    @Override
    public TemplateTag createTemplateTagFromDto(TemplateTagRequestDto dto, User createdBy) {
        // Return existing tag
        Optional<TemplateTag> existingTag = templateTagRepository.findByName(dto.getName());
        if (existingTag.isPresent()) {
            return existingTag.get();
        }

        TemplateTag tag = new TemplateTag();
        tag.setName(dto.getName());
        tag.setCreatedBy(createdBy);

        return createTag(tag);
    }

    @Override
    public void deleteTag(TemplateTag tag) {
        templateTagRepository.delete(tag);
    }

    @Override
    public void deleteTag(UUID id) {
        templateTagRepository.deleteById(id);
    }

    @Override
    public void deleteOrphanedTags() {
        templateTagRepository.deleteAllOrphanTags();
    }
}
