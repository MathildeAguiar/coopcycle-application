package com.coopcycle.service.mapper;

import com.coopcycle.domain.*;
import com.coopcycle.service.dto.CommandeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commande} and its DTO {@link CommandeDTO}.
 */
@Mapper(componentModel = "spring", uses = { RestaurateurMapper.class, LivreurMapper.class, ClientMapper.class })
public interface CommandeMapper extends EntityMapper<CommandeDTO, Commande> {
    @Mapping(target = "restaurateur", source = "restaurateur", qualifiedByName = "id")
    @Mapping(target = "livreur", source = "livreur", qualifiedByName = "id")
    @Mapping(target = "client", source = "client", qualifiedByName = "id")
    CommandeDTO toDto(Commande s);
}
