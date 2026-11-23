package com.onemoreuse.dto;

import com.onemoreuse.entity.ItemRequest;
import java.time.LocalDateTime;

public class RequestResponseDto {

    private Long id;
    private Long itemId;
    private String itemTitle;
    private String itemEmoji;
    private String itemExchangeType;
    private Long requesterId;
    private String requesterName;
    private String requesterLocation;
    private Long ownerId;
    private String ownerName;
    private String ownerLocation;
    private String message;
    private String status; // 'PENDING', 'ACCEPTED', 'REJECTED'
    private LocalDateTime createdAt;

    public RequestResponseDto() {
    }

    public static RequestResponseDto fromEntity(ItemRequest req) {
        RequestResponseDto dto = new RequestResponseDto();
        dto.setId(req.getId());
        if (req.getItem() != null) {
            dto.setItemId(req.getItem().getId());
            dto.setItemTitle(req.getItem().getTitle());
            dto.setItemEmoji(req.getItem().getEmoji());
            dto.setItemExchangeType(req.getItem().getExchangeType());
        }
        if (req.getRequester() != null) {
            dto.setRequesterId(req.getRequester().getId());
            dto.setRequesterName(req.getRequester().getName());
            dto.setRequesterLocation(req.getRequester().getLocation());
        }
        if (req.getOwner() != null) {
            dto.setOwnerId(req.getOwner().getId());
            dto.setOwnerName(req.getOwner().getName());
            dto.setOwnerLocation(req.getOwner().getLocation());
        }
        dto.setMessage(req.getMessage());
        dto.setStatus(req.getStatus());
        dto.setCreatedAt(req.getCreatedAt());
        return dto;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemEmoji() {
        return itemEmoji;
    }

    public void setItemEmoji(String itemEmoji) {
        this.itemEmoji = itemEmoji;
    }

    public String getItemExchangeType() {
        return itemExchangeType;
    }

    public void setItemExchangeType(String itemExchangeType) {
        this.itemExchangeType = itemExchangeType;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequesterLocation() {
        return requesterLocation;
    }

    public void setRequesterLocation(String requesterLocation) {
        this.requesterLocation = requesterLocation;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
