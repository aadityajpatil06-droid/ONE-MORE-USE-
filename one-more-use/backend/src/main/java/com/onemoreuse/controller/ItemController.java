package com.onemoreuse.controller;

import com.onemoreuse.dto.ItemCreateDto;
import com.onemoreuse.dto.ItemResponseDto;
import com.onemoreuse.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Get all currently available items.
     */
    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllAvailableItems());
    }

    /**
     * Get a specific item by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Search available items by title, description, category, or location.
     */
    @GetMapping("/search")
    public ResponseEntity<List<ItemResponseDto>> searchItems(@RequestParam(required = false, defaultValue = "") String q) {
        return ResponseEntity.ok(itemService.searchItems(q));
    }

    /**
     * Get all items posted by a specific user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ItemResponseDto>> getItemsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(itemService.getItemsByOwner(userId));
    }

    /**
     * Create a new item listing with an optional uploaded image file (multipart/form-data).
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ItemResponseDto> createItemWithFile(
            @RequestParam("ownerId") Long ownerId,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam("itemCondition") String itemCondition,
            @RequestParam(value = "approxSize", required = false) String approxSize,
            @RequestParam("location") String location,
            @RequestParam("exchangeType") String exchangeType,
            @RequestParam(value = "preferredExchangeItem", required = false) String preferredExchangeItem,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {

        ItemCreateDto dto = new ItemCreateDto();
        dto.setOwnerId(ownerId);
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setCategory(category);
        dto.setItemCondition(itemCondition);
        dto.setApproxSize(approxSize);
        dto.setLocation(location);
        dto.setExchangeType(exchangeType);
        dto.setPreferredExchangeItem(preferredExchangeItem);

        ItemResponseDto created = itemService.createItem(dto, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Create a new item listing via JSON (for easy Postman testing).
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ItemResponseDto> createItemJson(@Valid @RequestBody ItemCreateDto dto) throws IOException {
        ItemResponseDto created = itemService.createItem(dto, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
