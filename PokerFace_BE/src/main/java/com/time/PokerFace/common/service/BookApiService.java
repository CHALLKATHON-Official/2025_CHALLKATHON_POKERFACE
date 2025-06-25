package com.time.PokerFace.common.service;

import com.time.PokerFace.book.dto.BookSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${book.api.url:https://www.googleapis.com/books/v1/volumes}")
    private String apiUrl;

    @Value("${book.api.key:}")
    private String apiKey;

    public BookApiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public BookSearchResponse searchBooks(String query) {
        try {
            String url = apiUrl + "?q=" + query + "&maxResults=10";
            if (apiKey != null && !apiKey.isEmpty()) {
                url += "&key=" + apiKey;
            }
            
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            
            BookSearchResponse searchResponse = new BookSearchResponse();
            List<BookSearchResponse.BookInfo> books = new ArrayList<>();
            
            JsonNode items = root.get("items");
            if (items != null && items.isArray()) {
                for (JsonNode item : items) {
                    JsonNode volumeInfo = item.get("volumeInfo");
                    if (volumeInfo != null) {
                        BookSearchResponse.BookInfo bookInfo = new BookSearchResponse.BookInfo();
                        bookInfo.setTitle(volumeInfo.get("title") != null ? volumeInfo.get("title").asText() : "");
                        
                        JsonNode authors = volumeInfo.get("authors");
                        if (authors != null && authors.isArray() && authors.size() > 0) {
                            bookInfo.setAuthor(authors.get(0).asText());
                        }
                        
                        JsonNode imageLinks = volumeInfo.get("imageLinks");
                        if (imageLinks != null && imageLinks.get("thumbnail") != null) {
                            bookInfo.setImageUrl(imageLinks.get("thumbnail").asText());
                        }
                        
                        JsonNode industryIdentifiers = volumeInfo.get("industryIdentifiers");
                        if (industryIdentifiers != null && industryIdentifiers.isArray()) {
                            for (JsonNode identifier : industryIdentifiers) {
                                if ("ISBN_13".equals(identifier.get("type").asText())) {
                                    bookInfo.setIsbn(identifier.get("identifier").asText());
                                    break;
                                }
                            }
                        }
                        
                        bookInfo.setDescription(volumeInfo.get("description") != null ? 
                            volumeInfo.get("description").asText() : "");
                        
                        books.add(bookInfo);
                    }
                }
            }
            
            searchResponse.setBooks(books);
            searchResponse.setTotalItems(root.get("totalItems") != null ? root.get("totalItems").asInt() : 0);
            
            return searchResponse;
        } catch (Exception e) {
            // 예시 데이터 반환 (API 호출 실패 시)
            BookSearchResponse searchResponse = new BookSearchResponse();
            List<BookSearchResponse.BookInfo> books = new ArrayList<>();
            
            BookSearchResponse.BookInfo book1 = new BookSearchResponse.BookInfo();
            book1.setTitle("예시 책 제목");
            book1.setAuthor("예시 저자");
            book1.setImageUrl("https://via.placeholder.com/128x192");
            book1.setIsbn("9781234567890");
            book1.setDescription("예시 책 설명입니다.");
            books.add(book1);
            
            searchResponse.setBooks(books);
            searchResponse.setTotalItems(1);
            
            return searchResponse;
        }
    }
} 