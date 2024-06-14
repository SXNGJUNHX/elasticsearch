package com.test.book.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="tblBook")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq_generator")
    @SequenceGenerator(name = "book_seq_generator", sequenceName = "seqBook", allocationSize = 1)
    private Long seq;

    private String title;
    private String link;
    private String description;
    private String image;
    private String author;
    private int discount;
    private String publisher;
    private String isbn;
    private String pubdate;

}
