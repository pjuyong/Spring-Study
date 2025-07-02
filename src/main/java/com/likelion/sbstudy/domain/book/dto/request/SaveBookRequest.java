package com.likelion.sbstudy.domain.book.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "SaveBookRequest: 장바구니 등록 DTO")
public class SaveBookRequest {

  @Schema(description = "책 제목", example = "진격의거인")
  private String title;

  @Schema(description = "책 저자", example = "이사야마 하지메")
  private String author;

  @Schema(description = "책 가격", example = "12000")
  private int price;
}
