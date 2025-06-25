package com.time.PokerFace.book.service;

import com.time.PokerFace.book.dto.BookSearchResponse;
import com.time.PokerFace.book.dto.BookRegisterRequest;
import com.time.PokerFace.book.dto.BookResponse;
import com.time.PokerFace.book.entity.Book;
import com.time.PokerFace.book.repository.BookRepository;
import com.time.PokerFace.common.service.BookApiService;
import com.time.PokerFace.memory.entity.Emotion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookApiService bookApiService;

    @Autowired
    public BookService(BookRepository bookRepository, BookApiService bookApiService) {
        this.bookRepository = bookRepository;
        this.bookApiService = bookApiService;
    }

    public BookSearchResponse searchBooks(String query) {
        return bookApiService.searchBooks(query);
    }

    public BookResponse registerBook(Long userId, BookRegisterRequest request) {
        Book book = new Book();
        book.setUserId(userId);
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setImageUrl(request.getImageUrl());
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setMemo(request.getMemo());
        if (request.getEmotion() != null && !request.getEmotion().isEmpty()) {
            book.setEmotion(Emotion.valueOf(request.getEmotion().toUpperCase()));
        }

        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    public List<BookResponse> getMyBooks(Long userId) {
        List<Book> books = bookRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<BookResponse> responses = new ArrayList<>();
        for (Book book : books) {
            responses.add(toResponse(book));
        }
        return responses;
    }

    public BookResponse getBookDetail(Long bookId, Long userId) {
        Optional<Book> optional = bookRepository.findById(bookId);
        if (!optional.isPresent()) {
            throw new RuntimeException("Book not found");
        }
        Book book = optional.get();
        if (!book.getUserId().equals(userId)) {
            throw new RuntimeException("No permission to access this book");
        }
        return toResponse(book);
    }

    private BookResponse toResponse(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setImageUrl(book.getImageUrl());
        response.setIsbn(book.getIsbn());
        response.setDescription(book.getDescription());
        response.setMemo(book.getMemo());
        response.setEmotion(book.getEmotion() != null ? book.getEmotion().name() : null);
        response.setCreatedAt(book.getCreatedAt() != null ? 
            book.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
        return response;
    }
} 