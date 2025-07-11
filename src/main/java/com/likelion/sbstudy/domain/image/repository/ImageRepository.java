package com.likelion.sbstudy.domain.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.likelion.sbstudy.domain.image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}

