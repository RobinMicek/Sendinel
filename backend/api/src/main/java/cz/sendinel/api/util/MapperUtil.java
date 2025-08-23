package cz.sendinel.api.util;

import cz.sendinel.api.dto.PageResponseDto;
import cz.sendinel.api.dto.user.UserBasicsResponseDto;
import cz.sendinel.api.dto.user.UserResponseDto;
import cz.sendinel.api.entity.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        // Create custom mapping for User.createdBy/updatedBy -> UserBasicsResponseDto
        // The mapper gets confused when updatedBy points to itself

        // Converter for User -> UserBasicsResponseDto handling nulls
        Converter<User, UserBasicsResponseDto> userToBasicsConverter = context -> {
            User source = context.getSource();
            if (source == null) return null; // handle null createdBy/updatedBy
            return new UserBasicsResponseDto(source.getId(), source.getFirstname(), source.getLastname());
        };

        // Create TypeMap for User -> UserResponseDto
        TypeMap<User, UserResponseDto> userMap = modelMapper.createTypeMap(User.class, UserResponseDto.class);
        userMap.addMappings(mapper -> {
            mapper.using(userToBasicsConverter).map(User::getCreatedBy, UserResponseDto::setCreatedBy);
            mapper.using(userToBasicsConverter).map(User::getUpdatedBy, UserResponseDto::setUpdatedBy);
        });
    }

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

    private MapperUtil() {};
}