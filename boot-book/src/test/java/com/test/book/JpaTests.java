package com.test.book;

import com.test.book.entity.Book;
import com.test.book.repository.BookRepository;
import com.test.book.repository.ElasticsearchRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class JpaTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ElasticsearchRepository elasticsearchRepository;

    @Test
    @Disabled
    public void test(){

        Optional<Book> book = bookRepository.findById(1L);
        System.out.println(book);

    }

    @Test
    public void testElasticsearch(){

        Assertions.assertNotNull(elasticsearchRepository);
    }

}
