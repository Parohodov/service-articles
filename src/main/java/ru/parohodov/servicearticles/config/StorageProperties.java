package ru.parohodov.servicearticles.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Pavel Popov
 */
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {
    /**
     * Folder location for storing files
     */
    @Getter
    @Setter
    private String location;
    @Getter
    @Setter
    private String articleFileName;
}
