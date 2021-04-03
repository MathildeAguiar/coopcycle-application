package com.coopcycle.service.mapper;

import com.coopcycle.domain.*;
import com.coopcycle.service.dto.LivreurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Livreur} and its DTO {@link LivreurDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LivreurMapper extends EntityMapper<LivreurDTO, Livreur> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LivreurDTO toDtoId(Livreur livreur);
}
