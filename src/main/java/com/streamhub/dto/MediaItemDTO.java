package com.streamhub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for media items in the catalog response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaItemDTO {
    private String title;
    private String description;
    private String genre;
    private String language;
    private String type;  // Movie, Series, etc.
    private String rating;  // PG-13, R, etc.
    private String thumbnailURL;
    private Integer duration;  // Optional: duration in minutes
}
