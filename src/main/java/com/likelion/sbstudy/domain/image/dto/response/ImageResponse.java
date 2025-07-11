package com.likelion.sbstudy.domain.image.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "ImageResponse DTO", description = "이미지 정보 응답 반환")
public class ImageResponse {

  @Schema(description = "이미지 URL 리스트")
  private List<String> imagesUrl;
}

