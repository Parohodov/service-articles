package ru.parohodov.servicearticles.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Pavel Popov
 */
@Data
public class ContentDto {
    private final String content;

    public ContentDto(String content) {
        this.content = content;
    }
}
