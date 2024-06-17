package com.test.book.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookDTO {

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
