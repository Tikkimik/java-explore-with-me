package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(Long userId);

    CategoryDto updateCategory(CategoryDto categoryDto);

    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategory(Long catId);
}
