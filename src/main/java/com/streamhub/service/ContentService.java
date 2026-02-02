package com.streamhub.service;

import com.streamhub.dto.CatalogResponseDTO;
import com.streamhub.entity.Content;
import com.streamhub.mapper.ContentMapper;
import com.streamhub.repository.ContentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@Transactional
public class ContentService {

    private static final Logger logger = Logger.getLogger(ContentService.class.getName());
    private final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    private Page<Content> getAllContent(Pageable pageable) {
        logger.info("Fetching content with pagination: page=" + pageable.getPageNumber() + ", size=" + pageable.getPageSize());
        return contentRepository.findAll(pageable);
    }

    public Content createContent(Content content) {
        logger.info("Creating new content: " + content.getTitle());
        
        if (contentRepository.existsByTitle(content.getTitle())) {
            logger.warning("Content with title '" + content.getTitle() + "' already exists");
            throw new IllegalArgumentException("Content with this title already exists");
        }
        
        Content savedContent = contentRepository.save(content);
        logger.info("Content created successfully with ID: " + savedContent.getId());
        return savedContent;
    }

    public Optional<Content> getContentById(Long contentId) {
        logger.fine("Fetching content with ID: " + contentId);
        return contentRepository.findById(contentId);
    }

    private Page<Content> getCatalog(int page, int pageSize, String sortBy, String sortDirection) {
        logger.info("Fetching catalog - page=" + page + ", pageSize=" + pageSize + 
                    ", sortBy=" + sortBy + ", sortDirection=" + sortDirection);
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") 
            ? Sort.Direction.ASC 
            : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(direction, sortBy));
        return contentRepository.findAll(pageable);
    }

    public CatalogResponseDTO getCatalogResponse(int page, int pageSize, String sortBy, String sortDirection) {
        logger.info("Building catalog response - page=" + page + ", pageSize=" + pageSize + 
                    ", sortBy=" + sortBy + ", sortDirection=" + sortDirection);
        
        Page<Content> contentPage = getCatalog(page, pageSize, sortBy, sortDirection);
        return ContentMapper.toCatalogResponseDTO(contentPage, page);
    }

    public CatalogResponseDTO getContentByTypeResponse(String contentType, int page, int pageSize, 
                                                      String sortBy, String sortDirection) {
        logger.info("Building content by type response - type=" + contentType + ", page=" + page);
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") 
            ? Sort.Direction.ASC 
            : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(direction, sortBy));
        Page<Content> contentPage = contentRepository.findByContentType(contentType, pageable);
        return ContentMapper.toCatalogResponseDTO(contentPage, page);
    }

    public CatalogResponseDTO getContentByGenreResponse(String genre, int page, int pageSize, 
                                                       String sortBy, String sortDirection) {
        logger.info("Building content by genre response - genre=" + genre + ", page=" + page);
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") 
            ? Sort.Direction.ASC 
            : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(direction, sortBy));
        Page<Content> contentPage = contentRepository.findByGenre(genre, pageable);
        return ContentMapper.toCatalogResponseDTO(contentPage, page);
    }

    public CatalogResponseDTO searchContentResponse(String keyword, int page, int pageSize, 
                                                    String sortBy, String sortDirection) {
        logger.info("Building search response - keyword=" + keyword + ", page=" + page);
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") 
            ? Sort.Direction.ASC 
            : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(direction, sortBy));
        Page<Content> contentPage = contentRepository.searchByTitle(keyword, pageable);
        return ContentMapper.toCatalogResponseDTO(contentPage, page);
    }

    public Content updateContent(Long contentId, Content updatedContent) {
        logger.info("Updating content with ID: " + contentId);
        
        return contentRepository.findById(contentId)
            .map(existing -> {
                existing.setTitle(updatedContent.getTitle());
                existing.setDescription(updatedContent.getDescription());
                existing.setContentType(updatedContent.getContentType());
                existing.setGenre(updatedContent.getGenre());
                existing.setLanguage(updatedContent.getLanguage());
                existing.setStatus(updatedContent.getStatus());
                existing.setMetadata(updatedContent.getMetadata());
                existing.setUpdatedBy(updatedContent.getUpdatedBy());
                
                Content saved = contentRepository.save(existing);
                logger.info("Content with ID " + contentId + " updated successfully");
                return saved;
            })
            .orElseThrow(() -> {
                logger.severe("Content with ID " + contentId + " not found");
                return new RuntimeException("Content not found with ID: " + contentId);
            });
    }

    public void deleteContent(Long contentId) {
        logger.info("Deleting content with ID: " + contentId);
        
        if (!contentRepository.existsById(contentId)) {
            logger.severe("Content with ID " + contentId + " not found");
            throw new RuntimeException("Content not found with ID: " + contentId);
        }
        
        contentRepository.deleteById(contentId);
        logger.info("Content with ID " + contentId + " deleted successfully");
    }

    public ContentStats getStatistics() {
        logger.info("Fetching content statistics");
        return new ContentStats(
            contentRepository.count(),
            contentRepository.countByStatus("ACTIVE")
        );
    }

    public static class ContentStats {
        private Long totalContent;
        private Long activeContent;

        public ContentStats(Long totalContent, Long activeContent) {
            this.totalContent = totalContent;
            this.activeContent = activeContent;
        }

        public Long getTotalContent() {
            return totalContent;
        }

        public void setTotalContent(Long totalContent) {
            this.totalContent = totalContent;
        }

        public Long getActiveContent() {
            return activeContent;
        }

        public void setActiveContent(Long activeContent) {
            this.activeContent = activeContent;
        }

        @Override
        public String toString() {
            return "ContentStats{" +
                    "totalContent=" + totalContent +
                    ", activeContent=" + activeContent +
                    '}';
        }
    }
}
