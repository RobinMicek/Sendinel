package cz.promtply.backend.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static <E, D> D toDto(E entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    private MapperUtil() {};
}