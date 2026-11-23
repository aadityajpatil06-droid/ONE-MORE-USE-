package com.onemoreuse.dto;

public class UserStatsDto {

    private long itemsPosted;
    private long itemsGiven;
    private long itemsReceived;
    private long activeRequests;

    public UserStatsDto() {
    }

    public UserStatsDto(long itemsPosted, long itemsGiven, long itemsReceived, long activeRequests) {
        this.itemsPosted = itemsPosted;
        this.itemsGiven = itemsGiven;
        this.itemsReceived = itemsReceived;
        this.activeRequests = activeRequests;
    }

    public long getItemsPosted() {
        return itemsPosted;
    }

    public void setItemsPosted(long itemsPosted) {
        this.itemsPosted = itemsPosted;
    }

    public long getItemsGiven() {
        return itemsGiven;
    }

    public void setItemsGiven(long itemsGiven) {
        this.itemsGiven = itemsGiven;
    }

    public long getItemsReceived() {
        return itemsReceived;
    }

    public void setItemsReceived(long itemsReceived) {
        this.itemsReceived = itemsReceived;
    }

    public long getActiveRequests() {
        return activeRequests;
    }

    public void setActiveRequests(long activeRequests) {
        this.activeRequests = activeRequests;
    }
}
