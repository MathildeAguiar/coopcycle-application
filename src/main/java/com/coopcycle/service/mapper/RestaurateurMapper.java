package com.coopcycle.service.mapper;

import com.coopcycle.domain.*;
import com.coopcycle.service.dto.RestaurateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Restaurateur} and its DTO {@link RestaurateurDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RestaurateurMapper extends EntityMapper<RestaurateurDTO, Restaurateur> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RestaurateurDTO toDtoId(Restaurateur restaurateur);
}
