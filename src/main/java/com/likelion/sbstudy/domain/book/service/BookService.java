package com.likelion.sbstudy.domain.book.service;

import com.likelion.sbstudy.domain.book.converter.ToBookResponse;
import com.likelion.sbstudy.domain.book.dto.request.CreateBookRequest;
import com.likelion.sbstudy.domain.book.dto.request.SaveBookRequest;
import com.likelion.sbstudy.domain.book.dto.request.UpdateBookRequest;
import com.likelion.sbstudy.domain.book.dto.response.BookResponse;
import com.likelion.sbstudy.domain.book.dto.response.CreateBookResponse;
import com.likelion.sbstudy.domain.book.dto.response.SaveBookResponse;
import com.likelion.sbstudy.domain.book.dto.response.UpdateBookResponse;
import com.likelion.sbstudy.domain.book.entity.Book;
import com.likelion.sbstudy.domain.book.repository.BookRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // final 필드 생성자 자동 생성
public class BookService {

  private final BookRepository bookRepository;
  private final ToBookResponse toBookResponse;

  // 책 생성
  @Transactional
  public CreateBookResponse createBook(CreateBookRequest createBookRequest) {
    Book book = Book.builder()
        .title(createBookRequest.getTitle())
        .author(createBookRequest.getAuthor())
        .price(createBookRequest.getPrice())
        .content(createBookRequest.getContent())
        .build();
    bookRepository.save(book);

    return toBookResponse.toCreateBookResponse(book);
  }

  // 책 정보 수정
  @Transactional
  public UpdateBookResponse updateBook(Long id, UpdateBookRequest updateBookRequest) {
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("책 정보를 찾을 수 없습니다."));

    Book updateBook = Book.builder()
        .id(book.getId())
        .title(updateBookRequest.getTitle())
        .author(updateBookRequest.getAuthor())
        .price(updateBookRequest.getPrice())
        .content(updateBookRequest.getContent())
        .build();

    bookRepository.save(book);

    return toBookResponse.toUpdateeBookResponse(updateBook);
  }

  // 장바구니 등록
  @Transactional
  public SaveBookResponse saveBook(Long id, SaveBookRequest saveBookRequest) {
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("책 정보를 찾을 수 없습니다."));

    Book saveBook = Book.builder()
        .id(book.getId())
        .title(saveBookRequest.getTitle())
        .author(saveBookRequest.getAuthor())
        .price(saveBookRequest.getPrice())
        .build();

    bookRepository.save(book);

    return toBookResponse.toSaveBookResponse(saveBook);
  }

  // 책 전체 조회
  public List<BookResponse> getAllBooks() {
    List<Book> books = bookRepository.findAll();
    return books.stream().map(toBookResponse::toBookResponse).toList();
  }

  // 책 단일 조회
  public BookResponse getBook(Long id) {
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("책 정보를 찾을 수 없습니다."));
    return toBookResponse.toBookResponse(book);
  }

  // 책 삭제
  @Transactional
  public Boolean deleteBook(Long id) {
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("책 정보를 찾을 수 없습니다."));

    bookRepository.deleteById(id);
    return true;
  }
}
