package com.likelion.sbstudy.domain.book.entity;

import com.likelion.sbstudy.global.common.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "book")
public class Book extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "author", nullable = false)
  private String author;

  @Column(name = "publisher", nullable = false)
  private String publisher;

  @Column(name = "price", nullable = false)
  private Integer price;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "release_date", nullable = false)
  private String releaseDate;

  @Column(name = "category_list", nullable = false)
  private List<Category> categoryList;

  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BookImage> bookImages = new ArrayList<>();

  public void addBookImages(List<BookImage> bookImages) {
    this.bookImages = bookImages;
  }
}
