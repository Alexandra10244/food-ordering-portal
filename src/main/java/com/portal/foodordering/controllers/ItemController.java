package com.portal.foodordering.controllers;

import com.portal.foodordering.models.dtos.ItemDTO;
import com.portal.foodordering.serivces.interfaces.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/items")
public class ItemController {

    private ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.createItem(itemDTO));
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @PatchMapping
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Long id, @RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.updateItem(id, itemDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.deleteItem(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> findItemById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.findItemById(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ItemDTO> findItemByName(@PathVariable String name) {
        return ResponseEntity.ok(itemService.findItemByName(name));
    }

}
