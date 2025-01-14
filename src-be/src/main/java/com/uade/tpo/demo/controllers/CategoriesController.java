package com.uade.tpo.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.dto.CategoryRequest;
import com.uade.tpo.demo.exceptions.CategoryDuplicateException;
import com.uade.tpo.demo.service.CategoryService;
import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*
 * Controlador de categorías.
 */
@RestController
@CrossOrigin("*")
@RequestMapping("categories")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    /*
     * Obtiene todas las categorías.
     * 
     * @param page: Página a obtener.
     * 
     * @param size: Cantidad de elementos por página.
     */
    @GetMapping
    public ResponseEntity<Page<Category>> getCategories(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page == null || size == null)
            return ResponseEntity.ok(categoryService.getCategories(PageRequest.of(0, Integer.MAX_VALUE)));
        return ResponseEntity.ok(categoryService.getCategories(PageRequest.of(page, size)));
    }
    // @GetMapping
    // public ResponseEntity<Page<Category>> getCategories(
    // @RequestParam(required = false) Integer page,
    // @RequestParam(required = false) Integer size) {
    // if (page == null || size == null)
    // return ResponseEntity.ok(categoryService.getCategories(PageRequest.of(0,
    // Integer.MAX_VALUE)));
    // return ResponseEntity.ok(categoryService.getCategories(PageRequest.of(page,
    // size)));
    // }

    /*
     * Obtiene una categoría por su id.
     * 
     * @param categoryId: Id de la categoría a buscar.
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) {
        Optional<Category> result = categoryService.getCategoryById(categoryId);
        if (result.isPresent())
            return ResponseEntity.ok(result.get());

        return ResponseEntity.noContent().build();
    }

    /*
     * Crea una categoría.
     * 
     * @param categoryRequest: CategoryRequest con los datos de la categoría a
     * crear.
     * 
     * @throws CategoryDuplicateException: Si ya existe una categoría con la misma
     * descripción.
     */
    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody CategoryRequest categoryRequest)
            throws CategoryDuplicateException {
        Category result = categoryService.createCategory(categoryRequest.getDescription());
        return ResponseEntity.created(URI.create("/categories/" + result.getId())).body(result);
    }
}
