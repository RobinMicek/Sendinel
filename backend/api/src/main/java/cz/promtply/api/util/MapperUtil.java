package cz.promtply.api.util;

import cz.promtply.api.dto.PageResponseDto;
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

    private MapperUtil() {};
}