package com.streamhub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object for the complete catalog API response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogResponseDTO {
    private String status;
    private Integer count;
    private Integer page;
    private Integer total;
    
    @JsonProperty("categories")
    private Map<String, List<MediaItemDTO>> categories;
}
