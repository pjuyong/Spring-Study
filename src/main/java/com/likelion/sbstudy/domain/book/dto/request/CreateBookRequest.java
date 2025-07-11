package com.likelion.sbstudy.domain.book.dto.request;

import com.likelion.sbstudy.domain.book.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "CreateBookRequest DTO", description = "책 생성을 위한 데이터 전송")
public class CreateBookRequest {

  @Schema(description = "책 제목", example = "그리고 아무도 없었다")
  private String title;

  @Schema(description = "작가", example = "아가사 크리스티")
  private String author;

  @Schema(description = "출판사", example = "꿈나무책")
  private String publisher;

  @Schema(description = "가격", example = "20000")
  private Integer price;

  @Schema(description = "책 설명", example = "한 곳에 초대된 사람들.. 그리고 하나 둘 죽어가는데..")
  private String description;

  @Schema(description = "출간날짜", example = "2025년 3월 22일")
  private String releaseDate;

  @Schema(description = "카테고리")
  private List<Category> categoryList;
}
