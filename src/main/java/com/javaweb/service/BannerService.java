package com.javaweb.service;

import com.javaweb.dto.BannerDTO;
import com.javaweb.model.BannerEntity;
import com.javaweb.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    public List<BannerDTO> getAllBanners() {
        return bannerRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BannerDTO getBannerById(Long id) {
        return bannerRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    public BannerDTO createBanner(BannerDTO bannerDTO) {
        BannerEntity entity = convertToEntity(bannerDTO);
        return convertToDTO(bannerRepository.save(entity));
    }

    public BannerDTO updateBanner(Long id, BannerDTO bannerDTO) {
        return bannerRepository.findById(id).map(entity -> {
            entity.setTitle(bannerDTO.getTitle());
            entity.setDiscount(bannerDTO.getDiscount());
            entity.setImage(bannerDTO.getImage());
            return convertToDTO(bannerRepository.save(entity));
        }).orElse(null);
    }

    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }

    private BannerDTO convertToDTO(BannerEntity entity) {
        BannerDTO dto = new BannerDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDiscount(entity.getDiscount());
        dto.setImage(entity.getImage());
        return dto;
    }

    private BannerEntity convertToEntity(BannerDTO dto) {
        BannerEntity entity = new BannerEntity();
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        entity.setTitle(dto.getTitle());
        entity.setDiscount(dto.getDiscount());
        entity.setImage(dto.getImage());
        return entity;
    }
}
