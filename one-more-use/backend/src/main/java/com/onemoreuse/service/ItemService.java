package com.onemoreuse.service;

import com.onemoreuse.dto.ItemCreateDto;
import com.onemoreuse.dto.ItemResponseDto;
import com.onemoreuse.entity.Item;
import com.onemoreuse.entity.User;
import com.onemoreuse.repository.ItemRepository;
import com.onemoreuse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Autowired
    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public List<ItemResponseDto> getAllAvailableItems() {
        return itemRepository.findByIsAvailableTrueOrderByCreatedAtDesc()
                .stream()
                .map(ItemResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<ItemResponseDto> getItemById(Long id) {
        return itemRepository.findById(id).map(ItemResponseDto::fromEntity);
    }

    public List<ItemResponseDto> getItemsByOwner(Long ownerId) {
        return itemRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId)
                .stream()
                .map(ItemResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ItemResponseDto> searchItems(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllAvailableItems();
        }
        return itemRepository.searchAvailableItems(query.trim())
                .stream()
                .map(ItemResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ItemResponseDto createItem(ItemCreateDto dto, MultipartFile imageFile) throws IOException {
        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + dto.getOwnerId()));

        Item item = new Item();
        item.setOwner(owner);
        item.setTitle(dto.getTitle().trim());
        item.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : "");
        item.setCategory(dto.getCategory() != null && !dto.getCategory().isBlank() ? dto.getCategory().trim() : "General Household");
        item.setItemCondition(dto.getItemCondition().trim());
        item.setApproxSize(dto.getApproxSize() != null ? dto.getApproxSize().trim() : "Medium (fits in a bag)");
        item.setLocation(dto.getLocation().trim());

        // Validate exchange type (must be FREE or EXCHANGE)
        String exchangeType = "FREE";
        if (dto.getExchangeType() != null && dto.getExchangeType().equalsIgnoreCase("EXCHANGE")) {
            exchangeType = "EXCHANGE";
            item.setPreferredExchangeItem(dto.getPreferredExchangeItem());
        }
        item.setExchangeType(exchangeType);

        // Assign a friendly emoji representation
        item.setEmoji(determineEmoji(dto.getTitle(), dto.getCategory(), dto.getEmoji()));

        // Handle uploaded image if present
        if (imageFile != null && !imageFile.isEmpty()) {
            String savedFilename = saveImageFile(imageFile);
            item.setImageUrl("/uploads/" + savedFilename);
        }

        item.setIsAvailable(true);
        Item saved = itemRepository.save(item);
        return ItemResponseDto.fromEntity(saved);
    }

    private String saveImageFile(MultipartFile file) throws IOException {
        // Validate MIME type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files (JPG, PNG, GIF, WEBP) are allowed.");
        }

        // Validate file size limit (10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("Uploaded image exceeds the 10MB limit.");
        }

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        } else {
            extension = ".jpg";
        }

        String safeFileName = UUID.randomUUID().toString() + extension;
        Path targetPath = uploadPath.resolve(safeFileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return safeFileName;
    }

    private String determineEmoji(String title, String category, String customEmoji) {
        if (customEmoji != null && !customEmoji.isBlank()) {
            return customEmoji;
        }
        String combined = (title + " " + (category != null ? category : "")).toLowerCase();
        if (combined.contains("box") || combined.contains("crate") || combined.contains("wood")) return "🪵";
        if (combined.contains("chair") || combined.contains("stool")) return "🪑";
        if (combined.contains("lamp") || combined.contains("light")) return "💡";
        if (combined.contains("book") || combined.contains("shelf")) return "📚";
        if (combined.contains("cycle") || combined.contains("bike")) return "🚲";
        if (combined.contains("jar") || combined.contains("bottle") || combined.contains("mixer")) return "🫙";
        if (combined.contains("cloth") || combined.contains("shirt")) return "👕";
        if (combined.contains("plant") || combined.contains("pot")) return "🪴";
        return "📦";
    }
}
