package com.onemoreuse.repository;

import com.onemoreuse.entity.ItemRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    // Fetch incoming requests received by an item owner
    List<ItemRequest> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);

    // Fetch outgoing requests sent by a requester
    List<ItemRequest> findByRequesterIdOrderByCreatedAtDesc(Long requesterId);

    // Check if user already submitted a request for this item
    boolean existsByItemIdAndRequesterId(Long itemId, Long requesterId);

    // Count active (PENDING) requests for a user as owner or requester
    long countByOwnerIdAndStatus(Long ownerId, String status);

    long countByRequesterIdAndStatus(Long requesterId, String status);

    // Count items received (accepted requests for a requester)
    long countByRequesterIdAndStatusIgnoreCase(Long requesterId, String status);
}
