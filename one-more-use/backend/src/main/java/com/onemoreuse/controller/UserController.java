package com.onemoreuse.controller;

import com.onemoreuse.dto.UserStatsDto;
import com.onemoreuse.entity.User;
import com.onemoreuse.service.ItemRequestService;
import com.onemoreuse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ItemRequestService itemRequestService;

    @Autowired
    public UserController(UserService userService, ItemRequestService itemRequestService) {
        this.userService = userService;
        this.itemRequestService = itemRequestService;
    }

    /**
     * Get user profile details by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable Long id) {
        return userService.findById(id).map(user -> {
            Map<String, Object> profile = new HashMap<>();
            profile.put("id", user.getId());
            profile.put("name", user.getName());
            profile.put("email", user.getEmail());
            profile.put("location", user.getLocation());
            profile.put("createdAt", user.getCreatedAt());
            return ResponseEntity.ok(profile);
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get statistics for a user profile (items posted, given, received, active requests).
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<UserStatsDto> getUserStats(@PathVariable Long id) {
        return ResponseEntity.ok(itemRequestService.getUserStats(id));
    }
}
