package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.NotFoundParameterException;
import ru.practicum.ewm.exceptions.UpdateException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.category.mapper.CategoryMapper.toCategory;
import static ru.practicum.ewm.category.mapper.CategoryMapper.toCategoryDto;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("id").ascending());
        return categoryRepository.findAll(pageRequest).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundParameterException("Wrong category id (catId).");
        }
        return toCategoryDto(categoryRepository.getReferenceById(catId));
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {

        if (categoryDto.getName().equals("")) {
            throw new NotFoundParameterException("Sorry, but this name)");
        }

        if (categoryRepository.existsByName(categoryDto.getName()))
            throw new ConflictException("Name already exist.");

        if (!categoryRepository.existsById(categoryDto.getId()))
            throw new ConflictException("Category is not exist.");

        return toCategoryDto(categoryRepository.save(toCategory(categoryDto)));
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {


        if (categoryRepository.existsByName(categoryDto.getName()))
            throw new ConflictException("Name already exist.");

        return toCategoryDto(categoryRepository.save(toCategory(categoryDto)));
    }

    @Override
    public void deleteCategory(Long catId) {

        if (!categoryRepository.existsById(catId))
            throw new UpdateException("Category is not exist.");

        categoryRepository.deleteById(catId);
    }
}