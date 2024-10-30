package com.bookservice.service;

import com.bookservice.dto.BookDto;
import com.bookservice.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookRequestDto);

    List<BookDto> findByCriteria(List<Long> bookIds);

    void delete(Long id);
}
