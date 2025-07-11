package com.likelion.sbstudy.domain.image.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.likelion.sbstudy.domain.image.dto.response.ImageResponse;
import com.likelion.sbstudy.domain.image.entity.Image;
import com.likelion.sbstudy.domain.image.mapper.ImageMapper;
import com.likelion.sbstudy.domain.image.repository.ImageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

  @Value("${file.upload-dir}")
  private String uploadDir;

  @Value("${file.upload-url}")
  private String uploadUrl;

  @Value("${file.default-image}")
  private String defaultName;

  private final ImageRepository imageRepository;
  private final ImageMapper imageMapper;

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(Path.of(uploadDir));
      Path uploadPath = Path.of(uploadDir);
      log.info("Created project image directory at: {}", uploadPath.toAbsolutePath());
      log.info("Directory exists: {}", Files.exists(uploadPath));
      log.info("Directory is writable: {}", Files.isWritable(uploadPath));
    } catch (IOException e) {
      log.warn("Could not initialize project image storage", e);
    }
  }

  @Transactional
  public ImageResponse uploadImages(List<MultipartFile> images) {
    List<Image> imageList = new ArrayList<>();

    // 이미지가 없는 경우 기본 이미지 사용
    if (images == null || images.isEmpty()) {
      Image defaultImage = createAndSaveImage(defaultName, defaultName);
      imageList.add(defaultImage);

      return imageMapper.toImageResponse(imageList);
    }

    // 이미지 업로드 및 ProjectImage 생성/저장
    for (int i = 0; i < images.size(); i++) {
      MultipartFile image = images.get(i);
      try {
        if (image.isEmpty()) {
          continue;
        }

        validateFile(image);
        String originalFilename = image.getOriginalFilename();
        assert originalFilename != null;
        String uniqueFileName = createUniqueFileName(originalFilename, i);

        // 파일 저장
        Path destinationPath = Path.of(uploadDir, uniqueFileName);
        // 파일 저장 디버깅용 로그 추가
        log.info("Saving file to: {}", destinationPath.toAbsolutePath());
        Files.copy(image.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        // ProjectImage 엔티티 생성 및 저장
        Image newImage = createAndSaveImage(uniqueFileName, originalFilename);
        imageList.add(newImage);

      } catch (IOException e) {
        throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
      }
    }

    if (imageList.isEmpty()) {
      Image defaultImage = createAndSaveImage(defaultName, defaultName);

      imageList.add(defaultImage);
    }

    log.info("Uploaded ProjectImage: {}", imageList.stream().map(Image::getImageUrl).toList());

    return imageMapper.toImageResponse(imageList);
  }

  private void validateFile(MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("빈 파일은 업로드할 수 없습니다.");
    }

    if (file.getSize() > 5 * 1024 * 1024) {
      throw new IllegalArgumentException("파일 크기는 5MB를 초과할 수 없습니다.");
    }

    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
    }
  }

  private String createUniqueFileName(String originalFilename, int index) {
    String extension =
        originalFilename.contains(".")
            ? originalFilename.substring(originalFilename.lastIndexOf("."))
            : "";
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

    return String.format(
        "%s_%d%s",
        timestamp, // 타임스탬프
        index, // 이미지 순서
        extension); // 확장자
  }

  private Image createAndSaveImage(String fileName, String originalFileName) {
    // 클라이언트 접근용 URL 생성
    String fullImageUrl = uploadUrl + "/" + fileName;
    return imageRepository.save(new Image(null, fullImageUrl, originalFileName));
  }

  @Transactional
  public void deleteImage(String fileName) {
    try {
      Path imagePath = Path.of(uploadDir, fileName);
      Files.deleteIfExists(imagePath);
      log.info("Deleted image file: {}", imagePath);
    } catch (IOException e) {
      log.error("Failed to delete image file: {}", fileName, e);
      throw new RuntimeException("이미지 파일 삭제 중 오류가 발생했습니다.");
    }
  }
}

