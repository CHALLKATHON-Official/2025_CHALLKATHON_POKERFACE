package com.time.PokerFace.book.controller;

import com.time.PokerFace.book.dto.BookSearchResponse;
import com.time.PokerFace.book.dto.BookRegisterRequest;
import com.time.PokerFace.book.dto.BookResponse;
import com.time.PokerFace.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/search")
    public ResponseEntity<BookSearchResponse> searchBooks(@RequestParam String query) {
        BookSearchResponse response = bookService.searchBooks(query);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BookResponse> registerBook(@RequestBody BookRegisterRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        BookResponse response = bookService.registerBook(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getMyBooks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        List<BookResponse> responses = bookService.getMyBooks(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookDetail(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            userId = Long.parseLong(username); // 실제 구현에 맞게 수정 필요
        } else {
            return ResponseEntity.status(401).build();
        }
        BookResponse response = bookService.getBookDetail(id, userId);
        return ResponseEntity.ok(response);
    }
} 