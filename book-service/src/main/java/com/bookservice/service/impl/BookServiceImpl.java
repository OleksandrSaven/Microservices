package com.bookservice.service.impl;

import com.bookservice.domain.Book;
import com.bookservice.domain.Category;
import com.bookservice.dto.BookDto;
import com.bookservice.dto.CreateBookRequestDto;
import com.bookservice.exception.EntityNotFoundException;
import com.bookservice.mapper.BookMapper;
import com.bookservice.repository.BookRepository;
import com.bookservice.service.BookService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.dtoToBook(requestDto);
        return bookMapper.bookToDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::bookToDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find the book by id " + id));
        return bookMapper.bookToDto(book);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto bookRequestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book with id " + id));
        book.setTitle(bookRequestDto.title());
        book.setAuthor(bookRequestDto.author());
        book.setIsbn(bookRequestDto.isbn());
        book.setPrice(bookRequestDto.price());
        book.setDescription(bookRequestDto.description());
        book.setCoverImage(bookRequestDto.coverImage());
        Set<Category> categories = bookRequestDto.categoryIds().stream()
                .map(Category::new)
                .collect(Collectors.toSet());
        book.setCategories(categories);
        return bookMapper.bookToDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findByCriteria(List<Long> bookIds) {
        Specification<Book> specification = getSpecification(bookIds);
        List<Book> books = bookRepository.findAll(specification);
        return books.stream().map(bookMapper::bookToDto).toList();
    }

    @Override
    public void delete(Long id) {
        bookRepository.delete(bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find book by id " + id)));
    }

    private Specification<Book> getSpecification(List<Long> bookIds) {
        return (root, query, criteriaBuilder) -> {
            return root.get("id").in(bookIds);
        };
    }
}
