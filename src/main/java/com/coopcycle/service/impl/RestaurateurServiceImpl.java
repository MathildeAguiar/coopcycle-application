package com.coopcycle.service.impl;

import com.coopcycle.domain.Restaurateur;
import com.coopcycle.repository.RestaurateurRepository;
import com.coopcycle.service.RestaurateurService;
import com.coopcycle.service.dto.RestaurateurDTO;
import com.coopcycle.service.mapper.RestaurateurMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Restaurateur}.
 */
@Service
@Transactional
public class RestaurateurServiceImpl implements RestaurateurService {

    private final Logger log = LoggerFactory.getLogger(RestaurateurServiceImpl.class);

    private final RestaurateurRepository restaurateurRepository;

    private final RestaurateurMapper restaurateurMapper;

    public RestaurateurServiceImpl(RestaurateurRepository restaurateurRepository, RestaurateurMapper restaurateurMapper) {
        this.restaurateurRepository = restaurateurRepository;
        this.restaurateurMapper = restaurateurMapper;
    }

    @Override
    public RestaurateurDTO save(RestaurateurDTO restaurateurDTO) {
        log.debug("Request to save Restaurateur : {}", restaurateurDTO);
        Restaurateur restaurateur = restaurateurMapper.toEntity(restaurateurDTO);
        restaurateur = restaurateurRepository.save(restaurateur);
        return restaurateurMapper.toDto(restaurateur);
    }

    @Override
    public Optional<RestaurateurDTO> partialUpdate(RestaurateurDTO restaurateurDTO) {
        log.debug("Request to partially update Restaurateur : {}", restaurateurDTO);

        return restaurateurRepository
            .findById(restaurateurDTO.getId())
            .map(
                existingRestaurateur -> {
                    restaurateurMapper.partialUpdate(existingRestaurateur, restaurateurDTO);
                    return existingRestaurateur;
                }
            )
            .map(restaurateurRepository::save)
            .map(restaurateurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurateurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Restaurateurs");
        return restaurateurRepository.findAll(pageable).map(restaurateurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RestaurateurDTO> findOne(Long id) {
        log.debug("Request to get Restaurateur : {}", id);
        return restaurateurRepository.findById(id).map(restaurateurMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Restaurateur : {}", id);
        restaurateurRepository.deleteById(id);
    }
}
