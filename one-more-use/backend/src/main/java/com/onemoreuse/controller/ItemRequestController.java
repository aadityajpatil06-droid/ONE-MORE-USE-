package com.onemoreuse.controller;

import com.onemoreuse.dto.CreateRequestDto;
import com.onemoreuse.dto.RequestResponseDto;
import com.onemoreuse.service.ItemRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    /**
     * Send a request for an item.
     */
    @PostMapping
    public ResponseEntity<RequestResponseDto> createRequest(@Valid @RequestBody CreateRequestDto dto) {
        RequestResponseDto response = itemRequestService.createRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all incoming requests for items owned by the specified user.
     */
    @GetMapping("/incoming/{userId}")
    public ResponseEntity<List<RequestResponseDto>> getIncomingRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(itemRequestService.getIncomingRequests(userId));
    }

    /**
     * Get all outgoing requests sent by the specified user.
     */
    @GetMapping("/outgoing/{userId}")
    public ResponseEntity<List<RequestResponseDto>> getOutgoingRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(itemRequestService.getOutgoingRequests(userId));
    }

    /**
     * Item owner accepts or rejects a request.
     * PUT /api/requests/{id}/status with body: {"ownerId": 2, "status": "ACCEPTED"}
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<RequestResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {

        if (body == null || !body.containsKey("ownerId") || !body.containsKey("status")) {
            return ResponseEntity.badRequest().build();
        }
        Long ownerId = Long.valueOf(body.get("ownerId").toString());
        String status = body.get("status").toString();

        RequestResponseDto updated = itemRequestService.updateStatus(id, ownerId, status);
        return ResponseEntity.ok(updated);
    }
}
