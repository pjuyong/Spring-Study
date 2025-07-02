package com.likelion.sbstudy.domain.book.converter;
import com.likelion.sbstudy.domain.book.dto.response.CreateBookResponse;
import com.likelion.sbstudy.domain.book.dto.response.SaveBookResponse;
import com.likelion.sbstudy.domain.book.dto.response.UpdateBookResponse;
import com.likelion.sbstudy.domain.book.entity.Book;
import com.likelion.sbstudy.domain.book.dto.response.BookResponse;
import org.springframework.stereotype.Component;

@Component
public class ToBookResponse {
  // Entity를 DTO로 변환해주는 메소드

  public BookResponse toBookResponse(Book book) {
    return BookResponse.builder()
        .bookId(book.getId())
        .title(book.getTitle())
        .author(book.getAuthor())
        .price(book.getPrice())
        .content(book.getContent())
        .build();
  }

  public CreateBookResponse toCreateBookResponse(Book book) {
    return CreateBookResponse.builder()
        .bookId(book.getId())
        .title(book.getTitle())
        .author(book.getAuthor())
        .price(book.getPrice())
        .content(book.getContent())
        .build();
  }

  public UpdateBookResponse toUpdateeBookResponse(Book book) {
    return UpdateBookResponse.builder()
        .title(book.getTitle())
        .author(book.getAuthor())
        .price(book.getPrice())
        .content(book.getContent())
        .build();
  }

  public SaveBookResponse toSaveBookResponse(Book book) {
    return SaveBookResponse.builder()
        .title(book.getTitle())
        .author(book.getAuthor())
        .price(book.getPrice())
        .build();
  }
}
