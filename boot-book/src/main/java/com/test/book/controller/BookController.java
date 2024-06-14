package com.test.book.controller;

import com.test.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping(value="/list")
    public String list(){
        return "list";
    }

    @GetMapping(value="/add")
    public String add(){
        return "add";
    }

    @PostMapping(value="/addok")
    public String addok(){
        return "redirect:/list";
    }

    @GetMapping(value="/view")
    public String view(){
        return "view";
    }
}
