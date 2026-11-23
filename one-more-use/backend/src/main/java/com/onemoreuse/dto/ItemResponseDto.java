package com.onemoreuse.dto;

import com.onemoreuse.entity.Item;
import java.time.LocalDateTime;

public class ItemResponseDto {

    private Long id;
    private Long ownerId;
    private String ownerName;
    private String ownerLocation;
    private String title;
    private String description;
    private String category;
    private String itemCondition;
    private String approxSize;
    private String location;
    private String imageUrl;
    private String emoji;
    private String exchangeType;
    private String preferredExchangeItem;
    private Boolean isAvailable;
    private LocalDateTime createdAt;

    public ItemResponseDto() {
    }

    public static ItemResponseDto fromEntity(Item item) {
        ItemResponseDto dto = new ItemResponseDto();
        dto.setId(item.getId());
        if (item.getOwner() != null) {
            dto.setOwnerId(item.getOwner().getId());
            dto.setOwnerName(item.getOwner().getName());
            dto.setOwnerLocation(item.getOwner().getLocation());
        }
        dto.setTitle(item.getTitle());
        dto.setDescription(item.getDescription());
        dto.setCategory(item.getCategory());
        dto.setItemCondition(item.getItemCondition());
        dto.setApproxSize(item.getApproxSize());
        dto.setLocation(item.getLocation());
        dto.setImageUrl(item.getImageUrl());
        dto.setEmoji(item.getEmoji());
        dto.setExchangeType(item.getExchangeType());
        dto.setPreferredExchangeItem(item.getPreferredExchangeItem());
        dto.setIsAvailable(item.getIsAvailable());
        dto.setCreatedAt(item.getCreatedAt());
        return dto;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerLocation() {
        return ownerLocation;
    }

    public void setOwnerLocation(String ownerLocation) {
        this.ownerLocation = ownerLocation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getApproxSize() {
        return approxSize;
    }

    public void setApproxSize(String approxSize) {
        this.approxSize = approxSize;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public String getPreferredExchangeItem() {
        return preferredExchangeItem;
    }

    public void setPreferredExchangeItem(String preferredExchangeItem) {
        this.preferredExchangeItem = preferredExchangeItem;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
