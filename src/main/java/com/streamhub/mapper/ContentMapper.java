package com.streamhub.mapper;

import com.streamhub.dto.CatalogResponseDTO;
import com.streamhub.dto.MediaItemDTO;
import com.streamhub.entity.Content;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper for converting Content entities to DTOs
 */
public class ContentMapper {

    /**
     * Convert Content entity to MediaItemDTO
     */
    public static MediaItemDTO toMediaItemDTO(Content content) {
        return MediaItemDTO.builder()
            .title(content.getTitle())
            .description(content.getDescription())
            .genre(content.getGenre())
            .language(content.getLanguage())
            .type(content.getContentType())
            .rating(content.getRating())
            .thumbnailURL(content.getThumbnailURL())
            .duration(content.getDuration())
            .build();
    }

    /**
     * Convert Page of Content entities to CatalogResponseDTO
     */
    public static CatalogResponseDTO toCatalogResponseDTO(Page<Content> contentPage, int requestedPage) {
        List<MediaItemDTO> mediaItems = contentPage.getContent().stream()
            .map(ContentMapper::toMediaItemDTO)
            .collect(Collectors.toList());
        
        Map<String, List<MediaItemDTO>> categories = new HashMap<>();
        categories.put("media", mediaItems);
        
        return CatalogResponseDTO.builder()
            .status("success")
            .count(mediaItems.size())
            .page(requestedPage)
            .total((int) contentPage.getTotalElements())
            .categories(categories)
            .build();
    }

    /**
     * Create error response DTO
     */
    public static CatalogResponseDTO toErrorResponseDTO(int page) {
        return CatalogResponseDTO.builder()
            .status("error")
            .count(0)
            .page(page)
            .total(0)
            .build();
    }
}
