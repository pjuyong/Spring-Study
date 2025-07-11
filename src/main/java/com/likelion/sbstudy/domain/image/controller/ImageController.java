package com.likelion.sbstudy.domain.image.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.likelion.sbstudy.domain.image.dto.response.ImageResponse;
import com.likelion.sbstudy.domain.image.service.ImageService;
import com.likelion.sbstudy.global.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
@Tag(name = "Image", description = "이미지 관련 API")
public class ImageController {

  private final ImageService imageService;

  @Operation(
      summary = "새 이미지 등록",
      description = "새로운 이미지를 등록하고, 등록된 이미지 URL들을 반환합니다. (201 Created)")
  @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<BaseResponse<ImageResponse>> createBook(
      @Parameter(
          description = "이미지들",
          content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
      @RequestPart(value = "images")
      List<MultipartFile> images) {
    ImageResponse response = imageService.uploadImages(images);
    return ResponseEntity.ok(BaseResponse.success("이미지 등록에 성공하였습니다.", response));
  }
}

