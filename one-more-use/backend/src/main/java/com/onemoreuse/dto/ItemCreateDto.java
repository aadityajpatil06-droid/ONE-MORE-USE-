package com.onemoreuse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ItemCreateDto {

    @NotNull(message = "Owner ID is required")
    private Long ownerId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private String category;

    @NotBlank(message = "Condition is required")
    private String itemCondition;

    private String approxSize;

    @NotBlank(message = "Location is required")
    private String location;

    private String emoji;

    @NotBlank(message = "Exchange type is required (FREE or EXCHANGE)")
    private String exchangeType;

    private String preferredExchangeItem;

    public ItemCreateDto() {
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
}
