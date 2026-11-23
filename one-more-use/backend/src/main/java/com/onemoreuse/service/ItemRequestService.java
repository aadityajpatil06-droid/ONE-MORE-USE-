package com.onemoreuse.service;

import com.onemoreuse.dto.CreateRequestDto;
import com.onemoreuse.dto.RequestResponseDto;
import com.onemoreuse.dto.UserStatsDto;
import com.onemoreuse.entity.Item;
import com.onemoreuse.entity.ItemRequest;
import com.onemoreuse.entity.User;
import com.onemoreuse.repository.ItemRepository;
import com.onemoreuse.repository.ItemRequestRepository;
import com.onemoreuse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemRequestService(ItemRequestRepository itemRequestRepository,
                              ItemRepository itemRepository,
                              UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new request for an item.
     * Enforces business rules:
     * 1. Item must exist and be available.
     * 2. User cannot request their own item.
     * 3. User cannot request the same item multiple times.
     */
    @Transactional
    public RequestResponseDto createRequest(CreateRequestDto dto) {
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("Item not found with ID: " + dto.getItemId()));

        if (!Boolean.TRUE.equals(item.getIsAvailable())) {
            throw new IllegalStateException("This item is no longer available.");
        }

        User requester = userRepository.findById(dto.getRequesterId())
                .orElseThrow(() -> new IllegalArgumentException("Requester not found with ID: " + dto.getRequesterId()));

        User owner = item.getOwner();

        // Prevent users from requesting their own items
        if (Objects.equals(owner.getId(), requester.getId())) {
            throw new IllegalArgumentException("You cannot request an item that you posted yourself.");
        }

        // Prevent duplicate request
        if (itemRequestRepository.existsByItemIdAndRequesterId(item.getId(), requester.getId())) {
            throw new IllegalStateException("You have already submitted a request for this item.");
        }

        ItemRequest request = new ItemRequest();
        request.setItem(item);
        request.setRequester(requester);
        request.setOwner(owner);
        request.setMessage(dto.getMessage() != null ? dto.getMessage().trim() : "Hi, I am interested in this item!");
        request.setStatus("PENDING");

        ItemRequest saved = itemRequestRepository.save(request);
        return RequestResponseDto.fromEntity(saved);
    }

    public List<RequestResponseDto> getIncomingRequests(Long ownerId) {
        return itemRequestRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId)
                .stream()
                .map(RequestResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RequestResponseDto> getOutgoingRequests(Long requesterId) {
        return itemRequestRepository.findByRequesterIdOrderByCreatedAtDesc(requesterId)
                .stream()
                .map(RequestResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public RequestResponseDto updateStatus(Long requestId, Long ownerId, String newStatus) {
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found with ID: " + requestId));

        // Validate that only the item owner can accept or reject
        if (!Objects.equals(request.getOwner().getId(), ownerId)) {
            throw new IllegalArgumentException("Only the owner of the item can respond to this request.");
        }

        String normalizedStatus = newStatus.trim().toUpperCase();
        if (!normalizedStatus.equals("ACCEPTED") && !normalizedStatus.equals("REJECTED")) {
            throw new IllegalArgumentException("Status must be either ACCEPTED or REJECTED.");
        }

        request.setStatus(normalizedStatus);
        ItemRequest updated = itemRequestRepository.save(request);

        return RequestResponseDto.fromEntity(updated);
    }

    public UserStatsDto getUserStats(Long userId) {
        long itemsPosted = itemRepository.countByOwnerId(userId);
        long itemsGiven = itemRepository.countByOwnerIdAndIsAvailableFalse(userId);
        long itemsReceived = itemRequestRepository.countByRequesterIdAndStatusIgnoreCase(userId, "ACCEPTED");
        long activeRequests = itemRequestRepository.countByOwnerIdAndStatus(userId, "PENDING")
                + itemRequestRepository.countByRequesterIdAndStatus(userId, "PENDING");

        return new UserStatsDto(itemsPosted, itemsGiven, itemsReceived, activeRequests);
    }
}
