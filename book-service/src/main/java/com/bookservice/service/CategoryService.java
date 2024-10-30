package com.bookservice.service;

import com.bookservice.dto.BookDto;
import com.bookservice.dto.CategoryDto;
import com.bookservice.dto.CreateCategoryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryDto save(CreateCategoryDto requestDto);

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto findById(Long id);

    CategoryDto update(Long id, CreateCategoryDto request);

    void delete(Long id);

    List<BookDto> getBooksByCategoryId(Long id);
}
