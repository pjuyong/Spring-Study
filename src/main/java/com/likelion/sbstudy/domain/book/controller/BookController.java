package com.likelion.sbstudy.domain.book.controller;

import com.likelion.sbstudy.domain.book.dto.request.CreateBookRequest;
import com.likelion.sbstudy.domain.book.dto.request.SaveBookRequest;
import com.likelion.sbstudy.domain.book.dto.request.UpdateBookRequest;
import com.likelion.sbstudy.domain.book.dto.response.BookResponse;
import com.likelion.sbstudy.domain.book.dto.response.CreateBookResponse;
import com.likelion.sbstudy.domain.book.dto.response.SaveBookResponse;
import com.likelion.sbstudy.domain.book.dto.response.UpdateBookResponse;
import com.likelion.sbstudy.domain.book.entity.Book;
import com.likelion.sbstudy.domain.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Post", description = "북마켓 API")
public class BookController {

  private final BookService bookService;

  @Operation(summary = "책 생성",
      description = "책 목록 페이지에서 책 정보 작성 후 생성 버튼을 눌렀을 때 요청되는 API")
  @PostMapping("/books")
  public ResponseEntity<CreateBookResponse> createBook(@Parameter(description = "책 정보") @RequestBody CreateBookRequest createBookRequest) { // dto
    return ResponseEntity.ok(bookService.createBook(createBookRequest));
  }

  @Operation(summary = "책 정보 수정",
      description = "첵 목록 페이지에서 책 정보 수정 후 수정 완료 버튼을 눌러쓸 때 요청되는 API")
  @PutMapping("/books/{id}")
  public ResponseEntity<UpdateBookResponse> updateBook(@RequestBody UpdateBookRequest updateBookRequest,
      @Parameter(description = "책 일련번호") @PathVariable Long id) { // dto
    return ResponseEntity.ok(bookService.updateBook(id, updateBookRequest));
  }

  @Operation(summary = "장바구니 넣기",
      description = "책 목록 페이지에서 장바구니 넣기 버튼을 눌렀을 때 요청되는 API")
  @PostMapping("/books/basket/{id}")
  public ResponseEntity<SaveBookResponse> saveBook(@RequestBody SaveBookRequest saveBookRequest,
      @Parameter(description = "책 일련번호") @PathVariable Long id) {
    return ResponseEntity.ok(bookService.saveBook(id, saveBookRequest));
  }

  /*
  @Operation(summary = "책 주문하기",
      description = "책 목록 페이지에서 주문하기 버튼을 눌렀을 때 요청되는 API")
  @PostMapping("/books")
  public String orderBook(@Parameter(description = "책 정보") @RequestBody CreateBookRequest createBookRequest) { // dto
    return "책 주문";
  }
   */

  @Operation(summary = "책 전체 조회",
      description = "책 목록 페이지로 이동할 때 요청되는 API")
  @GetMapping("/books")
  public ResponseEntity<List<BookResponse>> getAllBooks() {
    return ResponseEntity.ok(bookService.getAllBooks());
  }

  @Operation(summary = "책 단일 조회",
      description = "책 목록 페이지에서 특정 책에 접근할 때 요청되는 API")
  @GetMapping("/books/{id}")
  public ResponseEntity<BookResponse> getBookById(@Parameter(description = "특정 게시글 ID") @PathVariable Long id) {
    return ResponseEntity.ok(bookService.getBook(id));
  }

  @Operation(summary = "책 삭제",
      description = "책 목록 페이지에서 책 삭제 버튼을 눌렀을 때 요청되는 API")
  @DeleteMapping("/books/{id}")
  public ResponseEntity<Boolean> deleteBook(@Parameter(description = "책 일련번호") @PathVariable Long id) {
    return ResponseEntity.ok(bookService.deleteBook(id));
  }

}
