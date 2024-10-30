package com.bookservice.controller;

import com.bookservice.dto.BookDto;
import com.bookservice.dto.CreateBookRequestDto;
import com.bookservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "Book controller")
public class BookController {
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save the book in database", description = "Returned the saved book")
    public BookDto save(@Valid @RequestBody CreateBookRequestDto request) {
        return bookService.save(request);
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Returns list books")
    public List<BookDto> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by id", description = "Returns a book as per the id")
    @Schema(name = "Book ID", example = "1", required = true)
    public BookDto findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @GetMapping("/specification")
    @Operation(summary = "Get a books by specific ids", description = "Returns a list books")
    public List<BookDto> findByCriteria(@Valid @RequestParam List<Long> bookIds) {
        return bookService.findByCriteria(bookIds);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update the book by id", description = "Returns updated book")
    public BookDto update(@PathVariable Long id, @RequestBody CreateBookRequestDto request) {
        return bookService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the book by id", description = "Nothing returns")
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
}
