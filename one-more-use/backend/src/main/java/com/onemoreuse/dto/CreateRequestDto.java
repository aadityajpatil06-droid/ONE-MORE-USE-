package com.onemoreuse.dto;

import jakarta.validation.constraints.NotNull;

public class CreateRequestDto {

    @NotNull(message = "Item ID is required")
    private Long itemId;

    @NotNull(message = "Requester user ID is required")
    private Long requesterId;

    private String message;

    public CreateRequestDto() {
    }

    public CreateRequestDto(Long itemId, Long requesterId, String message) {
        this.itemId = itemId;
        this.requesterId = requesterId;
        this.message = message;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
