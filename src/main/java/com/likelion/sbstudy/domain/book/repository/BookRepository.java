package com.likelion.sbstudy.domain.book.repository;

import com.likelion.sbstudy.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
