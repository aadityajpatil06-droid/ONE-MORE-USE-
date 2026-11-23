package com.onemoreuse.repository;

import com.onemoreuse.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Fetch all currently available items ordered by newest first
    List<Item> findByIsAvailableTrueOrderByCreatedAtDesc();

    // Fetch all items posted by a specific owner
    List<Item> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);

    // Count items posted by an owner
    long countByOwnerId(Long ownerId);

    // Count items given (items owned by user that are no longer available)
    long countByOwnerIdAndIsAvailableFalse(Long ownerId);

    // Search query by keyword in title, description, category or location
    @Query("SELECT i FROM Item i WHERE i.isAvailable = true AND (" +
           "LOWER(i.title) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(i.category) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(i.location) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Item> searchAvailableItems(@Param("q") String query);

    // Filter by location
    List<Item> findByIsAvailableTrueAndLocationContainingIgnoreCase(String location);

    // Filter by exchange type
    List<Item> findByIsAvailableTrueAndExchangeType(String exchangeType);
}
