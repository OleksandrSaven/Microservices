package com.bookservice.service.impl;

import com.bookservice.domain.Book;
import com.bookservice.domain.Category;
import com.bookservice.dto.BookDto;
import com.bookservice.dto.CategoryDto;
import com.bookservice.dto.CreateCategoryDto;
import com.bookservice.exception.EntityNotFoundException;
import com.bookservice.mapper.BookMapper;
import com.bookservice.mapper.CategoryMapper;
import com.bookservice.repository.BookRepository;
import com.bookservice.repository.CategoryRepository;
import com.bookservice.service.CategoryService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public CategoryDto save(CreateCategoryDto requestDto) {
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toModel(requestDto)));
    }

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto findById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id " + id)));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryDto request) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id " + id));
        category.setName(request.name());
        category.setDescription(request.description());
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<BookDto> getBooksByCategoryId(Long id) {
        categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id " + id));
        List<Book> books = bookRepository.findByCategoryId(id);
        return books.stream().map(bookMapper::bookToDto).collect(Collectors.toList());
    }
}
