package cz.sendinel.api.util;

import cz.sendinel.api.dto.PageResponseDto;
import cz.sendinel.api.dto.user.UserBasicsResponseDto;
import cz.sendinel.api.dto.user.UserResponseDto;
import cz.sendinel.api.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static <E, D> D toDto(E entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public static <T> PageResponseDto<T> fromPage(Page<T> page) {
        PageResponseDto<T> dto = new PageResponseDto<>();
        dto.setContent(page.getContent());
        dto.setPageNumber(page.getNumber());
        dto.setPageSize(page.getSize());
        dto.setTotalElements(page.getTotalElements());
        dto.setTotalPages(page.getTotalPages());
        dto.setFirst(page.isFirst());
        dto.setLast(page.isLast());
        dto.setEmpty(page.isEmpty());

        return dto;
    }

    // Special mapper for User -> UserResponseDto
    // Normal mapping fails do to self-references on createdBy/updatedBy
    public static UserResponseDto userToDto(User user) {
        if (user == null) return null;

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedOn(user.getCreatedOn());
        dto.setUpdatedOn(user.getUpdatedOn());
        dto.setDeletedOn(user.getDeletedOn());

        // Map createdBy / updatedBy safely using UserBasicsResponseDto
        dto.setCreatedBy(user.getCreatedBy() != null
                ? new UserBasicsResponseDto(user.getCreatedBy().getId(), user.getCreatedBy().getFirstname(), user.getCreatedBy().getLastname())
                : null);

        dto.setUpdatedBy(user.getUpdatedBy() != null
                ? new UserBasicsResponseDto(user.getUpdatedBy().getId(), user.getUpdatedBy().getFirstname(), user.getUpdatedBy().getLastname())
                : null);

        return dto;
    }

    private MapperUtil() {}
}
