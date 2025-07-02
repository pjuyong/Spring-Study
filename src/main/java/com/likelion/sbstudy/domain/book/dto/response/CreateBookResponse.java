package com.likelion.sbstudy.domain.book.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder // 빌더를 만들었기 때문에 생성자를 만들 필요가 없음
@Schema(title = "CreateBookResponse: 책 생성 응답 DTO")
public class CreateBookResponse {

  @Schema(description = "책 일련번호", example = "1")
  private Long bookId;

  @Schema(description = "책 제목", example = "진격의거인")
  private String title;

  @Schema(description = "책 저자", example = "이사야마 하지메")
  private String author;

  @Schema(description = "책 가격", example = "12000")
  private int price;

  @Schema(description = "책 정보", example = "진짜 영화 언제보냐")
  private String content;

}
