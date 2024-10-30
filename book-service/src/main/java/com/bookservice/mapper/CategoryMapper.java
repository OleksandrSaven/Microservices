package com.bookservice.mapper;

import com.bookservice.domain.Category;
import com.bookservice.dto.CategoryDto;
import com.bookservice.dto.CreateCategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    Category toModel(CreateCategoryDto requestDto);
}
