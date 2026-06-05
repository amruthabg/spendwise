package com.gaonkar.spendwise.service;

import com.gaonkar.spendwise.dto.CategoryRequest;
import com.gaonkar.spendwise.exception.BadRequestException;
import com.gaonkar.spendwise.exception.ResourceNotFoundException;
import com.gaonkar.spendwise.model.Category;
import com.gaonkar.spendwise.model.User;
import com.gaonkar.spendwise.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public CategoryService(CategoryRepository categoryRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    @Transactional
    public Category createCategory(CategoryRequest request) {
        User user = userService.getUserById(request.getUserId());
        
        categoryRepository.findByNameAndUserId(request.getName(), request.getUserId())
                .ifPresent(c -> {
                    throw new BadRequestException("Category already exists: " + request.getName());
                });

        Category category = new Category(request.getName(), user);
        return categoryRepository.save(category);
    }

    public List<Category> getCategoriesByUser(Long userId) {
        // Verify user exists
        userService.getUserById(userId);
        return categoryRepository.findByUserId(userId);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}
