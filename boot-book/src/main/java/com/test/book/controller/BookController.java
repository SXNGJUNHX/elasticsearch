package com.test.book.controller;

import com.test.book.dto.BookDTO;
import com.test.book.entity.Book;
import com.test.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping(value="/list")
    public String list(Model model, String word){

        if(word == null || word.equals("")){
            //DB select
            List<Book> list = bookService.list();
            model.addAttribute("list", list);
        } else {
            //Elasticsearch query
            List<Map<String, Object>> list = bookService.search(word);
            model.addAttribute("list", list);
            model.addAttribute("word", word);
        }

        return "list";
    }

    @GetMapping(value="/add")
    public String add(){
        return "add";
    }

    @PostMapping(value="/addok")
    public String addok(BookDTO dto){

        Book result = bookService.add(dto); // DB > seq(id) > 자동 증가
        dto.setSeq(result.getSeq()); // PK
        bookService.addDocument(dto); // Document > id > 직접 대입


        return "redirect:/list";
    }

    @GetMapping(value="/view")
    public String view(Model model, String seq){

        Book book = bookService.get(seq);

        model.addAttribute("book", book);

        return "view";
    }
}
