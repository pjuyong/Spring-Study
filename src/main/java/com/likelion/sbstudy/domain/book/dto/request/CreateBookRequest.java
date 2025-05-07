package com.likelion.sbstudy.domain.book.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "CreateBookRequest: 책 생성 요청 DTO")
public class CreateBookRequest {

  @Schema(description = "책 제목", example = "채식주의자")
  private String title;

  @Schema(description = "책 저자", example = "한강")
  private String author;

  @Schema(description = "책 가격", example = "10000")
  private int price;

  @Schema(description = "책 정보", example = "내용내용내용")
  private String content;
}
