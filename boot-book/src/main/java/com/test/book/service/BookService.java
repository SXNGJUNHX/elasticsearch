package com.test.book.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.book.dto.BookDTO;
import com.test.book.entity.Book;
import com.test.book.repository.BookRepository;
import com.test.book.repository.ElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    //JPA Repository
    private final BookRepository bookRepository;

    //Elasticsearch Repository
    private final ElasticsearchRepository elasticsearchRepository;

    public Book add(BookDTO dto) {

        //DTO > (변환) > Entity
        Book book = Book.builder()
                .seq(dto.getSeq())
                .title(dto.getTitle())
                .link(dto.getLink())
                .description(dto.getDescription())
                .image(dto.getImage())
                .author(dto.getAuthor())
                .discount(dto.getDiscount())
                .publisher(dto.getPublisher())
                .isbn(dto.getIsbn())
                .pubdate(dto.getPubdate())
                .build();

        return bookRepository.save(book); //insert or update

    }

    public List<Book> list() {

        List<Book> list = bookRepository.findAll(Sort.by(Sort.Direction.DESC, "seq"));

        return list;
    }

    public Book get(String seq) {

        Book book = bookRepository.findById(Long.parseLong(seq)).get();

        return book;
    }

    public List<Map<String, Object>> search(String word) {

        List<Map<String, Object>> list = elasticsearchRepository.search(word);

        return list;

    }

    public void addDocument(BookDTO dto) {

        elasticsearchRepository.addDocument(dto);

    }
}
